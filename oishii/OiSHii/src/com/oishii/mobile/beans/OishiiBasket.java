package com.oishii.mobile.beans;

import java.util.ArrayList;
import java.util.List;

public class OishiiBasket {
	List<BasketItem> items = new ArrayList<BasketItem>();

	public void addItem(BasketItem item) {
		items.add(item);
	}

	public void removeItem(int index) {
		items.remove(index);
	}

	public void removeAllItems() {
		items.clear();
	}
}
