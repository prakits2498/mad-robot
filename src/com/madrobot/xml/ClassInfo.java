
package com.madrobot.xml;

class ClassInfo {
	/**
	 * Class being mapped to
	 */
	private Class<?> type;

	/**
	 * Name of the element to be deserialized
	 */
	private String elementName;

	/**
	 * Creates an intance of {@link ClassInfo} with unitialized element name and
	 * type.
	 */
	ClassInfo() {
	}

	/**
	 * Creates an intance of {@link ClassInfo} with specified element name and
	 * type.
	 * 
	 * @param type
	 *            Class to which the element maps
	 * @param elementName
	 *            Name of the element being mapped
	 */
	ClassInfo(Class<?> type, String elementName) {
		this.type = type;
		this.elementName = elementName;
	}

	/**
	 * Gets the class being mapped.
	 * 
	 * @return Class being mapped
	 */
	Class<?> getType() {
		return type;
	}

	/**
	 * Sets the class being mapped.
	 * 
	 * @param type
	 *            Class being mapped
	 */
	void setType(Class<?> type) {
		this.type = type;
	}

	/**
	 * Gets the element name under consideration.
	 * 
	 * @return Name of the element
	 */
	String getElementName() {
		return elementName;
	}

	/**
	 * Sets the element name under consideration.
	 * 
	 * @return Name of the element
	 */
	void setElementName(String elementName) {
		this.elementName = elementName;
	}
}
