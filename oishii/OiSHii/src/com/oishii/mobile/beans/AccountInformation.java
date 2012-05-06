package com.oishii.mobile.beans;

import java.util.List;

public class AccountInformation {
	String title;
	String firstname;
	String lastname;
	String email;
	List<Address> addresses;
	List<SavedCard> savedCards;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	public List<SavedCard> getSavedCards() {
		return savedCards;
	}
	public void setSavedCards(List<SavedCard> savedCards) {
		this.savedCards = savedCards;
	}

}
