package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FQuestTaskLocation;

public class LocationOfId extends AHQMWorker<FLocation, Integer> {
	private static final LocationOfId WORKER = new LocationOfId();

	private LocationOfId() {
	}

	public static FLocation get( FQuestTaskLocation task, int id) {
		return task.forEachLocation( WORKER, id);
	}

	public static FLocation get( FQuestTaskLocation task, String ident) {
		return task.forEachLocation( WORKER, FLocation.fromIdent( ident));
	}

	@Override
	public FLocation forLocation( FLocation loc, Integer id) {
		return loc.getID() == id.intValue() ? loc : null;
	}
}
