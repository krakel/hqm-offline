package de.doerl.hqm.base;

import java.util.HashMap;

public abstract class ANamed extends ABase {
	private HashMap<FLanguage, LangInfo> mInfo = new HashMap<>();

	ANamed() {
	}

	public LangInfo addLang( FLanguage lang) {
		LangInfo info = mInfo.get( lang);
		if (info == null) {
			info = new LangInfo();
			mInfo.put( lang, info);
		}
		return info;
	}

	public void deleteLang( FLanguage lang) {
		mInfo.remove( lang);
	}

	public String getDescr() {
		return addLang( getHqm().mMain).mInfo2;
	}

	public String getDescr( FLanguage lang) {
		return addLang( lang).mInfo2;
	}

	@Override
	public FHqm getHqm() {
		return getParent().getHqm();
	}

	public String getName() {
		return addLang( getHqm().mMain).mInfo1;
	}

	public String getName( FLanguage lang) {
		return addLang( lang).mInfo1;
	}

	public void setDescr( FLanguage lang, String descr) {
		addLang( lang).mInfo2 = descr;
	}

	public void setDescr( String descr) {
		addLang( getHqm().mMain).mInfo2 = descr;
	}

	public void setName( FLanguage lang, String name) {
		addLang( lang).mInfo1 = name;
	}

	public void setName( String name) {
		addLang( getHqm().mMain).mInfo1 = name;
	}

	private static class LangInfo {
		public String mInfo1 = "unknown";
		public String mInfo2 = "unknown";
	}
}
