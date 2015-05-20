package de.doerl.hqm.view;

import java.util.Vector;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.Utils;

class QuestRemoveDepent extends AHQMWorker<Object, FQuest> {
	private static final QuestRemoveDepent WORKER = new QuestRemoveDepent();

	private QuestRemoveDepent() {
	}

	public static void get( FQuest req) {
		req.mParentHQM.forEachQuest( WORKER, req);
	}

	@Override
	public Object forQuest( FQuest quest, FQuest req) {
		remove( quest.mRequirements, req);
		remove( quest.mOptionLinks, req);
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