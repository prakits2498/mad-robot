
package com.madrobot.di.json.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.madrobot.di.json.JSONDeserializer;
import com.madrobot.di.json.JSONSerializer;



/**
 * Annotation to specify the item type when the field is a collection. <br/>
 * See {@link JSONDeserializer} {@link JSONSerializer} for usage
 * 
 * @see {@link JSONDeserializer}, {@link JSONSerializer}
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ItemType {

	/**
	 * Represent the item type
	 * 
	 * @return item type
	 */
	Class<?> value();

	/**
	 * 
	 * Represent the size of the collection
	 * 
	 * @return size of the collection
	 */
	int size() default JSONDeserializer.DEFAULT_ITEM_COLLECTION_SIZE;

	/**
	 * Decision to json should hold empty array or not
	 * 
	 * @see JSONSerializer
	 * 
	 * @return decision
	 */
	boolean canEmpty() default true;
}
