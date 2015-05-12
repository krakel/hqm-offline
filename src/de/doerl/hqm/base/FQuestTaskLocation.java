package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.utils.Utils;

public final class FQuestTaskLocation extends AQuestTask {
	private static final Logger LOGGER = Logger.getLogger( FQuestTaskLocation.class.getName());
	private Vector<FLocation> mLocations = new Vector<FLocation>();

	public FQuestTaskLocation( FQuest parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskLocation( this, p);
	}

	public FLocation createLocation( FItemStack icon, String name) {
		FLocation loc = new FLocation( this, icon, name);
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
				Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
			}
		}
		return null;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_TASK_LOCATION;
	}
}
