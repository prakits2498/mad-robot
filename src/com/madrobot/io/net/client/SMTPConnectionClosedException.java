
package com.madrobot.io.net.client;

import java.io.IOException;

/***
 * SMTPConnectionClosedException is used to indicate the premature or
 * unexpected closing of an SMTP connection resulting from a
 * {@link com.madrobot.io.net.client.SMTPReply#SERVICE_NOT_AVAILABLE
 * SMTPReply.SERVICE_NOT_AVAILABLE } response (SMTP reply code 421) to a
 * failed SMTP command. This exception is derived from IOException and
 * therefore may be caught either as an IOException or specifically as an
 * SMTPConnectionClosedException.
 * <p>
 * <p>
 * 
 * @see SMTP
 * @see SMTPClient
 ***/

public final class SMTPConnectionClosedException extends IOException {

	/**
	 * Provide a brief description of serialVersionUID.
	 * Specify the purpose of this field.
	 *
	 */
	private static final long serialVersionUID = -3552263612970518360L;

	/*** Constructs a SMTPConnectionClosedException with no message ***/
	public SMTPConnectionClosedException() {
		super();
	}

	/***
	 * Constructs a SMTPConnectionClosedException with a specified message.
	 * <p>
	 * 
	 * @param message
	 *            The message explaining the reason for the exception.
	 ***/
	public SMTPConnectionClosedException(String message) {
		super(message);
	}

}
