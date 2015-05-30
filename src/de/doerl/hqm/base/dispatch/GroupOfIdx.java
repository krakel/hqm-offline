package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FHqm;

public class GroupOfIdx extends AHQMWorker<FGroup, Object> {
	private int mIndex;

	private GroupOfIdx( int index) {
		mIndex = index;
	}

	public static FGroup get( FHqm hqm, int idx) {
		GroupOfIdx worker = new GroupOfIdx( idx);
		return hqm.mGroupTierCat.forEachMember( worker, null);
	}

	@Override
	public FGroup forGroup( FGroup grp, Object p) {
		if (mIndex == 0) {
			return grp;
		}
		--mIndex;
		return null;
	}
}
