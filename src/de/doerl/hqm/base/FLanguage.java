package de.doerl.hqm.base;

public final class FLanguage {
	public final FHqm mParentHQM;
	public String mLocale;

	public FLanguage( FHqm parent, String local) {
		mParentHQM = parent;
		mLocale = local;
	}

	@Override
	public String toString() {
		return mLocale;
	}
}
