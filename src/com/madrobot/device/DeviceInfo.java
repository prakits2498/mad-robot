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

public final class DeviceInfo {
	DeviceInfo() {
	}

	private String firmwareVersion;
	private String kernelVersion;
	private String manufacturer;
	private String deviceModel;
	private String deviceBrand;

	public String getDeviceBrand() {
		return deviceBrand;
	}

	void setDeviceBrand(String deviceBrand) {
		this.deviceBrand = deviceBrand;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getKernelVersion() {
		return kernelVersion;
	}

	void setKernelVersion(String kernelVersion) {
		this.kernelVersion = kernelVersion;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

}
