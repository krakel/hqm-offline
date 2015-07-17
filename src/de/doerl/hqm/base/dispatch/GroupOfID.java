package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;

public class GroupOfID extends AHQMWorker<FGroup, Integer> {
	private static final GroupOfID WORKER = new GroupOfID();

	private GroupOfID() {
	}

	public static FGroup get( FGroupTier set, int id) {
		return set.forEachGroup( WORKER, id);
	}

	@Override
	public FGroup forGroup( FGroup grp, Integer id) {
		return grp.getID() == id.intValue() ? grp : null;
	}
}
