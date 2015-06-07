package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationCat;

public class ReputationOfIdx extends AHQMWorker<FReputation, Object> {
	private int mIndex;

	private ReputationOfIdx( int index) {
		mIndex = index;
	}

	public static FReputation get( FHqm hqm, int idx) {
		return get( hqm.mReputationCat, idx);
	}

	public static FReputation get( FQuestTaskReputationTarget task, int idx) {
		return get( task.mParentQuest.mParentHQM.mReputationCat, idx);
	}

	public static FReputation get( FReputationCat set, int idx) {
		ReputationOfIdx worker = new ReputationOfIdx( idx);
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