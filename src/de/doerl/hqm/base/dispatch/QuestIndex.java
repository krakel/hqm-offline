package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.utils.Utils;

public class QuestIndex extends AHQMWorker<Boolean, Object> {
	private int mResult = -1;
	private FQuest mQuest;

	private QuestIndex( FQuest quest) {
		mQuest = quest;
	}

	public static int get( FQuest quest) {
		if (quest == null) {
			return -1;
		}
		QuestIndex worker = new QuestIndex( quest);
		quest.mParentSet.mParentCategory.forEachMember( worker, null);
		return worker.mResult;
	}

	@Override
	public Boolean forQuest( FQuest quest, Object p) {
		++mResult;
		return Utils.equals( quest, mQuest) ? Boolean.TRUE : null;
	}

	@Override
	public Boolean forQuestSet( FQuestSet qs, Object p) {
		return qs.forEachQuest( this, p);
	}
}
