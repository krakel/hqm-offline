package de.doerl.hqm.medium;

import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Hashtable;

import de.doerl.hqm.base.FHqm;

public class WeakInfo extends WeakReference<FHqm> {
	private Hashtable<String, Object> mProperties = new Hashtable<String, Object>();
	private File mSrc;

	WeakInfo( File name, FHqm id, ReferenceQueue<FHqm> q) {
		super( id, q);
		mSrc = name;
	}

	@Override
	public boolean equals( Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		try {
			WeakInfo other = (WeakInfo) obj;
			if (!mSrc.equals( other.mSrc)) {
				return false;
			}
		}
		catch (ClassCastException ex) {
			return false;
		}
		return true;
	}

	public Object get( String key) {
		return mProperties.get( key);
	}

	public File getSource() {
		return mSrc;
	}

	@Override
	public int hashCode() {
		return mSrc.hashCode();
	}

	public void set( String key, Object value) {
		if (value == null) {
			mProperties.remove( key);
		}
		else {
			mProperties.put( key, value);
		}
	}
}
