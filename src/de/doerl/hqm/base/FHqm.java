package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.Utils;

public final class FHqm extends ANamed {
	private static final Logger LOGGER = Logger.getLogger( FHqm.class.getName());
	private final FQuest mDeleted = new FQuest( this, "__DELETED__", true);
	private FileVersion mVersion;
	public final FQuestSetCat mQuestSetCat = new FQuestSetCat( this);
	public final FReputationCat mReputationCat = new FReputationCat( this);
	public final FGroupTierCat mGroupTierCat = new FGroupTierCat( this);
	public final FGroupCat mGroupCat = new FGroupCat( this);
	public String mPassCode;
	public String mDescr;
	private Vector<FQuest> mQuests = new Vector<>();
	private boolean mModified;

	public FHqm( String name) {
		super( name);
		mDescr = "Hallo D:";
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forHQM( this, p);
	}

	public void addDeletedQuest() {
		mQuests.add( mDeleted);
	}

	public FQuest createQuest( String name) {
		FQuest reward = new FQuest( this, name);
		mQuests.add( reward);
		return reward;
	}

	public <T, U> T forEachQuest( IHQMWorker<T, U> worker, U p) {
		for (FQuest disp : mQuests) {
			try {
				if (disp != null) {
					T obj = disp.accept( worker, p);
					if (obj != null) {
						return obj;
					}
				}
			}
			catch (RuntimeException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return null;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.HQM;
	}

	@Override
	public ABase getHierarchy() {
		return null;
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

	public void removeQuest( FQuest quest) {
		int pos = mQuests.indexOf( quest);
		if (pos >= 0) {
			mQuests.setElementAt( mDeleted, pos);
		}
	}

	public void setModified( boolean value) {
		mModified = value;
	}

	public void setVersion( FileVersion version) {
		mVersion = version;
	}
}
