package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;

public class QuestOfID extends AHQMWorker<FQuest, Object> {
	private int mIndex;

	private QuestOfID( int index) {
		mIndex = index;
	}

	public static FQuest get( FHqm hqm, int index) {
		return get( hqm.mQuestSetCat, index);
	}

	public static FQuest get( FQuestSetCat cat, int index) {
		QuestOfID worker = new QuestOfID( index);
		return cat.forEachMember( worker, null);
	}

	@Override
	public FQuest forQuest( FQuest quest, Object p) {
		if (mIndex == quest.mID) {
			return quest;
		}
		return null;
	}

	@Override
	public FQuest forQuestSet( FQuestSet set, Object p) {
		return set.forEachQuest( this, null);
	}
}
