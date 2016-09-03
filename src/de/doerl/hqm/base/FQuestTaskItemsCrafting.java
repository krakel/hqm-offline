package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.TaskTyp;

public final class FQuestTaskItemsCrafting extends AQuestTaskItems {
	FQuestTaskItemsCrafting( FQuest parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskItemsCrafting( this, p);
	}

	@Override
	public TaskTyp getTaskTyp() {
		return TaskTyp.CRAFT;
	}
}
