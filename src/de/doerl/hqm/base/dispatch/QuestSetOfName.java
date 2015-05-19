package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSets;
import de.doerl.hqm.utils.Utils;

public class QuestSetOfName extends AHQMWorker<FQuestSet, String> {
	private static final QuestSetOfName WORKER = new QuestSetOfName();

	private QuestSetOfName() {
	}

	public static FQuestSet get( FQuestSets set, String name) {
		return set.forEachMember( WORKER, name);
	}

	@Override
	public FQuestSet forQuestSet( FQuestSet qs, String name) {
		return Utils.equals( qs.mName, name) ? qs : null;
	}
}
