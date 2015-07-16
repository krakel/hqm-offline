package de.doerl.hqm.base;

import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.utils.Utils;

public final class FReputation extends AMember {
	private static final Logger LOGGER = Logger.getLogger( FReputation.class.getName());
	private static final String BASE = "rep";
	public final FReputationCat mParentCategory;
	private int mID;
	public final Vector<FMarker> mMarker = new Vector<>();
	public String mNeutral;

	public FReputation( FReputationCat parent, String name) {
		super( name);
		mParentCategory = parent;
		mID = MaxIdOf.getReputation( parent) + 1;
	}

	public static int fromIdent( String ident) {
		return fromIdent( BASE, ident);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forReputation( this, p);
	}

	public FMarker createMarker( String name) {
		FMarker marker = new FMarker( this, name);
		mMarker.add( marker);
		return marker;
	}

	public <T, U> T forEachMarker( IHQMWorker<T, U> worker, U p) {
		for (FMarker disp : mMarker) {
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
		return ElementTyp.REPUTATION;
	}

	public int getID() {
		return mID;
	}

	@Override
	public FReputationCat getParent() {
		return mParentCategory;
	}

	public boolean isFirst() {
		return ABase.isFirst( mParentCategory.mArr, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentCategory.mArr, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentCategory.mArr, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentCategory.mArr, this);
	}

	public void remove() {
		ABase.remove( mParentCategory.mArr, this);
	}

	public void setID( String ident) {
		if (ident != null) {
			int id = fromIdent( ident);
			if (id >= 0) {
				mID = id;
			}
		}
	}

	public void sort() {
		Collections.sort( mMarker);
	}

	public String toIdent() {
		return toIdent( BASE, mID);
	}

	@Override
	public String toString() {
		return String.format( "%s [%s]", mName, mNeutral);
	}
}
