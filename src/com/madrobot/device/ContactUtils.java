
package com.madrobot.device;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Contacts;
import android.provider.ContactsContract;

import com.madrobot.beans.IntrospectionException;
import com.madrobot.db.DBUtils;

/**
 * Utility for accessing phone contacts
 * 
 */
public final class ContactUtils {

	public static List<Contact> fetchContacts(Context context) {
		ContentResolver cr = context.getContentResolver();
		Cursor namesCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		try{
			return DBUtils.toBeanList(namesCursor, Contact.class);
		} catch(IllegalArgumentException e){
			e.printStackTrace();
		} catch(IllegalAccessException e){
			e.printStackTrace();
		} catch(InstantiationException e){
			e.printStackTrace();
		} catch(IntrospectionException e){
			e.printStackTrace();
		} catch(InvocationTargetException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Save a number to the contacts
	 * 
	 * @param context
	 *            Application context
	 * @param number
	 *            Phone number to save
	 */
	public static void saveToContact(Context context, String number) {
		Intent intent = new Intent(Contacts.Intents.Insert.ACTION, Contacts.People.CONTENT_URI);
		intent.putExtra(Contacts.Intents.Insert.PHONE, number);
		context.startActivity(intent);
	}
}
