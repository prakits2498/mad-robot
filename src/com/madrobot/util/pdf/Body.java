
package com.madrobot.util.pdf;

import java.util.ArrayList;

 class Body extends List {

	private int mByteOffsetStart;
	private int mObjectNumberStart;
	private int mGeneratedObjectsCount;
	private ArrayList<IndirectObject> mObjectsList;
	
	 Body() {
		super();
		clear();
	}
	
	 int getObjectNumberStart() {
		return mObjectNumberStart;
	}
	
	 void setObjectNumberStart(int Value) {
		mObjectNumberStart = Value;
	}
	
	 int getByteOffsetStart() {
		return mByteOffsetStart;
	}
	
	 void setByteOffsetStart(int Value) {
		mByteOffsetStart = Value;
	}
	
	 int getObjectsCount() {
		return mObjectsList.size();
	}

	 int getNextAvailableObjectNumber() {
		return ++mGeneratedObjectsCount + mObjectNumberStart;
	}
	
	 IndirectObject getNewIndirectObject() {
		return getNewIndirectObject(getNextAvailableObjectNumber(), 0, true);
	}
	
	 IndirectObject getNewIndirectObject(int Number, int Generation, boolean InUse) {
		IndirectObject iobj = new IndirectObject();
		iobj.setNumberID(Number);
		iobj.setGeneration(Generation);
		iobj.setInUse(InUse);
		return iobj;
	}
	
	 void includeIndirectObject(IndirectObject iobj) {		
		mObjectsList.add(iobj);
	}

	 int getObjectByteOffset(int x) {
		return mObjectsList.get(x).getByteOffset();
	}

	 int getObjectGeneration(int x) {
		return mObjectsList.get(x).getGeneration();
	}

	 boolean isInUseObject(int x) {
		return mObjectsList.get(x).getInUse();
	}
	
	private String render() {
		int x = 0;
		int offset = mByteOffsetStart;
		while (x < mObjectsList.size()) {
			IndirectObject iobj = mObjectsList.get(x);
			String s = iobj.toPDFString()+"\n";
			mList.add(s);
			iobj.setByteOffset(offset);
			offset += s.length();
			x++;
		}
		return renderList();
	}
	
	@Override
	public String toPDFString() {		
		return render();
	}

	@Override
	public void clear() {
		super.clear();
		mByteOffsetStart = 0;
		mObjectNumberStart = 0;
		mGeneratedObjectsCount = 0;
		mObjectsList = new ArrayList<IndirectObject>();
	}

}
