
package com.madrobot.di.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to specify the boolean format in the json string. <br/>
 * See {@link JSONDeserializer} {@link JSONSerializer} for usage
 * 
 * @see {@link JSONDeserializer}, {@link JSONSerializer}
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface BooleanFormat {

	/**
	 * Represent the true format
	 * 
	 * @return true format
	 */
	String trueFormat();

	/**
	 * Represent the false format
	 * 
	 * @return false format , default false
	 */
	String falseFormat() default "false";
}
