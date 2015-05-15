package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;

public class QuestOfIdx extends AHQMWorker<FQuest, Object> {
	private int mIndex;

	private QuestOfIdx( int index) {
		mIndex = index;
	}

	public static FQuest get( FHqm hqm, int idx) {
		QuestOfIdx worker = new QuestOfIdx( idx);
		return hqm.forEachQuest( worker, null);
	}

	@Override
	public FQuest forQuest( FQuest quest, Object p) {
		if (mIndex == 0) {
			return quest;
		}
		--mIndex;
		return null;
	}
}
