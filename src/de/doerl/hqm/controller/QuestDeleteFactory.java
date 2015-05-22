package de.doerl.hqm.controller;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.Utils;

class QuestDeleteFactory extends AHQMWorker<Object, FQuestSet> {
	private EditController mController;

	public QuestDeleteFactory( EditController ctrl) {
		mController = ctrl;
	}

	@Override
	public Object forQuest( FQuest quest, FQuestSet qs) {
		if (Utils.equals( quest.mQuestSet, qs)) {
			mController.questDependentDelete( quest);
			quest.remove();
		}
		return null;
	}
}
