
package com.madrobot.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents name of the element to which a field in model may map to. <br/>
 * 
 * @see BeanReader
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD })
public @interface ElementName {
	/**
	 * Name of the element being deserialized
	 */
	String value();
}
