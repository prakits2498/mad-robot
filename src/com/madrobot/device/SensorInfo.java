
package com.madrobot.device;

/**
 * Contains sensor related information
 *<p>
 *{@link DeviceUtils#getSensorInfo(android.content.Context)}
 *</p>
 */
public class SensorInfo {
	private boolean hasAccelerometer;
	private boolean hasProximitySensor;
	private boolean hasGyroscope;
	private boolean hasLightSensor;
	private boolean hasOrientationSensor;
	private boolean hasPressureSensor;
	private boolean hasTemperatureSensor;
	private boolean hasMagneticSensor;

	public boolean hasAccelerometer() {
		return hasAccelerometer;
	}

	void setHasAccelerometer(boolean hasAccelerometer) {
		this.hasAccelerometer = hasAccelerometer;
	}

	public boolean hasProximitySensor() {
		return hasProximitySensor;
	}

	void setHasProximitySensor(boolean hasProximitySensor) {
		this.hasProximitySensor = hasProximitySensor;
	}

	public boolean hasGyroscope() {
		return hasGyroscope;
	}

	void setHasGyroscope(boolean hasGyroscope) {
		this.hasGyroscope = hasGyroscope;
	}

	public boolean hasLightSensor() {
		return hasLightSensor;
	}

	void setHasLightSensor(boolean hasLightSensor) {
		this.hasLightSensor = hasLightSensor;
	}

	public boolean hasOrientationSensor() {
		return hasOrientationSensor;
	}

	void setHasOrientationSensor(boolean hasOrientationSensor) {
		this.hasOrientationSensor = hasOrientationSensor;
	}

	public boolean hasPressureSensor() {
		return hasPressureSensor;
	}

	void setHasPressureSensor(boolean hasPressureSensor) {
		this.hasPressureSensor = hasPressureSensor;
	}

	public boolean hasTemperatureSensor() {
		return hasTemperatureSensor;
	}

	void setHasTemperatureSensor(boolean hasTemperatureSensor) {
		this.hasTemperatureSensor = hasTemperatureSensor;
	}

	public boolean hasMagneticSensor() {
		return hasMagneticSensor;
	}

	void setHasMagneticSensor(boolean hasMagneticSensor) {
		this.hasMagneticSensor = hasMagneticSensor;
	}

}
