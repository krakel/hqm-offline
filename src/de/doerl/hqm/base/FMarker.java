package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.utils.ToString;

public final class FMarker extends AIdent implements Comparable<FMarker>, IElement {
	private static final String BASE = "mark";
	public final FReputation mParentRep;
	public int mMark;

	FMarker( FReputation parent) {
		super( BASE, MaxIdOf.getMarker( parent));
		mParentRep = parent;
	}

	FMarker( FReputation parent, int id) {
		super( BASE, id);
		mParentRep = parent;
	}

	public static int fromIdent( String ident) {
		return AIdent.fromIdent( BASE, ident);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forMarker( this, p);
	}

	@Override
	public int compareTo( FMarker other) {
		return Integer.compare( mMark, other.mMark);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.REPUTATION_MARKER;
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
	void localeDefault( LocaleInfo info) {
		info.mInfo1 = "Unnamed";
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
	public String toString() {
		ToString sb = new ToString( this);
		sb.appendMsg( "name", getName());
		sb.appendMsg( "mark", mMark);
		return sb.toString();
	}
}
