package com.oishii.mobile.util.tasks;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;

import com.madrobot.util.HttpSettings;

public class HttpRequestWrapper {
	public  HttpRequestWrapper(Context ctx){
		this.ctx=ctx;
	}
	public boolean canCache=true;
	public int intExtra;
	public Context ctx;
	public URI requestURI;
	/*integer to identify the operation. an activity may user*/
	public int operationID;
	public IHttpCallback callback;
	public HttpSettings httpSettings=new HttpSettings();
	public List<NameValuePair> httpParams=new ArrayList<NameValuePair>();
}
