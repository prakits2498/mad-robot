package com.oishii.mobile.beans;

import java.util.List;

public class HistoryContainer {

	private List<OrderCategory> orderCategories;
	private List<List<Order>> orderDetails;

	public List<OrderCategory> getOrderCategories() {
		return orderCategories;
	}

	public void setOrderCategories(List<OrderCategory> orderCategories) {
		this.orderCategories = orderCategories;
	}

	public List<List<Order>> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<List<Order>> orderDetails) {
		this.orderDetails = orderDetails;
	}

}
