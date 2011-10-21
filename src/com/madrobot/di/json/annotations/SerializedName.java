
package com.madrobot.di.json.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.madrobot.di.json.JSONDeserializer;
import com.madrobot.di.json.JSONSerializer;

/**
 * Annotation to specify the serialized name of the json key. <br/>
 * See {@link JSONDeserializer} {@link JSONSerializer} for usage
 * 
 * @see {@link JSONDeserializer}, {@link JSONSerializer}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface SerializedName {

	/**
	 * Represent the json key name
	 * 
	 * @return json key name
	 */
	String value();
}
