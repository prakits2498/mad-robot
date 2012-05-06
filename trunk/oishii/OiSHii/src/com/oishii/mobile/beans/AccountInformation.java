package com.oishii.mobile.beans;

import java.util.ArrayList;
import java.util.List;

public class AccountInformation {
	private String title;
	private String firstname;
	private String lastname;
	private String email;
	private int subscribed;
	public int getSubscribed() {
		return subscribed;
	}

	public void setSubscribed(int subscribed) {
		this.subscribed = subscribed;
	}

	private List<Address> addresses=new ArrayList<Address>();
	private List<SavedCard> savedCards=new ArrayList<SavedCard>();

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
