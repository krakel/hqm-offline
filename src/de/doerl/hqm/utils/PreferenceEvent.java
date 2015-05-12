package de.doerl.hqm.utils;

import javax.swing.event.ChangeEvent;

public class PreferenceEvent extends ChangeEvent {
	private static final long serialVersionUID = 4830369823034680333L;
	private String mKey;

	public PreferenceEvent( Object source, String key) {
		super( source);
		mKey = key;
	}

	public String getKey() {
		return mKey;
	}

	public boolean isKey( String key) {
		return Utils.equals( mKey, key);
	}
}
