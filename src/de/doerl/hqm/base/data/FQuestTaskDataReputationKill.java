package de.doerl.hqm.base.data;

import de.doerl.hqm.base.FQuestTaskReputationKill;
import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestTaskDataReputationKill extends AQuestTaskData {
	public final FQuestTaskReputationKill mTask;
	public int mKills;

	public FQuestTaskDataReputationKill( FQuestData parentQuest, FQuestTaskReputationKill task) {
		super( parentQuest);
		mTask = task;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forDataTaskReputationKill( this, p);
	}
}
