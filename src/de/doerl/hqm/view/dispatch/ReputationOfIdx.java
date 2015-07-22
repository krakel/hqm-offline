package de.doerl.hqm.view.dispatch;

import de.doerl.hqm.base.AQuestTaskReputation;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;

public class ReputationOfIdx extends AHQMWorker<FReputation, Object> {
	private int mIndex;

	private ReputationOfIdx( int index) {
		mIndex = index;
	}

	public static FReputation get( AQuestTaskReputation task, int idx) {
		return get( task.getHqm().mReputationCat, idx);
	}

	public static FReputation get( FHqm hqm, int idx) {
		return get( hqm.mReputationCat, idx);
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
