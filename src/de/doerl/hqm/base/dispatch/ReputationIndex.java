package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.utils.Utils;

public class ReputationIndex extends AHQMWorker<Boolean, Object> {
	private int mResult = -1;
	private FReputation mRep;

	private ReputationIndex( FReputation rep) {
		mRep = rep;
	}

	public static int get( FReputation rep) {
		ReputationIndex worker = new ReputationIndex( rep);
		rep.mParentCategory.forEachMember( worker, null);
		return worker.mResult;
	}

	@Override
	public Boolean forReputation( FReputation rep, Object p) {
		++mResult;
		return Utils.equals( rep, mRep) ? Boolean.TRUE : null;
	}
}
