package org.powerbot.game.api.wrappers;

public interface Verifiable {
	/**
	 * Verify that this entity exists.
	 *
	 * @return <tt>true</tt> if this entity is still valid; otherwise <tt>false</tt>.
	 */
	public boolean validate();
}
