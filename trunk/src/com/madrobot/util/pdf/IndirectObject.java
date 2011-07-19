package com.madrobot.util.pdf;

class IndirectObject extends Base {

	private EnclosedContent mContent;
	private Dictionary mDictionaryContent;
	private Stream mStreamContent;
	private IndirectIdentifier mID;
	private int mByteOffset;
	private boolean mInUse;

	IndirectObject() {
		clear();
	}

	void setNumberID(int Value) {
		mID.setNumber(Value);
	}

	int getNumberID() {
		return mID.getNumber();
	}

	void setGeneration(int Value) {
		mID.setGeneration(Value);
	}

	int getGeneration() {
		return mID.getGeneration();
	}

	String getIndirectReference() {
		return mID.toPDFString() + " R";
	}

	void setByteOffset(int Value) {
		mByteOffset = Value;
	}

	int getByteOffset() {
		return mByteOffset;
	}

	void setInUse(boolean Value) {
		mInUse = Value;
	}

	boolean getInUse() {
		return mInUse;
	}

	void addContent(String Value) {
		mContent.addContent(Value);
	}

	void setContent(String Value) {
		mContent.setContent(Value);
	}

	String getContent() {
		return mContent.getContent();
	}

	void addDictionaryContent(String Value) {
		mDictionaryContent.addContent(Value);
	}

	void setDictionaryContent(String Value) {
		mDictionaryContent.setContent(Value);
	}

	String getDictionaryContent() {
		return mDictionaryContent.getContent();
	}

	void addStreamContent(String Value) {
		mStreamContent.addContent(Value);
	}

	void setStreamContent(String Value) {
		mStreamContent.setContent(Value);
	}

	public String getStreamContent() {
		return mStreamContent.getContent();
	}

	protected String render() {
		StringBuilder sb = new StringBuilder();
		sb.append(mID.toPDFString());
		sb.append(" ");
		// j-a-s-d: this can be performed in inherited classes DictionaryObject
		// and StreamObject
		if (mDictionaryContent.hasContent()) {
			mContent.setContent(mDictionaryContent.toPDFString());
			if (mStreamContent.hasContent())
				mContent.addContent(mStreamContent.toPDFString());
		}
		sb.append(mContent.toPDFString());
		return sb.toString();
	}

	@Override
	public void clear() {
		mID = new IndirectIdentifier();
		mByteOffset = 0;
		mInUse = false;
		mContent = new EnclosedContent();
		mContent.setBeginKeyword("obj", false, true);
		mContent.setEndKeyword("endobj", false, true);
		mDictionaryContent = new Dictionary();
		mStreamContent = new Stream();
	}

	@Override
	public String toPDFString() {
		return render();
	}

}
