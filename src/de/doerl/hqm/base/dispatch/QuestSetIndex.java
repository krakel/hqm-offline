package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.utils.Utils;

public class QuestSetIndex extends AHQMWorker<Boolean, Object> {
	private int mResult = -1;
	private FQuestSet mSet;

	private QuestSetIndex( FQuestSet set) {
		mSet = set;
	}

	public static int get( FQuestSet set) {
		QuestSetIndex worker = new QuestSetIndex( set);
		set.mParentCategory.forEachMember( worker, null);
		return worker.mResult;
	}

	@Override
	public Boolean forQuestSet( FQuestSet set, Object p) {
		++mResult;
		return Utils.equals( set, mSet) ? Boolean.TRUE : null;
	}
}
