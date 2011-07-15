package com.madrobot.io.net.client.util;

import java.io.Serializable;
import java.util.EventListener;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 */

public class ListenerList implements Serializable, Iterable<EventListener>
{
    /**
	 * Provide a brief description of serialVersionUID.
	 * Specify the purpose of this field.
	 *
	 */
	private static final long serialVersionUID = -6684541015292922675L;
	private final CopyOnWriteArrayList<EventListener> __listeners;

    public ListenerList()
    {
        __listeners = new CopyOnWriteArrayList<EventListener>();
    }

    public void addListener(EventListener listener)
    {
            __listeners.add(listener);
    }

    public  void removeListener(EventListener listener)
    {
            __listeners.remove(listener);
    }

    public int getListenerCount()
    {
        return __listeners.size();
    }
    
    /**
     * Return an {@link Iterator} for the {@link EventListener} instances
     * 
     * @since 2.0
     * TODO Check that this is a good defensive strategy
     */
    public Iterator<EventListener> iterator() {
            return __listeners.iterator();
    }

}
