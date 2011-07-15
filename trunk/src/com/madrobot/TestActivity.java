/*******************************************************************************
 * Copyright (c) 2011 MadRobot.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *  Elton Kent - initial API and implementation
 ******************************************************************************/
package com.madrobot;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.ek.android.R;
import com.madrobot.device.Contact;
import com.madrobot.device.ContactUtils;

public class TestActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//		try{
//			TestReflect bean = new TestReflect();
//			BeanInfo beanInfo = Introspector.getBeanInfo(TestReflect.class);
//			PropertyDescriptor[] desc = beanInfo.getPropertyDescriptors();
//			for(int i = 0; i < desc.length; i++){
//				PropertyDescriptor prop = desc[i];
//				Log.d("Test", "prop name " + prop.getName());
//				// Log.d("Test", "prop display name " + prop.getDisplayName());
//				Method method = prop.getWriteMethod();
//
//				if(method != null){
//					Log.d("Test", "prop write method " + method.getName());
//
//					Class[] types = method.getParameterTypes();
//					if(types != null)
//						for(int ty = 0; ty < types.length; ty++){
//							Log.d("Test", "types FOUND==> " + types[ty]);
//							Log.d("Test", "Type Test==>" + types[ty].equals( byte[].class));
//							Log.w("Test", "types IS ARRAY==> " + types[ty].isArray());
//							if(types[ty].equals( byte[].class)){
//								Log.e("Test", "byte class");
//								method.invoke(bean, new Object[] {new byte[]{32,34} });
//							} else if(types[ty].equals(String.class)){
//								method.invoke(bean, new Object[] { "test" });
//							}
//
//						}
//				}
//				Log.d("Test", "bean->" + bean.toString());
//			}
//		} catch(IntrospectionException e){
//			e.printStackTrace();
//		} catch(IllegalArgumentException e){
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch(IllegalAccessException e){
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch(InvocationTargetException e){
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		List<Contact> contacts=ContactUtils.fetchContacts(getApplicationContext());
		for(int i=0;i<contacts.size();i++){
			Log.d("Test","Contact"+contacts.get(i));
		}
		
	}
}
