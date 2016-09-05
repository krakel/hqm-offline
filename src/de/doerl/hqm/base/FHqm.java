package de.doerl.hqm.base;

import java.util.ArrayList;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.Utils;

public final class FHqm extends ANamed {
//	private static final Logger LOGGER = Logger.getLogger( FHqm.class.getName());
	private FileVersion mVersion;
	public final FQuestSetCat mQuestSetCat = new FQuestSetCat( this);
	public final FReputationCat mReputationCat = new FReputationCat( this);
	public final FGroupTierCat mGroupTierCat = new FGroupTierCat( this);
	public final ArrayList<FLanguage> mLanguages = new ArrayList<>();
	public String mPassCode;
	public FLanguage mMain;
	public String mName;
	private boolean mModified;

	public FHqm( String name) {
		mName = name;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forHQM( this, p);
	}

	public void addLanguage( String text) {
		if (getLanguage( text) == null) {
			createLanguage( text);
		}
	}

	public boolean containsLanguage( String text) {
		return getLanguage( text) != null;
	}

	public FLanguage createLanguage( String text) {
		FLanguage res = new FLanguage( this, text);
		mLanguages.add( res);
		return res;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.HQM;
	}

	@Override
	public FHqm getHqm() {
		return this;
	}

	public FLanguage getLanguage( String lang) {
		for (FLanguage ll : mLanguages) {
			if (Utils.equals( ll.mLocale, lang)) {
				return ll;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public ABase getParent() {
		return null;
	}

	public FileVersion getVersion() {
		return mVersion;
	}

	public boolean isModified() {
		return mModified;
	}

	@Override
	void localeDefault( LocaleInfo info) {
		info.mInfo2 = "No description";
	}

	public void removeLanguage( FLanguage old) {
		if (old != null) {
			mLanguages.remove( old);
		}
		if (mLanguages.size() == 0) {
			createLanguage( PreferenceManager.getString( BaseDefaults.LANGUAGE_MAIN));
		}
		if (Utils.equals( mMain, old.mLocale)) {
			mMain = mLanguages.get( 0);
		}
	}

	public void setMain( String language) {
		FLanguage lang = getLanguage( language);
		if (lang == null) {
			lang = createLanguage( language);
		}
		mMain = lang;
	}

	public void setModified( boolean value) {
		mModified = value;
	}

	@Override
	public void setName( String name) {
		mName = name;
	}

	public void setVersion( FileVersion version) {
		mVersion = version;
	}
}
