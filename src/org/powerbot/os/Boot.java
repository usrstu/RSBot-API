package org.powerbot.os;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.powerbot.os.gui.BotChrome;
import org.powerbot.os.misc.PrintStreamHandler;
import org.powerbot.os.misc.Resources;
import org.powerbot.os.util.IOUtils;
import org.powerbot.os.util.StringUtils;

public class Boot implements Runnable {
	private static final String SWITCH_RESTARTED = "-restarted", SWITCH_DEBUG = "-debug";

	public static void main(final String[] args) {
		boolean fork = true;

		for (final String a : args) {
			if (a.equalsIgnoreCase(SWITCH_RESTARTED) || a.equalsIgnoreCase(SWITCH_DEBUG)) {
				fork = false;
			}
		}

		if (fork) {
			fork();
		} else {
			new Boot().run();
		}
	}

	@Override
	public void run() {
		final Logger logger = Logger.getLogger("");
		for (final Handler handler : logger.getHandlers()) {
			logger.removeHandler(handler);
		}
		logger.addHandler(new PrintStreamHandler());

		if (Configuration.OS == Configuration.OperatingSystem.MAC) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}

		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("http.keepalive", "false");

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (final Exception ignored) {
				}

				System.setProperty("java.net.preferIPv4Stack", "true");
				System.setProperty("http.keepalive", "false");

				BotChrome.getInstance();
			}
		});
	}

	public static void fork() {
		final List<String> args = new ArrayList<String>();
		args.add("java");

		args.add("-Xmx512m");
		args.add("-Xss2m");
		args.add("-Dsun.java2d.noddraw=true");
		args.add("-XX:CompileThreshold=1500");
		args.add("-Xincgc");
		args.add("-XX:+UseConcMarkSweepGC");
		args.add("-XX:+UseParNewGC");

		args.add("-XX:+CMSClassUnloadingEnabled");
		args.add("-XX:+UseCodeCacheFlushing");
		args.add("-XX:-UseSplitVerifier");

		if (Configuration.OS == Configuration.OperatingSystem.MAC) {
			args.add("-Xdock:name=" + Configuration.NAME);

			final File icon = new File(Configuration.TEMP, "ico");
			if (!icon.isFile()) {
				try {
					IOUtils.write(Resources.getResourceURL(Resources.Paths.ICON).openStream(), new FileOutputStream(icon));
				} catch (final IOException ignored) {
				}
			}

			args.add("-Xdock:icon=" + icon.getAbsolutePath());
		}

		args.add("-classpath");
		final String location = Boot.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		args.add(StringUtils.urlDecode(location).replaceAll("\\\\", "/"));
		args.add(Boot.class.getCanonicalName());
		args.add(SWITCH_RESTARTED);

		final ProcessBuilder pb = new ProcessBuilder(args);

		if (Configuration.OS == Configuration.OperatingSystem.MAC) {
			final File java_home = new File("/usr/libexec/java_home");
			if (java_home.canExecute()) {
				try {
					final Process p = Runtime.getRuntime().exec(new String[]{java_home.getPath(), "-v", "1.6"});
					final BufferedReader stdin = new BufferedReader(new InputStreamReader(p.getInputStream()));
					final String home = stdin.readLine();
					if (home != null && !home.isEmpty() && new File(home).isDirectory()) {
						pb.environment().put("JAVA_HOME", home);
					}
					stdin.close();
				} catch (final IOException ignored) {
				}
			}
		}

		try {
			pb.start();
		} catch (final Exception ignored) {
			ignored.printStackTrace();
		}
	}
}
