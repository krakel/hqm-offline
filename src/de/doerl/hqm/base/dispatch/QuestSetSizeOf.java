package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.utils.Utils;

public class QuestSetSizeOf extends AHQMWorker<Object, FQuestSet> {
	private int mResult;

	private QuestSetSizeOf() {
	}

	public static int get( ACategory<FQuestSet> set) {
		QuestSetSizeOf size = new QuestSetSizeOf();
		set.mParentHQM.forEachQuest( size, null);
		return size.mResult;
	}

	public static int get( FQuestSet qs) {
		QuestSetSizeOf size = new QuestSetSizeOf();
		qs.mParentCategory.mParentHQM.forEachQuest( size, qs);
		return size.mResult;
	}

	@Override
	public Object forQuest( FQuest quest, FQuestSet qs) {
		if (qs == null || Utils.equals( quest.mSet, qs) && !quest.isDeleted()) {
			++mResult;
		}
		return null;
	}
}
