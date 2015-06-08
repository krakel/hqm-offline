package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.utils.Utils;

public class IndexOfQuest extends AHQMWorker<Boolean, Object> {
	private int mResult = -1;
	private ABase mBase;

	private IndexOfQuest( ABase set) {
		mBase = set;
	}

	public static int get( FQuest quest) {
		IndexOfQuest worker = new IndexOfQuest( quest);
		quest.getParent().mParentCategory.forEachMember( worker, null);
		return worker.mResult;
	}

	@Override
	public Boolean forQuest( FQuest quest, Object p) {
		++mResult;
		return Utils.equals( quest, mBase) ? Boolean.TRUE : null;
	}

	@Override
	public Boolean forQuestSet( FQuestSet set, Object p) {
		return set.forEachQuest( this, p);
	}
}
