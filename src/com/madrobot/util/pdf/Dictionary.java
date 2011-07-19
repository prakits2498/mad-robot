package com.madrobot.util.pdf;

public class Dictionary extends EnclosedContent {
	
	public Dictionary() {
		super();
		setBeginKeyword("<<",false,true);
		setEndKeyword(">>",false,true);
	}
	
}
