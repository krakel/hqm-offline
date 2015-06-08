package de.doerl.hqm.controller;

import java.util.Vector;

import de.doerl.hqm.Tuple2;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.Utils;

class QuestRemoveDepent extends AHQMWorker<Object, Tuple2<FQuest, EditController>> {
	private static final QuestRemoveDepent WORKER = new QuestRemoveDepent();

	private QuestRemoveDepent() {
	}

	public static void get( FQuest quest, EditController ctrl) {
		quest.getParent().mParentCategory.forEachMember( WORKER, Tuple2.apply( quest, ctrl));
	}

	@Override
	public Object forQuest( FQuest quest, Tuple2<FQuest, EditController> p) {
		remove( quest.mRequirements, p._1);
		remove( quest.mOptionLinks, p._1);
		p._2.fireChanged( quest);
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Tuple2<FQuest, EditController> p) {
		set.forEachQuest( this, p);
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
