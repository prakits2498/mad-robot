package com.oishii.mobile.beans;

public class CurrentScreen {

	private int currentScreenID;

	public int getCurrentScreenID() {
		return currentScreenID;
	}

	public void setCurrentScreenID(int currentScreenID) {
		this.currentScreenID = currentScreenID;
	}

	private CurrentScreen() {

	}

	private static CurrentScreen instance;

	public synchronized static CurrentScreen getInstance() {
		if (instance == null) {
			instance = new CurrentScreen();
		}
		return instance;
	}

}
