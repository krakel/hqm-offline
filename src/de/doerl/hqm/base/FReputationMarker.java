package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FReputationMarker extends ANamed implements Comparable<FReputationMarker> {
	public final FReputation mParentRep;
	public final FParameterInt mValue = new FParameterInt( this, "Value");

	public FReputationMarker( FReputation parent, String name) {
		super( name);
		mParentRep = parent;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forReputationMarker( this, p);
	}

	public int compareTo( FReputationMarker other) {
		return mValue.compareTo( other.mValue);
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
