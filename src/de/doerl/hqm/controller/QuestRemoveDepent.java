package de.doerl.hqm.controller;

import java.util.Vector;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.Utils;

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
		remove( quest.mRequirements);
		remove( quest.mOptionLinks);
		mCtrl.fireChanged( quest);
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		set.forEachQuest( this, p);
		return null;
	}

	private void remove( Vector<FQuest> arr) {
		for (int i = 0; i < arr.size(); ++i) {
			FQuest q = arr.get( i);
			if (Utils.equals( q, mQuest)) {
				arr.setElementAt( null, i);
			}
		}
	}
}
