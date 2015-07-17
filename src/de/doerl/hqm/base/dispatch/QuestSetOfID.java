package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;

public class QuestSetOfID extends AHQMWorker<FQuestSet, Integer> {
	private static final QuestSetOfID WORKER = new QuestSetOfID();

	private QuestSetOfID() {
	}

	public static FQuestSet get( FHqm hqm, int id) {
		return hqm.mQuestSetCat.forEachMember( WORKER, id);
	}

	public static FQuestSet get( FQuestSet set, int id) {
		return set.mParentCategory.forEachMember( WORKER, id);
	}

	public static FQuestSet get( FQuestSetCat cat, int id) {
		return cat.forEachMember( WORKER, id);
	}

	@Override
	public FQuestSet forQuestSet( FQuestSet set, Integer id) {
		return set.getID() == id.intValue() ? set : null;
	}
}
