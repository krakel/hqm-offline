package de.doerl.hqm.base.data;

import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestDataTaskReputationTarget extends AQuestDataTask {
	public FQuestDataTaskReputationTarget( FQuestData parentQuest) {
		super( parentQuest);
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return null;
	}
}
