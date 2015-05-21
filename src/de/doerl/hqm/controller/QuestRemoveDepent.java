package de.doerl.hqm.controller;

import java.util.Vector;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.Utils;

class QuestRemoveDepent extends AHQMWorker<Object, FQuest> {
	private EditController mController;

	public QuestRemoveDepent( EditController ctrl) {
		mController = ctrl;
	}

	@Override
	public Object forQuest( FQuest quest, FQuest req) {
		remove( quest.mRequirements, req);
		remove( quest.mOptionLinks, req);
		mController.fireChanged( quest);
		return null;
	}

	private void remove( Vector<FQuest> arr, FQuest req) {
		for (int i = 0; i < arr.size(); ++i) {
			FQuest q = arr.get( i);
			if (Utils.equals( q, req)) {
				arr.setElementAt( null, i);
			}
		}
	}
}
