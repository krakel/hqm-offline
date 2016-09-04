package de.doerl.hqm.base;

import java.util.UUID;

public abstract class AUUID extends AIdent {
	private String mUUID = UUID.randomUUID().toString();

	public AUUID( String base, int id) {
		super( base, id);
	}

	public String getUUID() {
		return mUUID;
	}

	public void setUUID( String uuid) {
		if (uuid != null) {
			mUUID = uuid;
		}
	}
}
