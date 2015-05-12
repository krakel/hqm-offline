package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.ASet;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;

public class QuestSetSizeOf extends AHQMWorker<Object, Object> {
	private int mResult;

	private QuestSetSizeOf() {
	}

	public static int get( ASet<FQuestSet> set) {
		QuestSetSizeOf size = new QuestSetSizeOf();
		set.forEachMember( size, null);
		return size.mResult;
	}

	public static int get( FQuestSet qs) {
		QuestSetSizeOf size = new QuestSetSizeOf();
		qs.forEachQuest( size, null);
		return size.mResult;
	}

	@Override
	public Object forQuest( FQuest quest, Object p) {
		++mResult;
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet qs, Object p) {
		qs.forEachQuest( this, null);
		return null;
	}
}
