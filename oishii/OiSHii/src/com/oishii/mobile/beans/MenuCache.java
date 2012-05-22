package com.oishii.mobile.beans;

import java.util.List;

public class MenuCache {

	private static MenuCache cache;

	public static MenuCache getInstance() {
		if (cache == null) {
			cache = new MenuCache();
		}
		return cache;
	}

	private List<MenuData> todayMenu;
	private List<SpecialOffer> splOffers;
	
	

	private MenuCache() {

	}

	public List<SpecialOffer> getSplOffers() {
		return splOffers;
	}

	public List<MenuData> getTodayMenu() {
		return todayMenu;
	}

	public void setSplOffers(List<SpecialOffer> splOffers) {
		this.splOffers = splOffers;
	}

	/**
	 * Get cached today's menu
	 * 
	 * @param todayMenu
	 */
	public void setTodayMenu(List<MenuData> todayMenu) {
		this.todayMenu = todayMenu;
	}
}
