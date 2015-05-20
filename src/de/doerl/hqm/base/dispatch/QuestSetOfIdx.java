package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;

public class QuestSetOfIdx extends AHQMWorker<FQuestSet, Object> {
	private int mIndex;

	private QuestSetOfIdx( int index) {
		mIndex = index;
	}

	public static FQuestSet get( FQuestSetCat set, int idx) {
		QuestSetOfIdx worker = new QuestSetOfIdx( idx);
		return set.forEachMember( worker, null);
	}

	@Override
	public FQuestSet forQuestSet( FQuestSet set, Object p) {
		if (mIndex == 0) {
			return set;
		}
		--mIndex;
		return null;
	}
}
