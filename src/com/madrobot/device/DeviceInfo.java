
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
