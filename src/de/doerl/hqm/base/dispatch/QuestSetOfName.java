package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.utils.Utils;

public class QuestSetOfName extends AHQMWorker<FQuestSet, String> {
	private static final QuestSetOfName WORKER = new QuestSetOfName();

	private QuestSetOfName() {
	}

	public static FQuestSet get( FHqm hqm, String name) {
		return hqm.mQuestSetCat.forEachMember( WORKER, name);
	}

	public static FQuestSet get( FQuestSetCat cat, String name) {
		return cat.forEachMember( WORKER, name);
	}

	@Override
	public FQuestSet forQuestSet( FQuestSet set, String name) {
		return Utils.equals( set.mName, name) ? set : null;
	}
}
