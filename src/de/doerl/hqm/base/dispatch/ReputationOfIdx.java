package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputations;

public class ReputationOfIdx extends AHQMWorker<FReputation, Object> {
	private int mIndex;

	private ReputationOfIdx( int index) {
		mIndex = index;
	}

	public static FReputation get( FHqm hqm, int idx) {
		return get( hqm.mRepSets, idx);
	}

	public static FReputation get( FQuestTaskReputationTarget task, int idx) {
		return get( task.mParentQuest.mParentHQM.mRepSets, idx);
	}

	public static FReputation get( FReputations set, int idx) {
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
