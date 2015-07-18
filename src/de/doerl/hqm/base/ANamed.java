package de.doerl.hqm.base;

import java.util.HashMap;

public abstract class ANamed extends ABase {
	private HashMap<String, LangInfo> mInfo = new HashMap<>();

	ANamed() {
	}

	public LangInfo addLang( String lang) {
		LangInfo info = new LangInfo();
		mInfo.put( lang, info);
		return info;
	}

	public void deleteLang( String lang) {
		mInfo.remove( lang);
	}

	public String getDescr() {
		return getInfo().mInfo2;
	}

	public String getDescr( String lang) {
		return getInfo( lang).mInfo2;
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
			info = addLang( lang);
		}
		return info;
	}

	public String getName() {
		return getInfo().mInfo1;
	}

	public String getName( String lang) {
		return getInfo( lang).mInfo1;
	}

	public void setDescr( String descr) {
		getInfo().mInfo2 = descr;
	}

	public void setDescr( String lang, String descr) {
		getInfo( lang).mInfo2 = descr;
	}

	public void setName( String name) {
		getInfo().mInfo1 = name;
	}

	public void setName( String lang, String name) {
		getInfo( lang).mInfo1 = name;
	}

	private static class LangInfo {
		public String mInfo1 = "unknown";
		public String mInfo2 = "unknown";
	}
}
