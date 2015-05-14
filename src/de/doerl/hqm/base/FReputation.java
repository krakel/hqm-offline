package de.doerl.hqm.base;

import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.utils.Utils;

public final class FReputation extends AMember<FReputation> {
	private static final Logger LOGGER = Logger.getLogger( FReputation.class.getName());
	public final FReputations mParentSet;
	public final FParameterInteger mID = new FParameterInteger( this, "ID");
	public final FParameterString mNeutral = new FParameterString( this, "Neutral");
	private Vector<FMarker> mMarker = new Vector<FMarker>();

	public FReputation( FReputations parent, String name) {
		super( name);
		mParentSet = parent;
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

	@Override
	public ASet<FReputation> getParent() {
		return mParentSet;
	}

	public void sort() {
		Collections.sort( mMarker);
	}
}
