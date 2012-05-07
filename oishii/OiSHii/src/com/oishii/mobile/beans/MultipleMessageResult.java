package com.oishii.mobile.beans;

public class MultipleMessageResult {

	StringBuilder errors;
	boolean success;

	public StringBuilder getErrors() {
		return errors;
	}

	public void setErrors(StringBuilder errors) {
		this.errors = errors;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
