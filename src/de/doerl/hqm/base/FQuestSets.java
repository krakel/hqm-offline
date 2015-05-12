package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FQuestSets extends ASet<FQuestSet> {
	FQuestSets( FHqm parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forQuestSets( this, p);
	}

	@Override
	public FQuestSet createMember( String name) {
		FQuestSet reward = new FQuestSet( this, name);
		addMember( reward);
		return reward;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_SETS;
	}
}
