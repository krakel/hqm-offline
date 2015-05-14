package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FMarker;

public class MarkerOfIdx extends AHQMWorker<FMarker, Object> {
	private int mIndex;

	private MarkerOfIdx( int index) {
		mIndex = index;
	}

	public static FMarker get( FReputation rep, int idx) {
		if (rep == null) {
			return null;
		}
		MarkerOfIdx worker = new MarkerOfIdx( idx);
		return rep.forEachMarker( worker, null);
	}

	@Override
	public FMarker forMarker( FMarker mark, Object p) {
		if (mIndex == 0) {
			return mark;
		}
		--mIndex;
		return null;
	}
}
