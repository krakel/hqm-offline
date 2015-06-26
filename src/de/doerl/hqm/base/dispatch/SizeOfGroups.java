package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;

public class SizeOfGroups extends AHQMWorker<Object, Object> {
	private int mResult = 0;

	private SizeOfGroups() {
	}

	public static Object get( FGroupTier tier) {
		SizeOfGroups size = new SizeOfGroups();
		tier.forEachGroup( size, null);
		return size.mResult;
	}

	public static int get( FGroupTierCat cat) {
		SizeOfGroups size = new SizeOfGroups();
		cat.forEachMember( size, null);
		return size.mResult;
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
