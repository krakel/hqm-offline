package de.doerl.hqm.controller;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;

class QuestSetDelete extends AHQMWorker<Object, EditController> {
	private static final QuestSetDelete WORKER = new QuestSetDelete();

	private QuestSetDelete() {
	}

	public static void get( FQuestSet set, EditController ctrl) {
		set.forEachQuest( WORKER, ctrl);
		set.remove();
	}

	@Override
	public Object forQuest( FQuest quest, EditController ctrl) {
		QuestRemoveDepent.get( quest, ctrl);
		return null;
	}
}
