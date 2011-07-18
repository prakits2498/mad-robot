package com.madrobot.db;

/**
 */
public class DBException extends Exception {
	private static final long serialVersionUID = -1305233534054765602L;

	public DBException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DBException(String detailMessage) {
		super(detailMessage);
	}

	public DBException(Throwable throwable) {
		super(throwable);
	}

	public DBException() {
		super();
	}
}
