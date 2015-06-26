package de.doerl.hqm.controller;

import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.Utils;

class GroupTierDelete extends AHQMWorker<Object, Object> {
	private FGroupTier mTier;
	private EditController mCtrl;

	private GroupTierDelete( FGroupTier tier, EditController ctrl) {
		mTier = tier;
		mCtrl = ctrl;
	}

	public static void get( FGroupTier tier, EditController ctrl) {
		GroupTierDelete worker = new GroupTierDelete( tier, ctrl);
		tier.mParentCategory.mParentHQM.mGroupCat.forEachMember( worker, null);
		tier.remove();
	}

	@Override
	public Object forGroup( FGroup grp, Object p) {
		if (Utils.equals( grp.mTier, mTier)) {
			grp.mTier = null;
			mCtrl.fireChanged( grp);
		}
		return null;
	}
}
