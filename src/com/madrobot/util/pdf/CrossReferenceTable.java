
package com.madrobot.util.pdf;

 class CrossReferenceTable extends List {

	private int mObjectNumberStart;
	
	 CrossReferenceTable() {
		super();
		clear();
	}
	
	 void setObjectNumberStart(int Value) {
		mObjectNumberStart = Value;
	}
	
	 int getObjectNumberStart() {
		return mObjectNumberStart;
	}
	
	private String getObjectsXRefInfo() {
		return renderList();
	}
	
	 void addObjectXRefInfo(int ByteOffset, int Generation, boolean InUse) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%010d", ByteOffset));
		sb.append(" ");
		sb.append(String.format("%05d", Generation));
		if (InUse) {
			sb.append(" n");
		} else {
			sb.append(" f");
		}
		sb.append("\n");
		mList.add(sb.toString());
	}

	private String render() {
		StringBuilder sb = new StringBuilder();
		sb.append("xref");
		sb.append("\n");
		sb.append(mObjectNumberStart);
		sb.append(" ");
		sb.append(mList.size());
		sb.append("\n");
		sb.append(getObjectsXRefInfo());
		return sb.toString(); 
	}	
	
	@Override
	public String toPDFString() {
		return render();
	}

	@Override
	public void clear() {
		super.clear();
		addObjectXRefInfo(0, 65536, false); // free objects linked list head
		mObjectNumberStart = 0;
	}

}
