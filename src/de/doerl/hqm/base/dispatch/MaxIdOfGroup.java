package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FHqm;

public class MaxIdOfGroup extends AHQMWorker<Object, Object> {
	private int mResult = -1;

	private MaxIdOfGroup() {
	}

	public static int get( FGroupTier tier) {
		return get( tier.mParentCategory);
	}

	public static int get( FGroupTierCat cat) {
		MaxIdOfGroup worker = new MaxIdOfGroup();
		cat.forEachMember( worker, null);
		return worker.mResult + 1;
	}

	public static int get( FHqm hqm) {
		return get( hqm.mGroupTierCat);
	}

	@Override
	public Object forGroup( FGroup grp, Object p) {
		mResult = Math.max( mResult, grp.getID());
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, Object p) {
		tier.forEachGroup( this, null);
		return null;
	}
}
