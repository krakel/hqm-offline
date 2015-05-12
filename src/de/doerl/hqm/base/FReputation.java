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
	public final FParameterInteger mID = new FParameterInteger( this, "ID");
	public final FParameterString mNeutral = new FParameterString( this, "Neutral");
	private Vector<FReputationMarker> mMarker = new Vector<FReputationMarker>();

	public FReputation( FReputations parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forReputation( this, p);
	}

	public FReputationMarker createMarker( String name) {
		FReputationMarker marker = new FReputationMarker( this, name);
		mMarker.add( marker);
		return marker;
	}

	public <T, U> T forEachMarker( IHQMWorker<T, U> worker, U p) {
		for (FReputationMarker disp : mMarker) {
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

	public void sort() {
		Collections.sort( mMarker);
	}
}
