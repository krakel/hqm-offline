package de.doerl.hqm.base;

import java.util.Vector;

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
	public Vector<FReputation> asVector() {
		return new Vector<>( mArr);
	}

	@Override
	public FReputation createMember() {
		FReputation reward = new FReputation( this);
		addMember( reward);
		return reward;
	}

	public FReputation createReputation( int id) {
		FReputation reward = new FReputation( this, id);
		addMember( reward);
		return reward;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.REPUTATION_CAT;
	}

	@Override
	public String getNodeName() {
		return "Reputations";
	}
}
