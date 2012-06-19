package com.oishii.mobile.beans;

import java.util.ArrayList;

public class SideOrderContainer {

	private ArrayList<MenuItem> snacksList;
	private ArrayList<MenuItem> drinksList;
	

	public ArrayList<MenuItem> getSnacksList() {
		return snacksList;
	}

	public void setSnacksList(ArrayList<MenuItem> snacksList) {
		this.snacksList = snacksList;
	}

	public ArrayList<MenuItem> getDrinksList() {
		return drinksList;
	}

	public void setDrinksList(ArrayList<MenuItem> drinksList) {
		this.drinksList = drinksList;
	}

	private static SideOrderContainer _instance;

	public synchronized static SideOrderContainer getInstance() {
		if (_instance == null) {
			_instance = new SideOrderContainer();
		}
		return _instance;
	}
	
	private SideOrderContainer(){
		
	}
}

