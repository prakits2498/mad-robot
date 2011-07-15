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
 * Contains network related information.
 * <p>
 * 
 * {@link DeviceUtils#getNetworkInfo(android.content.Context)}
 * </p>
 */
public final class NetworkInfo {

	NetworkInfo() {
	}

	private boolean isRoaming;
	private String operatorName;
	private int networkType;
	private int dataState;
	private String voicemailNumber;
	private int phoneType;

	public int getPhoneType() {
		return phoneType;
	}

	/**
	 * Get the phone type
	 * 
	 * 
	 * @return TelephonyManager.PHONETYPE_XXX , 0 if phone type cannot be
	 *         established.
	 */
	void setPhoneType(int phoneType) {
		this.phoneType = phoneType;
	}

	/**
	 * 
	 * 
	 * @return True if the network is in roaming state
	 */
	public boolean isRoaming() {
		return isRoaming;
	}

	void setRoaming(boolean isRoaming) {
		this.isRoaming = isRoaming;
	}

	/**
	 * Returns the operator name
	 * 
	 * @return
	 */
	public String getOperatorName() {
		return operatorName;
	}

	void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * Get the network type currently in use
	 * 
	 * @return TelephonyManager.NETWORKTYPE_XXX
	 */
	public int getNetworkType() {
		return networkType;
	}

	void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

	/**
	 * 
	 * 
	 * @return TelephonyManager.DATA_XXX
	 */
	public int getDataState() {
		return dataState;
	}

	void setDataState(int dataState) {
		this.dataState = dataState;
	}

	/**
	 * Returns the voice mail number
	 * 
	 * @return
	 */
	public String getVoicemailNumber() {
		return voicemailNumber;
	}

	void setVoicemailNumber(String voicemailNumber) {
		this.voicemailNumber = voicemailNumber;
	}

}
