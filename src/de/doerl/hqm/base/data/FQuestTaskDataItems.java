package de.doerl.hqm.base.data;

import java.util.Vector;

import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestTaskDataItems extends AQuestTaskData {
	public final AQuestTaskItems mTask;
	public final Vector<Integer> mProgress = new Vector<>();

	public FQuestTaskDataItems( FQuestData parentQuest, AQuestTaskItems task) {
		super( parentQuest);
		mTask = task;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forDataTaskItems( this, p);
	}
}
