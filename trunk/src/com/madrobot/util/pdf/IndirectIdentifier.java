package com.madrobot.util.pdf;

class IndirectIdentifier extends Base {

	private int mNumber;
	private int mGeneration;

	IndirectIdentifier() {
		clear();
	}

	void setNumber(int Number) {
		this.mNumber = Number;
	}

	int getNumber() {
		return mNumber;
	}

	void setGeneration(int Generation) {
		this.mGeneration = Generation;
	}

	int getGeneration() {
		return mGeneration;
	}

	@Override
	void clear() {
		mNumber = 0;
		mGeneration = 0;
	}

	@Override
	String toPDFString() {
		return Integer.toString(mNumber) + " " + Integer.toString(mGeneration);
	}

}
