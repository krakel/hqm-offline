package de.doerl.hqm.base;

import java.net.URI;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.FileVersion;

public final class FHqm extends ABase {
//	private static final Logger LOGGER = Logger.getLogger( FHqm.class.getName());
	private FileVersion mVersion;
	public final FParameterString mPassCode = new FParameterString( this, "PassCode");
	public final FParameterString mDesc = new FParameterString( this, "Description");
	public final FQuestSets mQuestSets = new FQuestSets( this);
	public final FReputations mRepSets = new FReputations( this);
	public final FGroupTiers mGroupTiers = new FGroupTiers( this);
	public final FGroups mGroups = new FGroups( this);
	private URI mURI;

	public FHqm( URI uri) {
		mURI = uri;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forHQM( this, p);
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
