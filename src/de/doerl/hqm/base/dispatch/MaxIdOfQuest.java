package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;

public class MaxIdOfQuest extends AHQMWorker<Object, Object> {
	private int mResult = -1;

	private MaxIdOfQuest() {
	}

	public static int get( FHqm hqm) {
		return get( hqm.mQuestSetCat);
	}

	public static int get( FQuestSet set) {
		return get( set.mParentCategory);
	}

	public static int get( FQuestSetCat cat) {
		MaxIdOfQuest worker = new MaxIdOfQuest();
		cat.forEachMember( worker, null);
		return worker.mResult + 1;
	}

	@Override
	public Object forQuest( FQuest quest, Object p) {
		mResult = Math.max( mResult, quest.getID());
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		set.forEachQuest( this, null);
		return null;
	}
}
