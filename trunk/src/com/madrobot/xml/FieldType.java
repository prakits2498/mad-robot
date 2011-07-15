
package com.madrobot.xml;

/**
 * Possible field types, during deserialization
 * See {@link BeanReader} for more details on its usage
 * 
 * @see BeanReader
 */
enum FieldType {
	/**
	 * Used to indicate that the type of the field is not defined
	 */
	NOT_DEFINED,
	/**
	 * Used to indicate that the type of the field is pseudo-primitive
	 */
	PSEUDO_PRIMITIVE,
	/**
	 * Used to indicate that the type of the field is composite
	 */
	COMPOSITE,
	/**
	 * Used to indicate that the type of the field is a collection
	 */
	COLLECTION
}
