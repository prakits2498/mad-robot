
package com.madrobot.di.json.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.madrobot.di.json.JSONDeserializer;
import com.madrobot.di.json.JSONSerializer;

/**
 * Annotation to specify the date format in the json string. <br/>
 * See {@link JSONDeserializer} {@link JSONSerializer} for usage
 * 
 * @see {@link JSONDeserializer}, {@link JSONSerializer}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface DateFormat {

	/**
	 * Represent the date format
	 * 
	 * @return date format
	 */
	String format();
}
