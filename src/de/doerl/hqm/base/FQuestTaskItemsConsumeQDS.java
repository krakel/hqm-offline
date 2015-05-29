package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.TaskTyp;

public final class FQuestTaskItemsConsumeQDS extends AQuestTaskItems {
	public FQuestTaskItemsConsumeQDS( FQuest parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskItemsConsumeQDS( this, p);
	}

	@Override
	public TaskTyp getTaskTyp() {
		return TaskTyp.TASK_ITEMS_CONSUME_QDS;
	}
}
