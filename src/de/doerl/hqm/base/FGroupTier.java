package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FGroupTier extends AMember<FGroupTier> {
	public final FParameterInt mColorID = new FParameterInt( this, "Color");
	public final FParameterIntegerArr mWeights = new FParameterIntegerArr( this, "Weights");

	public FGroupTier( FGroupTiers parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forGroupTier( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.GROUP_TIER;
	}
}
