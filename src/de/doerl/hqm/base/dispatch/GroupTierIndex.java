package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.utils.Utils;

public class GroupTierIndex extends AHQMWorker<Boolean, Object> {
	private int mResult = -1;
	private FGroupTier mTier;

	private GroupTierIndex( FGroupTier tier) {
		mTier = tier;
	}

	public static int get( FGroupTier tier) {
		GroupTierIndex worker = new GroupTierIndex( tier);
		tier.mParentCategory.forEachMember( worker, null);
		return worker.mResult;
	}

	@Override
	public Boolean forGroupTier( FGroupTier tier, Object p) {
		++mResult;
		return Utils.equals( tier, mTier) ? Boolean.TRUE : null;
	}
}
