package com.oishii.mobile.beans;

import java.util.List;

public class MenuCache {

	public static MenuCache getInstance() {
		if (cache == null) {
			cache = new MenuCache();
		}
		return cache;
	}

	private static MenuCache cache;

	List<MenuData> todayMenu;

	public List<MenuData> getTodayMenu() {
		return todayMenu;
	}

	/**
	 * Get cached today's menu
	 * 
	 * @param todayMenu
	 */
	public void setTodayMenu(List<MenuData> todayMenu) {
		this.todayMenu = todayMenu;
	}

	private MenuCache() {

	}
}
