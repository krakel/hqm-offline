package de.doerl.hqm.base.data;

import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestDataTaskLocation extends AQuestDataTask {
	public FQuestDataTaskLocation( FQuestData parentQuest) {
		super( parentQuest);
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return null;
	}
}
