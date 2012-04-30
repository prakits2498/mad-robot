package com.oishii.mobile.beans;

import java.util.ArrayList;
import java.util.List;

public class MenuItemCategory {
	private int id;
	private String name;
	private String description;
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<MenuItem> getMenuItems() {
		return menuItems;
	}
	
	public void addMenuItem(MenuItem item){
		menuItems.add(item);
	}
}
