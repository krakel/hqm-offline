package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.TaskTyp;

public final class FQuestTaskReputationKill extends AQuestTaskReputation {
	public int mKills;

	FQuestTaskReputationKill( FQuest parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskReputationKill( this, p);
	}

	@Override
	public TaskTyp getTaskTyp() {
		return TaskTyp.REPUTATION_KILL;
	}
}
