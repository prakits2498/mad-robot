package com.madrobot.util.pdf;

 class Trailer extends Base {

	private int mXRefByteOffset;
	private int mObjectsCount;
	private Dictionary mTrailerDictionary;
	
	 Trailer() {
		clear();
	}
	
	 void setCrossReferenceTableByteOffset(int Value)
	{
		mXRefByteOffset = Value;
	}

	 void setObjectsCount(int Value)
	{
		mObjectsCount = Value;
	}
	
	private void renderDictionary() {
		mTrailerDictionary.setContent("  /Size "+Integer.toString(mObjectsCount));
		mTrailerDictionary.addNewLine();
		mTrailerDictionary.addContent("  /Root 1 0 R");
		mTrailerDictionary.addNewLine();
	}

	private String render() {
		renderDictionary();
		StringBuilder sb = new StringBuilder();
		sb.append("trailer");
		sb.append("\n");
		sb.append(mTrailerDictionary.toPDFString());
		sb.append("startxref");
		sb.append("\n");
		sb.append(mXRefByteOffset);
		sb.append("\n");
		sb.append("%%EOF");
		sb.append("\n");		
		return sb.toString();
	}
	
	@Override
	 String toPDFString() {		
		return render();
	}

	@Override
	 void clear() {
		mXRefByteOffset = 0;
		mTrailerDictionary = new Dictionary();
	}

}
