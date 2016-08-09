package de.doerl.hqm.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import javax.swing.event.EventListenerList;

public class WeakActionListener extends WeakReference<ActionListener> implements ActionListener {
	private EventListenerList mListener;

	public WeakActionListener( EventListenerList l, ActionListener a) {
		super( a);
		mListener = l;
	}

	public WeakActionListener( EventListenerList l, ActionListener a, ReferenceQueue<ActionListener> q) {
		super( a, q);
		mListener = l;
	}

	public void actionPerformed( ActionEvent event) {
		Object obj = get();
		if (obj != null) {
			((ActionListener) obj).actionPerformed( event);
		}
		else {
			mListener.remove( ActionListener.class, this);
		}
	}
}
