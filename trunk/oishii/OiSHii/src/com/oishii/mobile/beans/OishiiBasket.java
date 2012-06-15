package com.oishii.mobile.beans;

import java.util.ArrayList;
import java.util.List;

import com.madrobot.util.TextUtils;

public class OishiiBasket {
	private List<BasketItem> items = new ArrayList<BasketItem>();
	private float currentTotal;
	private float discountedTotal;
	private String deliveryTime = "12:00";
	private int billingAddressId;
	private int shippingAddressId;
	private boolean isDiscountApplied;
	private float discountAmount;
	private float discount;
	private boolean isCorporate;
	/* save card details for this tx */
	private boolean saveCC;

	public boolean isSaveCC() {
		return saveCC;
	}

	public void setSaveCC(boolean saveCC) {
		this.saveCC = saveCC;
	}

	public boolean isCorporate() {
		return isCorporate;
	}

	public void setCorporate(boolean isCorporate) {
		this.isCorporate = isCorporate;
	}

	/**
	 * Coupon if used
	 */
	private String currentCouponCode = "";

	/**
	 * saved creditcard token if selected.
	 */
	private String savedToken;

	public String getSavedToken() {
		return savedToken;
	}

	public void setSavedToken(String savedToken) {
		this.savedToken = savedToken;
	}

	public String getCurrentCouponCode() {
		return currentCouponCode;
	}

	public void setCurrentCouponCode(String currentCouponCode) {
		this.currentCouponCode = currentCouponCode;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscountAmount(float discountPercentage) {
		this.discountAmount = discountPercentage;
		this.discount = discountAmount;
		applyDiscount();
	}

	/**
	 * Apply the given discount percentage to the basket
	 */
	private void applyDiscount() {
		discountedTotal = currentTotal - discountAmount;
		/* Earlier it was percentage */
		// discount = (currentTotal * discountAmount) / 100;
		// discount=(float) TextUtils.round(discount, 2);
		// discountedTotal = currentTotal - discount;
		discountedTotal = (float) TextUtils.round(discountedTotal, 2);
		isDiscountApplied = true;
	}

	public boolean isDiscountApplied() {
		return isDiscountApplied;
	}

	public void setDiscountApplied(boolean isDiscountApplied) {
		this.isDiscountApplied = isDiscountApplied;
	}

	public float getDiscountedTotal() {
		return discountedTotal;
	}

	public void setDiscountedTotal(float discountedTotal) {
		this.discountedTotal = discountedTotal;
	}

	public int getBillingAddressId() {
		return billingAddressId;
	}

	public void setBillingAddressId(int billingAddressId) {
		this.billingAddressId = billingAddressId;
	}

	public int getShippingAddressId() {
		return shippingAddressId;
	}

	public void setShippingAddressId(int shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public float getCurrentTotal() {
		return currentTotal;
	}

	public List<BasketItem> getBasketItems() {
		return items;
	}

	public void setCurrentTotal(float currentTotal) {
		this.currentTotal = currentTotal;
	}

	private void updateTotal() {
		currentTotal = 0.0f;
		discountAmount = 0.0f;
		BasketItem item;
		for (int i = 0; i < items.size(); i++) {
			item = items.get(i);
			currentTotal += item.price * item.count;
		}
		currentTotal = (float) TextUtils.round(currentTotal, 2);
		if (isDiscountApplied) {
			applyDiscount();
		}
	}

	public void addItem(BasketItem item) {
		boolean foundExisting = false;
		BasketItem basketItem;
		for (int i = 0; i < items.size(); i++) {
			basketItem = items.get(i);
			if(basketItem.isSameItem(item)){
				foundExisting=true;
				basketItem.setCount(basketItem.getCount()+1);
			}
		}
		if (!foundExisting) {
			items.add(item);
		}
		updateTotal();
	}

	public BasketItem getItem(int index) {
		return items.get(index);
	}

	public void removeItem(int index) {
		items.remove(index);
		updateTotal();
	}

	public void removeAllItems() {
		items.clear();
	}
}
