package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.TaskTyp;

public final class FQuestTaskItemsDetect extends AQuestTaskItems {
	FQuestTaskItemsDetect( FQuest parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskItemsDetect( this, p);
	}

	@Override
	public TaskTyp getTaskTyp() {
		return TaskTyp.DETECT;
	}
}
