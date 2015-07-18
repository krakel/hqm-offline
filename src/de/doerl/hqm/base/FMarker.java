package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;

public final class FMarker extends AIdent implements Comparable<FMarker>, IElement {
	private static final String BASE = "mark";
	public final FReputation mParentRep;
	public int mMark;

	FMarker( FReputation parent) {
		super( BASE, MaxIdOf.getMarker( parent) + 1);
		mParentRep = parent;
	}

	public static int fromIdent( String ident) {
		return AIdent.fromIdent( BASE, ident);
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
	public String getName() {
		return getInfo().mInfo1;
	}

	public String getName( String lang) {
		return getInfo( lang).mInfo1;
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

	@Override
	public void setName( String name) {
		getInfo().mInfo1 = name;
	}

	public void setName( String lang, String name) {
		getInfo( lang).mInfo1 = name;
	}

	@Override
	public String toString() {
		return String.format( "%s(%d)", getName(), mMark);
	}
}
