package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.utils.Utils;

public class GroupIndex extends AHQMWorker<Boolean, Object> {
	private int mResult = -1;
	private FGroup mGrp;

	private GroupIndex( FGroup grp) {
		mGrp = grp;
	}

	public static int get( FGroup grp) {
		GroupIndex worker = new GroupIndex( grp);
		grp.mParentCategory.forEachMember( worker, null);
		return worker.mResult;
	}

	@Override
	public Boolean forGroup( FGroup grp, Object p) {
		++mResult;
		return Utils.equals( grp, mGrp) ? Boolean.TRUE : null;
	}
}
