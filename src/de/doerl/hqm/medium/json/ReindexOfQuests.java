package de.doerl.hqm.medium.json;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOfQuest;

class ReindexOfQuests extends AHQMWorker<Object, Object> {
	private int mMax;

	private ReindexOfQuests( int max) {
		mMax = max;
	}

	public static int get( FHqm hqm) {
		return get( hqm.mQuestSetCat);
	}

	public static int get( FQuestSetCat cat) {
		int old = MaxIdOfQuest.get( cat);
		ReindexOfQuests worker = new ReindexOfQuests( old);
		cat.forEachMember( worker, null);
		return worker.mMax;
	}

	@Override
	public Object forQuest( FQuest quest, Object p) {
		if (quest.getID() < 0) {
			quest.setID( FQuest.toIdent( ++mMax));
		}
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		set.forEachQuest( this, null);
		return null;
	}
}
