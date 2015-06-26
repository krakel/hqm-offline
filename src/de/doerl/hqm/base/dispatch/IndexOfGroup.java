package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.utils.Utils;

public class IndexOfGroup extends AHQMWorker<Boolean, Object> {
	private int mResult = -1;
	private ABase mBase;

	private IndexOfGroup( ABase set) {
		mBase = set;
	}

	public static int get( FGroup grp) {
		IndexOfGroup worker = new IndexOfGroup( grp);
		grp.getParent().mParentCategory.forEachMember( worker, null);
		return worker.mResult;
	}

	@Override
	public Boolean forGroup( FGroup grp, Object p) {
		++mResult;
		return Utils.equals( grp, mBase) ? Boolean.TRUE : null;
	}

	@Override
	public Boolean forGroupTier( FGroupTier tier, Object p) {
		return tier.forEachGroup( this, p);
	}
}
