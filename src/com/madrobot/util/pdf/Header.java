package com.madrobot.util.pdf;

class Header extends Base {

	private String mVersion;
	private String mRenderedHeader;

	Header() {
		clear();
	}

	void setVersion(int Major, int Minor) {
		mVersion = Integer.toString(Major) + "." + Integer.toString(Minor);
		render();
	}

	int getPDFStringSize() {
		return mRenderedHeader.length();
	}

	private void render() {
		mRenderedHeader = "%PDF-" + mVersion + "\n\n";
	}

	@Override
	String toPDFString() {
		return mRenderedHeader;
	}

	@Override
	void clear() {
		setVersion(1, 4);
	}

}
