package org.powerbot.script.xenon.wrappers;

import java.awt.Point;

import org.powerbot.script.xenon.Keyboard;
import org.powerbot.script.xenon.Settings;
import org.powerbot.script.xenon.Widgets;
import org.powerbot.script.xenon.widgets.ActionBar;

public class Action extends Interactive {
	private final int slot;
	private final Type type;
	private final int id;

	public Action(final int slot, final Type type, final int id) {
		if (slot < 0 || slot >= ActionBar.NUM_SLOTS || type == null || id <= 0) throw new IllegalArgumentException();
		this.slot = slot;
		this.type = type;
		this.id = id;
	}

	public Type getType() {
		return this.type;
	}

	public int getId() {
		return id;
	}

	public String getBind() {
		final Component c = Widgets.get(ActionBar.WIDGET, ActionBar.COMPONENT_SLOTS_BIND[slot]);
		if (c == null) return "";
		final String str = c.getText();
		return str != null ? str.trim() : "";
	}

	public boolean select() {//TODO if bind is in-capable, click + add a method for clicking
		if (!isValid()) return false;
		final String b = getBind();
		return b != null && Keyboard.send(b);
	}

	public boolean isReady() {
		final Component reload = Widgets.get(ActionBar.WIDGET, ActionBar.COMPONENT_SLOTS_COOLDOWN[slot]);
		final Component action = Widgets.get(ActionBar.WIDGET, ActionBar.COMPONENT_SLOTS_ACTION[slot]);
		return reload != null && action != null &&
				reload.isValid() && !reload.isVisible() &&
				action.isValid() && action.getTextColor() == 0xFFFFFF;
	}

	@Override
	public Point getInteractPoint() {
		final Component c = Widgets.get(ActionBar.WIDGET, ActionBar.COMPONENT_SLOTS[slot]);
		if (c == null) return new Point(-1, -1);
		return c.getInteractPoint();
	}

	@Override
	public Point getNextPoint() {
		final Component c = Widgets.get(ActionBar.WIDGET, ActionBar.COMPONENT_SLOTS[slot]);
		if (c == null) return new Point(-1, -1);
		return c.getNextPoint();
	}

	@Override
	public Point getCenterPoint() {
		final Component c = Widgets.get(ActionBar.WIDGET, ActionBar.COMPONENT_SLOTS[slot]);
		if (c == null) return new Point(-1, -1);
		return c.getCenterPoint();
	}

	@Override
	public boolean contains(final Point point) {
		final Component c = Widgets.get(ActionBar.WIDGET, ActionBar.COMPONENT_SLOTS[slot]);
		return c != null && c.contains(point);
	}

	@Override
	public boolean isValid() {
		return this.id == (this.type == Type.ABILITY ?
				Settings.get(ActionBar.SETTING_ABILITY + this.slot) :
				Settings.get(ActionBar.SETTING_ITEM + this.slot));
	}

	public static enum Type {
		ABILITY, ITEM
	}
}
