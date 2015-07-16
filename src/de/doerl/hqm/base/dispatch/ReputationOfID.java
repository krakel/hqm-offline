package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationCat;

public class ReputationOfID extends AHQMWorker<FReputation, Integer> {
	private static final ReputationOfID WORKER = new ReputationOfID();

	private ReputationOfID() {
	}

	public static FReputation get( FHqm hqm, int idx) {
		return get( hqm.mReputationCat, idx);
	}

	public static FReputation get( FQuestTaskReputationTarget task, int idx) {
		return get( task.getHqm().mReputationCat, idx);
	}

	public static FReputation get( FReputationCat set, int id) {
		return set.forEachMember( WORKER, id);
	}

	@Override
	public FReputation forReputation( FReputation rep, Integer id) {
		return rep.getID() == id.intValue() ? rep : null;
	}
}
