/*******************************************************************************
 * (c) Copyright IBM Corporation 2016. All Rights Reserved.
 *
 *******************************************************************************/
package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.AQuestTaskReputation;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.utils.Utils;

public class ReputationOfUUID extends AHQMWorker<FReputation, String> {
	private static final ReputationOfUUID WORKER = new ReputationOfUUID();

	private ReputationOfUUID() {
	}

	public static FReputation get( AQuestTaskReputation task, String uuid) {
		return get( task.getHqm().mReputationCat, uuid);
	}

	public static FReputation get( FHqm hqm, String uuid) {
		return get( hqm.mReputationCat, uuid);
	}

	public static FReputation get( FReputationCat set, String uuid) {
		return set.forEachMember( WORKER, uuid);
	}

	@Override
	public FReputation forReputation( FReputation rep, String uuid) {
		return Utils.equals( rep.getUUID(), uuid) ? rep : null;
	}
}
