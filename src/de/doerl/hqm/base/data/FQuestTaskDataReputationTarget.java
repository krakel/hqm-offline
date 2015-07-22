package de.doerl.hqm.base.data;

import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestTaskDataReputationTarget extends AQuestTaskData {
	public final FQuestTaskReputationTarget mTask;

	public FQuestTaskDataReputationTarget( FQuestData parentQuest, FQuestTaskReputationTarget task) {
		super( parentQuest);
		mTask = task;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forDataTaskReputationTarget( this, p);
	}
}
