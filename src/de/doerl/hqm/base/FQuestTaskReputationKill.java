package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public class FQuestTaskReputationKill extends AQuestTaskReputation {
	public final FParameterInt mKills = new FParameterInt( this, "Kills");

	public FQuestTaskReputationKill( FQuest parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskReputationKill( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_TASK_REPUTATION_KILL;
	}
}
