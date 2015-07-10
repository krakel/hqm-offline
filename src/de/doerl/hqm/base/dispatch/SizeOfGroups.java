package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;

public class SizeOfGroups extends AHQMWorker<Object, Object> {
	private int mResult = 0;

	private SizeOfGroups() {
	}

	public static Object get( FGroupTier tier) {
		SizeOfGroups worker = new SizeOfGroups();
		tier.forEachGroup( worker, null);
		return worker.mResult;
	}

	public static int get( FGroupTierCat cat) {
		SizeOfGroups worker = new SizeOfGroups();
		cat.forEachMember( worker, null);
		return worker.mResult;
	}

	@Override
	public Object forGroup( FGroup grp, Object p) {
		++mResult;
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, Object p) {
		tier.forEachGroup( this, p);
		return null;
	}
}
