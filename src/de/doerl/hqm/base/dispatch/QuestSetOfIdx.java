package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSets;

public class QuestSetOfIdx extends AHQMWorker<FQuestSet, Object> {
	private int mIndex;

	private QuestSetOfIdx( int index) {
	}

	public static FQuestSet get( FQuestSets set, int idx) {
		QuestSetOfIdx worker = new QuestSetOfIdx( idx);
		return set.forEachMember( worker, null);
	}

	@Override
	public FQuestSet forQuestSet( FQuestSet qs, Object p) {
		if (mIndex == 0) {
			return qs;
		}
		--mIndex;
		return null;
	}
}
