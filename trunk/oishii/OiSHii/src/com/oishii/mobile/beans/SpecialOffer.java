package com.oishii.mobile.beans;

import java.io.UnsupportedEncodingException;

public class SpecialOffer {
	int id;
	String offerName;
	String shortDesc;
	int color;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		offerName.replaceAll("null", "\u00a3");
		this.offerName = offerName;
		System.out.println("OFfer name->"+offerName);
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		offerName.replaceAll("null", "\u00a3");
		try {
			this.shortDesc =new String(shortDesc.getBytes(),"US-ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

}
