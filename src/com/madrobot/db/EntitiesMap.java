package com.madrobot.db;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 
 */
class EntitiesMap {
	private Map<String, WeakReference<DatabaseClient>> map = new HashMap<String, WeakReference<DatabaseClient>>();
	WeakHashMap<DatabaseClient, String> _map = new WeakHashMap<DatabaseClient, String>(); 

	@SuppressWarnings("unchecked")
	<T extends DatabaseClient> T get(Class<T> c, long id) {
		String key = makeKey(c, id);
		WeakReference<DatabaseClient> i = map.get(key);
		if (i == null)
			return null;
		return (T) i.get();
	}

	void set(DatabaseClient e) {
		String key = makeKey(e.getClass(), e.getID());
		map.put(key, new WeakReference<DatabaseClient>(e));
	}
	
	@SuppressWarnings("unchecked")
	private String makeKey(Class entityType, long id) {
		StringBuilder sb = new StringBuilder();
		sb	.append(entityType.getName())
			.append(id);
		return sb.toString();
	}
}
