package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FMarker extends ANamed implements Comparable<FMarker> {
	public final FReputation mParentRep;
	public final FParameterInt mMark = new FParameterInt( this);

	public FMarker( FReputation parent, String name) {
		super( name);
		mParentRep = parent;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forMarker( this, p);
	}

	public int compareTo( FMarker other) {
		return mMark.compareTo( other.mMark);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.REPUTATION_MARKER;
	}

	@Override
	public FReputation getParent() {
		return mParentRep;
	}
}
