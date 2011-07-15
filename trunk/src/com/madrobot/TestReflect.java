/*******************************************************************************
 * Copyright (c) 2011 MadRobot.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *  Elton Kent - initial API and implementation
 ******************************************************************************/
package com.madrobot;

public class TestReflect {
	private  byte[] ift;
	private  String text;
	public   byte[] getIft() {
		return ift;
	}
	public  void setIft( byte[] ift) {
		this.ift = ift;
	}
	public  String getTextMain() {
		return text;
	}
	public void setTextMain(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "TestReflect [ift=" + ift + ", text=" + text + "]";
	}
	
//	public static int getInt(int i) {
//		return i + 1;
//	}
	
	
}
