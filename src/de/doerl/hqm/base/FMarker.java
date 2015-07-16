package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;

public final class FMarker extends ANamed implements Comparable<FMarker>, IElement {
	private static final String BASE = "mark";
	public final FReputation mParentRep;
	private int mID;
	public int mMark;

	public FMarker( FReputation parent, String name) {
		super( name);
		mParentRep = parent;
		mID = MaxIdOf.getMarker( parent) + 1;
	}

	public static int fromIdent( String ident) {
		return fromIdent( BASE, ident);
	}

	public static String toIdent( int idx) {
		return toIdent( BASE, idx);
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

	public int getID() {
		return mID;
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

	public void setID( String ident) {
		if (ident != null) {
			int id = fromIdent( ident);
			if (id >= 0) {
				mID = id;
			}
		}
	}

	public String toIdent() {
		return toIdent( BASE, mID);
	}

	@Override
	public String toString() {
		return String.format( "%s(%d)", mName, mMark);
	}
}
