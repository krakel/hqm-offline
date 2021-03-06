package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.TaskTyp;

public final class FQuestTaskItemsConsume extends AQuestTaskItems {
	FQuestTaskItemsConsume( FQuest parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskItemsConsume( this, p);
	}

	@Override
	public TaskTyp getTaskTyp() {
		return TaskTyp.CONSUME;
	}
}
