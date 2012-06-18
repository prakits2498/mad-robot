package com.oishii.mobile.beans;

public class SavedCard {
	private String token;
	private String type;
	private String number;
	private int cardIndex;

	public int getCardIndex() {
		return cardIndex;
	}

	public void setCardIndex(int cardIndex) {
		this.cardIndex = cardIndex;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
