package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationCat;

public class SettingOfIdx extends AHQMWorker<FReputation, Object> {
	private int mIndex;

	private SettingOfIdx( int index) {
		mIndex = index;
	}

	public static FReputation get( FHqm hqm, int idx) {
		return get( hqm.mReputationCat, idx);
	}

	public static FReputation get( FQuestTaskReputationTarget task, int idx) {
		return get( task.mParentQuest.mParentHQM.mReputationCat, idx);
	}

	public static FReputation get( FReputationCat set, int idx) {
		SettingOfIdx worker = new SettingOfIdx( idx);
		return set.forEachMember( worker, null);
	}

	@Override
	public FReputation forReputation( FReputation rep, Object p) {
		if (mIndex == 0) {
			return rep;
		}
		--mIndex;
		return null;
	}
}
