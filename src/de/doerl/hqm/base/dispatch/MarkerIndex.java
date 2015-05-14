package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.utils.Utils;

public class MarkerIndex extends AHQMWorker<Boolean, Object> {
	private int mResult = -1;
	private FMarker mMarker;

	private MarkerIndex( FMarker mark) {
		mMarker = mark;
	}

	public static int get( FMarker mark) {
		if (mark == null) {
			return -1;
		}
		MarkerIndex worker = new MarkerIndex( mark);
		mark.getParent().forEachMarker( worker, null);
		return worker.mResult;
	}

	@Override
	public Boolean forMarker( FMarker mark, Object p) {
		++mResult;
		return Utils.equals( mark, mMarker) ? Boolean.TRUE : null;
	}
}
