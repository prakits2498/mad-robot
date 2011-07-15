
package com.madrobot;

public class TestReflect {
	private  byte[] ift;
	private  String text;
	public   byte[] getIft() {
		return ift;
	}
	public  void setIft( byte[] ift) {
		this.ift = ift;
	}
	public  String getTextMain() {
		return text;
	}
	public void setTextMain(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "TestReflect [ift=" + ift + ", text=" + text + "]";
	}
	
//	public static int getInt(int i) {
//		return i + 1;
//	}
	
	
}
