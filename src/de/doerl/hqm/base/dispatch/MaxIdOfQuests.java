package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;

public class MaxIdOfQuests extends AHQMWorker<Object, Object> {
	private int mResult = -1;

	private MaxIdOfQuests() {
	}

	public static int get( FHqm hqm) {
		return get( hqm.mQuestSetCat);
	}

	public static int get( FQuestSetCat cat) {
		MaxIdOfQuests worker = new MaxIdOfQuests();
		cat.forEachMember( worker, null);
		return worker.mResult;
	}

	@Override
	public Object forQuest( FQuest quest, Object p) {
		mResult = Math.max( mResult, quest.mID);
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		set.forEachQuest( this, null);
		return null;
	}
}
