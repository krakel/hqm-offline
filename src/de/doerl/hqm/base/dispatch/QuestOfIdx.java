package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSets;

public class QuestOfIdx extends AHQMWorker<FQuest, Object> {
	private int mIndex;

	private QuestOfIdx( int index) {
		mIndex = index;
	}

	public static FQuest get( FQuestSets set, int idx) {
		QuestOfIdx worker = new QuestOfIdx( idx);
		return set.forEachMember( worker, null);
	}

	@Override
	public FQuest forQuest( FQuest quest, Object p) {
		if (mIndex == 0) {
			return quest;
		}
		--mIndex;
		return null;
	}

	@Override
	public FQuest forQuestSet( FQuestSet qs, Object p) {
		return qs.forEachQuest( this, p);
	}
}
