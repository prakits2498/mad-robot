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
package com.madrobot.device;

/**
 * Contact information bean
 *
 */
public class Contact {
	private String _id;
	private String display_name;
	private int has_phone_number;
	private String last_time_contacted;
	private String custom_ringtone;
	private String lookup;
	private int times_contacted;
	
	public int getTimesContacted() {
		return times_contacted;
	}
	public void setTimesContacted(int timesContacted) {
		times_contacted = timesContacted;
	}
	public String getID() {
		return _id;
	}
	public void setID(String id) {
		_id = id;
	}
	public String getDisplayName() {
		return display_name;
	}
	public void setDisplayName(String displayName) {
		display_name = displayName;
	}
	public int hasPhoneNumber() {
		return has_phone_number;
	}
	public void setHasPhoneNumber(int hasPhoneNumber) {
		has_phone_number = hasPhoneNumber;
	}
	public String getLastTimeContacted() {
		return last_time_contacted;
	}
	public void setLastTimeContacted(String lastTimeContacted) {
		last_time_contacted = lastTimeContacted;
		
	}
	public String getCustomRingtone() {
		return custom_ringtone;
	}
	public void setCustomRingtone(String customRingtone) {
		custom_ringtone = customRingtone;
	}
	public String getLookup() {
		return lookup;
	}
	public void setLookup(String lookup) {
		this.lookup = lookup;
	}
	@Override
	public String toString() {
		return "Contact [_id=" + _id + ", custom_ringtone=" + custom_ringtone + ", display_name="
				+ display_name + ", has_phone_number=" + has_phone_number + ", last_time_contacted="
				+ last_time_contacted + ", lookup=" + lookup + ", times_contacted=" + times_contacted + "]";
	}
	
	
}
