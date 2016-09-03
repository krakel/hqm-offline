package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.utils.Utils;

public class QuestOfName extends AHQMWorker<FQuest, String> {
	private static final QuestOfName WORKER = new QuestOfName();

	private QuestOfName() {
	}

	public static FQuest get( FHqm hqm, String name) {
		return get( hqm.mQuestSetCat, name);
	}

	public static FQuest get( FQuestSet set, String name) {
		return get( set.mParentCategory, name);
	}

	public static FQuest get( FQuestSetCat cat, String name) {
		return cat.forEachMember( WORKER, name);
	}

	@Override
	public FQuest forQuest( FQuest quest, String name) {
		return Utils.equals( quest.getName(), name) ? quest : null;
	}

	@Override
	public FQuest forQuestSet( FQuestSet set, String name) {
		return set.forEachQuest( this, name);
	}
}
