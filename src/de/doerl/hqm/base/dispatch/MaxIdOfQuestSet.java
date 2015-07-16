package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;

public class MaxIdOfQuestSet extends AHQMWorker<Object, Object> {
	private int mResult = -1;

	private MaxIdOfQuestSet() {
	}

	public static int get( FHqm hqm) {
		return get( hqm.mQuestSetCat);
	}

	public static int get( FQuestSetCat cat) {
		MaxIdOfQuestSet worker = new MaxIdOfQuestSet();
		cat.forEachMember( worker, null);
		return worker.mResult;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		mResult = Math.max( mResult, set.getID());
		return null;
	}
}
