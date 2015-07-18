package de.doerl.hqm.base;

import java.util.HashMap;

public abstract class ANamed extends ABase {
	private HashMap<FLanguage, LocaleInfo> mInfo = new HashMap<>();

	ANamed() {
	}

	public LocaleInfo addLocale( FLanguage lang) {
		LocaleInfo info = mInfo.get( lang);
		if (info == null) {
			info = new LocaleInfo();
			mInfo.put( lang, info);
			localeDefault( info);
		}
		return info;
	}

	public void deleteLocale( FLanguage lang) {
		mInfo.remove( lang);
	}

	public String getDescr() {
		return addLocale( getHqm().mMain).mInfo2;
	}

	public String getDescr( FLanguage lang) {
		return addLocale( lang).mInfo2;
	}

	@Override
	public FHqm getHqm() {
		return getParent().getHqm();
	}

	public String getName() {
		return addLocale( getHqm().mMain).mInfo1;
	}

	public String getName( FLanguage lang) {
		return addLocale( lang).mInfo1;
	}

	abstract void localeDefault( LocaleInfo info);

	public void setDescr( FLanguage lang, String descr) {
		addLocale( lang).mInfo2 = descr;
	}

	public void setDescr( String descr) {
		addLocale( getHqm().mMain).mInfo2 = descr;
	}

	public void setName( FLanguage lang, String name) {
		addLocale( lang).mInfo1 = name;
	}

	public void setName( String name) {
		addLocale( getHqm().mMain).mInfo1 = name;
	}

	protected static class LocaleInfo {
		public String mInfo1 = "unknown";
		public String mInfo2 = "unknown";
	}
}
