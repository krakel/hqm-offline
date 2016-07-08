package de.doerl.hqm.base.data;

import java.util.ArrayList;

import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestTaskDataItems extends AQuestTaskData {
	public final AQuestTaskItems mTask;
	public final ArrayList<Integer> mProgress = new ArrayList<>();

	public FQuestTaskDataItems( FQuestData parentQuest, AQuestTaskItems task) {
		super( parentQuest);
		mTask = task;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forDataTaskItems( this, p);
	}
}
