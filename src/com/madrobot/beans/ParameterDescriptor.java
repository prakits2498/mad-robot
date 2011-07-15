
package com.madrobot.beans;

public class ParameterDescriptor extends FeatureDescriptor {

	/**
	 * Public default constructor.
	 */
	public ParameterDescriptor() {
	}

	/**
	 * Package private dup constructor.
	 * This must isolate the new object from any changes to the old object.
	 */
	ParameterDescriptor(ParameterDescriptor old) {
		super(old);
	}

}
