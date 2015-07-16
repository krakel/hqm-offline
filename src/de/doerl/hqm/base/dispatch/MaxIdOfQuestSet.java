package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.utils.Utils;

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
		if (set.mID != null && set.mID.length() > 3) {
			mResult = Math.max( mResult, Utils.parseInteger( set.mID.substring( 3), 0));
		}
		return null;
	}
}
