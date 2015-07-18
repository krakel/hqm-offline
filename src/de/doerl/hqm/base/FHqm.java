package de.doerl.hqm.base;

import java.util.Vector;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.FileVersion;

public final class FHqm extends ANamed {
//	private static final Logger LOGGER = Logger.getLogger( FHqm.class.getName());
	public static final String LANG_EN_US = "enUS";
	public static final String LANG_DE_DE = "deDE";
	private FileVersion mVersion;
	public final FQuestSetCat mQuestSetCat = new FQuestSetCat( this);
	public final FReputationCat mReputationCat = new FReputationCat( this);
	public final FGroupTierCat mGroupTierCat = new FGroupTierCat( this);
	public final Vector<String> mLanguages = new Vector<>();
	public String mPassCode;
	public String mLang = LANG_EN_US;
	public String mName;
	private boolean mModified;

	public FHqm( String name) {
		mName = name;
		setDescr( "Hallo D:");
		mLanguages.add( mLang);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forHQM( this, p);
	}

	public String getDescr() {
		return getInfo().mInfo2;
	}

	public String getDescr( String lang) {
		return getInfo( lang).mInfo2;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.HQM;
	}

	@Override
	public FHqm getHqm() {
		return this;
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

	public void setDescr( String descr) {
		getInfo().mInfo2 = descr;
	}

	public void setDescr( String lang, String descr) {
		getInfo( lang).mInfo2 = descr;
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
