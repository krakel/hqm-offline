package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.TaskTyp;

public class FQuestTaskReputationKill extends AQuestTask {
	public final FParameterInt mKills = new FParameterInt( this);

	public FQuestTaskReputationKill( FQuest parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskReputationKill( this, p);
	}

	@Override
	public TaskTyp getTaskTyp() {
		return TaskTyp.TASK_REPUTATION_KILL;
	}
}
