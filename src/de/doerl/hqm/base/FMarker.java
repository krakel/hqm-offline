package de.doerl.hqm.base;

import java.util.HashMap;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;

public final class FMarker extends AIdented implements Comparable<FMarker>, IElement {
	private static final String BASE = "mark";
	public final FReputation mParentRep;
	public int mMark;
	private HashMap<String, String> mInfo = new HashMap<>();

	FMarker( FReputation parent) {
		super( BASE, MaxIdOf.getMarker( parent) + 1);
		mParentRep = parent;
	}

	public static int fromIdent( String ident) {
		return AIdented.fromIdent( BASE, ident);
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
		return mInfo.get( getHqm().mLang);
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
		mInfo.put( getHqm().mLang, name);
	}

	@Override
	public String toString() {
		return String.format( "%s(%d)", getName(), mMark);
	}
}
