package org.powerbot.bot.rt4;


import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;

import java.util.ArrayList;
import java.util.List;

public class Killswitch extends PollingScript<ClientContext> {

	PollingScript ps = null;
	final Client c;
	final List<Break> breaks = new ArrayList<>();

	public Killswitch() {
		priority.set(6);
		ps = (PollingScript) ctx.controller.script();
		c = ctx.client();

		final String breakString = (String) ctx.properties.get("killswitch");
		for (final String s : breakString.split(";")) {
			final String[] b = s.split(":");
			if (b.length == 3) {
				breaks.add(new Break(Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2])));
			}
		}
		if (breaks.size() > 0) {
			log.info("this script will use the configured breaking schedule");
		}
	}

	private boolean isValid() {
		final long runTime = getTotalRuntime();

		if (breaks.size() > 0) {
			final Break b = breaks.get(0);
			if (b.getBreakTime() * 60 * 1000 <= runTime) {
				return c != null;
			}
		}

		return false;

	}


	@Override
	public void poll() {
		if (!isValid()) {
			threshold.remove(this);
			return;
		}
		threshold.add(this);

		if (ps != null && ps.canBreak()) {
			final Break b = breaks.get(0);
			breaks.remove(b);
			log.info(b.toString());
			ctx.properties.setProperty("login.disable", "true");
			if (b.getLogoutType() == 1 || b.getLogoutType() == 2) {

				Condition.wait(() -> !ctx.game.loggedIn(), 500, 4);
			}
			if (Random.nextBoolean()) {
				ctx.input.defocus();
			}

			Condition.sleep(b.getLength() * 60 * 1000);

			ctx.properties.setProperty("login.disable", "false");
			if (b.getLength() == 0) {
				ctx.controller.stop();
			} else {
				ctx.input.focus();
			}
		}
	}
}