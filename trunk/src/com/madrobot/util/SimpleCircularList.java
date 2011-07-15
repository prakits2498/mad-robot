package com.madrobot.util;


public class SimpleCircularList {
	Object[] list;
	int currentIndex;
	int size;

	public SimpleCircularList(int size) {
		this.size = size;
		this.list = new Object[size];
	}

	public void add(Object o) {
		list[currentIndex] = o;
		currentIndex++;
	}

	private int pollIndex = -1;

	public Object poll() {
		int temp = pollIndex + 1;
		if(temp > (size - 1)){
			pollIndex = 0;
		} else{
			pollIndex++;
		}
		return list[pollIndex];
	}

	public Object getObjectAt(int index) throws ArrayIndexOutOfBoundsException {
		return list[index];
	}
}
