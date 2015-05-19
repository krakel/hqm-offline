package de.doerl.hqm.base;

import java.net.URI;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.Utils;

public final class FHqm extends ABase {
	private static final Logger LOGGER = Logger.getLogger( FHqm.class.getName());
	private final FQuest mDeleted = new FQuest( this, "__DELETED__", true);
	private FileVersion mVersion;
	public final FParameterString mPassCode = new FParameterString( this);
	public final FParameterString mDesc = new FParameterString( this);
	public final FQuestSets mQuestSets = new FQuestSets( this);
	private Vector<FQuest> mQuests = new Vector<FQuest>();
	public final FReputations mRepSets = new FReputations( this);
	public final FGroupTiers mGroupTiers = new FGroupTiers( this);
	public final FGroups mGroups = new FGroups( this);
	private URI mURI;

	public FHqm( URI uri) {
		mURI = uri;
		mDesc.mValue = "Hallo D:";
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
	public ABase getParent() {
		return null;
	}

	public URI getURI() {
		return mURI;
	}

	public FileVersion getVersion() {
		return mVersion;
	}

	public void setVersion( FileVersion version) {
		mVersion = version;
	}
}
