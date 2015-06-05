package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FMarker extends ANamed implements Comparable<FMarker>, IElement {
	public final FReputation mParentRep;
	public int mMark;

	public FMarker( FReputation parent, String name) {
		super( name);
		mParentRep = parent;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forMarker( this, p);
	}

	public int compareTo( FMarker other) {
		return Integer.compare( mMark, other.mMark);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.REPUTATION_MARKER;
	}

	@Override
	public FReputation getHierarchy() {
		return mParentRep;
	}

	@Override
	public FReputation getParent() {
		return mParentRep;
	}

	@Override
	public boolean isFirst() {
		return ABase.isFirst( mParentRep.mMarker, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentRep.mMarker, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentRep.mMarker, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentRep.mMarker, this);
	}

	@Override
	public void remove() {
		ABase.remove( mParentRep.mMarker, this);
	}
}
