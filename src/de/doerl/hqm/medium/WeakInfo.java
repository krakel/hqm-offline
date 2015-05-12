package de.doerl.hqm.medium;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.Hashtable;

import de.doerl.hqm.base.FHqm;

public class WeakInfo extends WeakReference<FHqm> {
	private Hashtable<String, Object> mProperties = new Hashtable<String, Object>();
	private URI mURI;

	WeakInfo( URI name, FHqm id, ReferenceQueue<FHqm> q) {
		super( id, q);
		mURI = name;
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
			if (!mURI.equals( other.mURI)) {
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

	public URI getURI() {
		return mURI;
	}

	@Override
	public int hashCode() {
		return mURI.hashCode();
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
