package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FQuestTaskDeath extends AQuestTask {
	public final FParameterInt mDeaths = new FParameterInt( this, "Death");

	public FQuestTaskDeath( FQuest parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskDeath( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_TASK_DEATH;
	}
}
