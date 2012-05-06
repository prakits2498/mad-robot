package com.oishii.mobile.beans;

public class Address {
	int id;
	String company;
	String floor;
	String address;
	String city;
	String postCode;
	String mobile;
	boolean isShipping;
	boolean isBilling;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public boolean isShipping() {
		return isShipping;
	}

	public void setShipping(boolean isShipping) {
		this.isShipping = isShipping;
	}

	public boolean isBilling() {
		return isBilling;
	}

	public void setBilling(boolean isBilling) {
		this.isBilling = isBilling;
	}
}
