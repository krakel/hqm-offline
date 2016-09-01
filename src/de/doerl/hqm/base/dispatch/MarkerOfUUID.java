/*******************************************************************************
 * (c) Copyright IBM Corporation 2016. All Rights Reserved.
 *
 *******************************************************************************/
package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.utils.Utils;

public class MarkerOfUUID extends AHQMWorker<FMarker, String> {
	private static final MarkerOfUUID WORKER = new MarkerOfUUID();

	private MarkerOfUUID() {
	}

	public static FMarker get( FReputation set, String uuid) {
		return set.forEachMarker( WORKER, uuid);
	}

	@Override
	public FMarker forMarker( FMarker mark, String uuid) {
		return Utils.equals( mark.getID(), uuid) ? mark : null;
	}
}
