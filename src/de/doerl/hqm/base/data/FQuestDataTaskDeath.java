package de.doerl.hqm.base.data;

import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestDataTaskDeath extends AQuestDataTask {
	public int mDeaths;

	public FQuestDataTaskDeath( FQuestData parentQuest) {
		super( parentQuest);
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forDataTaskDeath( this, p);
	}
}
