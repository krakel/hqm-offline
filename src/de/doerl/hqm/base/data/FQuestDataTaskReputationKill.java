package de.doerl.hqm.base.data;

import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestDataTaskReputationKill extends AQuestDataTask {
	public int mKills;

	public FQuestDataTaskReputationKill( FQuestData parentQuest) {
		super( parentQuest);
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forDataTaskReputationKill( this, p);
	}
}
