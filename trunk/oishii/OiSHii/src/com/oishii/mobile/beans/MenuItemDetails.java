package com.oishii.mobile.beans;

import java.util.ArrayList;
import java.util.List;

public class MenuItemDetails {
	private List<MenuItemCategory> menuCategories = new ArrayList<MenuItemCategory>();

	public List<MenuItemCategory> getMenuCategories() {
		return menuCategories;
	}

	public void addMenuCategories(MenuItemCategory menuCategory) {
		menuCategories.add(menuCategory);
	}

}
