package de.doerl.hqm.base.data;

import de.doerl.hqm.base.FQuestTaskDeath;
import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestTaskDataDeath extends AQuestTaskData {
	public final FQuestTaskDeath mTask;
	public int mDeaths;

	public FQuestTaskDataDeath( FQuestData parentQuest, FQuestTaskDeath task) {
		super( parentQuest);
		mTask = task;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forDataTaskDeath( this, p);
	}
}
