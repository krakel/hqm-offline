package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FReputation;

public class MarkerOfID extends AHQMWorker<FMarker, Integer> {
	private static final MarkerOfID WORKER = new MarkerOfID();

	private MarkerOfID() {
	}

	public static FMarker get( FReputation set, int id) {
		return set.forEachMarker( WORKER, id);
	}

	@Override
	public FMarker forMarker( FMarker mark, Integer id) {
		return mark.getID() == id.intValue() ? mark : null;
	}
}
