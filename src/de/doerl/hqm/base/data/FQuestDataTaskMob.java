package de.doerl.hqm.base.data;

import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestDataTaskMob extends AQuestDataTask {
	public int[] mKilled;

	public FQuestDataTaskMob( FQuestData parentQuest) {
		super( parentQuest);
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forDataTaskMob( this, p);
	}
}
