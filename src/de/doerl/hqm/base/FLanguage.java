package de.doerl.hqm.base;

import de.doerl.hqm.utils.ToString;

public final class FLanguage {
	public final FHqm mParentHQM;
	public String mLocale;

	public FLanguage( FHqm parent, String local) {
		mParentHQM = parent;
		mLocale = local;
	}

	@Override
	public String toString() {
		ToString sb = new ToString( this);
		sb.appendMsg( "locale", mLocale);
		return sb.toString();
	}
}
