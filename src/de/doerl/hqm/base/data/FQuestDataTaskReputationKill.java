package de.doerl.hqm.base.data;

import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestDataTaskReputationKill extends AQuestDataTask {
	public FQuestDataTaskReputationKill( FQuestData parentQuest) {
		super( parentQuest);
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return null;
	}
}
