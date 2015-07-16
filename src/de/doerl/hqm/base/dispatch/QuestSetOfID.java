package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.utils.Utils;

public class QuestSetOfID extends AHQMWorker<FQuestSet, String> {
	private static final QuestSetOfID WORKER = new QuestSetOfID();

	private QuestSetOfID() {
	}

	public static FQuestSet get( FHqm hqm, String id) {
		return hqm.mQuestSetCat.forEachMember( WORKER, id);
	}

	public static FQuestSet get( FQuestSetCat cat, String id) {
		return cat.forEachMember( WORKER, id);
	}

	@Override
	public FQuestSet forQuestSet( FQuestSet set, String id) {
		return Utils.equals( set.mID, id) ? set : null;
	}
}
