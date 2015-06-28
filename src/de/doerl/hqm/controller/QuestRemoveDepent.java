package de.doerl.hqm.controller;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;

class QuestRemoveDepent extends AHQMWorker<Object, Object> {
	private FQuest mQuest;
	private EditController mCtrl;

	private QuestRemoveDepent( FQuest quest, EditController ctrl) {
		mQuest = quest;
		mCtrl = ctrl;
	}

	public static void get( FQuest quest, EditController ctrl) {
		QuestRemoveDepent worker = new QuestRemoveDepent( quest, ctrl);
		quest.getParent().mParentCategory.forEachMember( worker, null);
	}

	@Override
	public Object forQuest( FQuest quest, Object p) {
		quest.mRequirements.remove( mQuest);
		quest.mOptionLinks.remove( mQuest);
		mCtrl.fireChanged( quest);
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		set.forEachQuest( this, p);
		return null;
	}
}
