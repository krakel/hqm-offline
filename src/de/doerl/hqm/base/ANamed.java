package de.doerl.hqm.base;

import java.util.HashMap;

public abstract class ANamed extends ABase {
	private HashMap<String, LangInfo> mInfo = new HashMap<>();

	ANamed() {
	}

	@Override
	public FHqm getHqm() {
		return getParent().getHqm();
	}

	protected LangInfo getInfo() {
		return getInfo( getHqm().mLang);
	}

	protected LangInfo getInfo( String lang) {
		LangInfo info = mInfo.get( lang);
		if (info == null) {
			info = new LangInfo();
			mInfo.put( lang, info);
		}
		return info;
	}

	public abstract String getName();

	public abstract void setName( String name);

	protected static class LangInfo {
		public String mInfo1;
		public String mInfo2;
	}
}
