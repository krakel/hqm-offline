package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FGroupTiers extends ACategory<FGroupTier> {
	FGroupTiers( FHqm parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forGroupTiers( this, p);
	}

	@Override
	public FGroupTier createMember( String name) {
		FGroupTier reward = new FGroupTier( this, name);
		addMember( reward);
		return reward;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.GROUP_TIERS;
	}
}
