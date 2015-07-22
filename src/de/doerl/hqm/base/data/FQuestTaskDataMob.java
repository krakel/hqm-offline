package de.doerl.hqm.base.data;

import java.util.Vector;

import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestTaskDataMob extends AQuestTaskData {
	public final FQuestTaskMob mTask;
	public final Vector<Integer> mKilled = new Vector<>();

	public FQuestTaskDataMob( FQuestData parentQuest, FQuestTaskMob task) {
		super( parentQuest);
		mTask = task;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forDataTaskMob( this, p);
	}
}
