package de.doerl.hqm.base.data;

import java.util.Vector;

import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestTaskDataLocation extends AQuestTaskData {
	public final FQuestTaskLocation mTask;
	public final Vector<Boolean> mVisited = new Vector<>();

	public FQuestTaskDataLocation( FQuestData parentQuest, FQuestTaskLocation task) {
		super( parentQuest);
		mTask = task;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forDataTaskLocation( this, p);
	}
}
