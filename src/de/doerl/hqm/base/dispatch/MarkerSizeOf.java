package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FMarker;

public class MarkerSizeOf extends AHQMWorker<Object, Object> {
	private int mResult;

	private MarkerSizeOf() {
	}

	public static int get( FReputation rep) {
		MarkerSizeOf size = new MarkerSizeOf();
		rep.forEachMarker( size, null);
		return size.mResult;
	}

	@Override
	public Object forMarker( FMarker mark, Object p) {
		++mResult;
		return null;
	}
}
