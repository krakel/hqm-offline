package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.TaskTyp;

public final class FQuestTaskItemsDetect extends AQuestTaskItems {
	public FQuestTaskItemsDetect( FQuest parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskItemsDetect( this, p);
	}

	@Override
	public TaskTyp getTaskTyp() {
		return TaskTyp.TASK_ITEMS_DETECT;
	}
}
