package com.oishii.mobile.util.tasks;

import java.io.InputStream;

public interface IHttpCallback {
	
	public Object populateBean(InputStream is,int operationId);
	
	public void bindUI(Object t, int operationId);
	public void onFailure(int message,int operationID);
}
