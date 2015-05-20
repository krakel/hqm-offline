package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FReputationCat extends ACategory<FReputation> {
	FReputationCat( FHqm parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forReputationCat( this, p);
	}

	@Override
	public FReputation createMember( String name) {
		FReputation reward = new FReputation( this, name);
		addMember( reward);
		return reward;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.REPUTATION_CAT;
	}
}
