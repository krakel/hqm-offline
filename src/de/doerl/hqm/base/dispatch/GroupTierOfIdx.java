package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FHqm;

public class GroupTierOfIdx extends AHQMWorker<FGroupTier, Object> {
	private int mIndex;

	private GroupTierOfIdx( int index) {
		mIndex = index;
	}

	public static FGroupTier get( FGroupTierCat tier, int idx) {
		GroupTierOfIdx worker = new GroupTierOfIdx( idx);
		return tier.forEachMember( worker, null);
	}

	public static FGroupTier get( FHqm hqm, int idx) {
		return get( hqm.mGroupTierCat, idx);
	}

	@Override
	public FGroupTier forGroupTier( FGroupTier tier, Object p) {
		if (mIndex == 0) {
			return tier;
		}
		--mIndex;
		return null;
	}
}
