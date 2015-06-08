package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.FileVersion;

public final class FHqm extends ANamed {
//	private static final Logger LOGGER = Logger.getLogger( FHqm.class.getName());
	private FileVersion mVersion;
	public final FQuestSetCat mQuestSetCat = new FQuestSetCat( this);
	public final FReputationCat mReputationCat = new FReputationCat( this);
	public final FGroupTierCat mGroupTierCat = new FGroupTierCat( this);
	public final FGroupCat mGroupCat = new FGroupCat( this);
	public String mPassCode;
	public String mDescr;
	private boolean mModified;

	public FHqm( String name) {
		super( name);
		mDescr = "Hallo D:";
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forHQM( this, p);
	}

	public boolean contains( FileVersion other) {
		return getVersion().contains( other);
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
	public ABase getParent() {
		return null;
	}

	public FileVersion getVersion() {
		return mVersion;
	}

	public boolean isModified() {
		return mModified;
	}

	public void setModified( boolean value) {
		mModified = value;
	}

	public void setVersion( FileVersion version) {
		mVersion = version;
	}
}
