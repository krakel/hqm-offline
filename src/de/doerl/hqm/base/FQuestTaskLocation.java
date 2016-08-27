package de.doerl.hqm.base;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.utils.Utils;

public final class FQuestTaskLocation extends AQuestTask {
	private static final Logger LOGGER = Logger.getLogger( FQuestTaskLocation.class.getName());
	public final ArrayList<FLocation> mLocations = new ArrayList<>();

	FQuestTaskLocation( FQuest parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskLocation( this, p);
	}

	public FLocation createLocation() {
		FLocation loc = new FLocation( this);
		mLocations.add( loc);
		return loc;
	}

	public FLocation createLocation( int id) {
		FLocation loc = new FLocation( this, id);
		mLocations.add( loc);
		return loc;
	}

	public <T, U> T forEachLocation( IHQMWorker<T, U> worker, U p) {
		for (FLocation disp : mLocations) {
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
	public TaskTyp getTaskTyp() {
		return TaskTyp.LOCATION;
	}
}
