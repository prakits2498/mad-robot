
package com.madrobot.beans;

import java.beans.PropertyChangeEvent;

/*
 * A PropertyVetoException is thrown when a proposed change to a
 * property represents an unacceptable value.
 */

public class PropertyVetoException extends Exception {

	/**
	 * Provide a brief description of serialVersionUID.
	 * Specify the purpose of this field.
	 *
	 */
	private static final long serialVersionUID = -2206020012556077235L;

	/**
	 * Constructs a <code>PropertyVetoException</code> with a
	 * detailed message.
	 * 
	 * @param mess
	 *            Descriptive message
	 * @param evt
	 *            A PropertyChangeEvent describing the vetoed change.
	 */
	public PropertyVetoException(String mess, PropertyChangeEvent evt) {
		super(mess);
		this.evt = evt;
	}

	/**
	 * Gets the vetoed <code>PropertyChangeEvent</code>.
	 * 
	 * @return A PropertyChangeEvent describing the vetoed change.
	 */
	public PropertyChangeEvent getPropertyChangeEvent() {
		return evt;
	}

	/**
	 * A PropertyChangeEvent describing the vetoed change.
	 * 
	 * @serial
	 */
	private PropertyChangeEvent evt;
}
