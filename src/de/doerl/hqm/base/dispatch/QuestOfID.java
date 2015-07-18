package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;

public class QuestOfID extends AHQMWorker<FQuest, Integer> {
	private static final QuestOfID WORKER = new QuestOfID();

	private QuestOfID() {
	}

	public static FQuest get( FHqm hqm, int id) {
		return get( hqm.mQuestSetCat, id);
	}

	public static ANamed get( FQuestSet set, int id) {
		return get( set.mParentCategory, id);
	}

	public static FQuest get( FQuestSet set, String ident) {
		return get( set.mParentCategory, FQuest.fromIdent( ident));
	}

	public static FQuest get( FQuestSetCat cat, int id) {
		return cat.forEachMember( WORKER, id);
	}

	@Override
	public FQuest forQuest( FQuest quest, Integer id) {
		return quest.getID() == id.intValue() ? quest : null;
	}

	@Override
	public FQuest forQuestSet( FQuestSet set, Integer id) {
		return set.forEachQuest( this, id);
	}
}
