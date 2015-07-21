package de.doerl.hqm.base.data;

import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestDataTaskItems extends AQuestDataTask {
	public int[] mProgress;

	public FQuestDataTaskItems( FQuestData parentQuest) {
		super( parentQuest);
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forDataTaskItems( this, p);
	}
}
