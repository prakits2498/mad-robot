
package com.madrobot.util.pdf;

 class EnclosedContent extends Base {

	private String mBegin;
	private String mEnd;
	protected StringBuilder mContent;
	
	 EnclosedContent() {
		clear();
	}
	
	 void setBeginKeyword(String Value, boolean NewLineBefore, boolean NewLineAfter) {
		if (NewLineBefore)
			mBegin = "\n" + Value;
		else
			mBegin = Value;
		if (NewLineAfter)
			mBegin += "\n";
	}

	 void setEndKeyword(String Value, boolean NewLineBefore, boolean NewLineAfter) {		
		if (NewLineBefore)
			mEnd = "\n" + Value;
		else
			mEnd = Value;
		if (NewLineAfter)
			mEnd += "\n";
	}
	
	 boolean hasContent() {
		return mContent.length() > 0;
	}

	 void setContent(String Value) {
		clear();
		mContent.append(Value);
	}
	
	 String getContent() {
		return mContent.toString();
	}

	 void addContent(String Value) {
		mContent.append(Value);
	}
	
	 void addNewLine() {
		mContent.append("\n");
	}

	 void addSpace() {
		mContent.append(" ");
	}
	
	@Override
	public void clear() {
		mContent = new StringBuilder();
	}
	
	@Override
	public String toPDFString() {
		return mBegin + mContent.toString() + mEnd;
	}

}
