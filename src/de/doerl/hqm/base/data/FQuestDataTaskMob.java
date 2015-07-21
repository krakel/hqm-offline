package de.doerl.hqm.base.data;

import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestDataTaskMob extends AQuestDataTask {
	public FQuestDataTaskMob( FQuestData parentQuest) {
		super( parentQuest);
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return null;
	}
}
