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
