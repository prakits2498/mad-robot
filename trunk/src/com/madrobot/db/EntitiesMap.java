package com.madrobot.db;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 
 */
class EntitiesMap {
	private Map<String, WeakReference<RecordBase>> map = new HashMap<String, WeakReference<RecordBase>>();
	WeakHashMap<RecordBase, String> _map = new WeakHashMap<RecordBase, String>(); 

	@SuppressWarnings("unchecked")
	<T extends RecordBase> T get(Class<T> c, long id) {
		String key = makeKey(c, id);
		WeakReference<RecordBase> i = map.get(key);
		if (i == null)
			return null;
		return (T) i.get();
	}

	void set(RecordBase e) {
		String key = makeKey(e.getClass(), e.getID());
		map.put(key, new WeakReference<RecordBase>(e));
	}
	
	@SuppressWarnings("unchecked")
	private String makeKey(Class entityType, long id) {
		StringBuilder sb = new StringBuilder();
		sb	.append(entityType.getName())
			.append(id);
		return sb.toString();
	}
}
