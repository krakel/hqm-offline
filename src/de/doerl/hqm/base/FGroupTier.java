package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FGroupTier extends AMember<FGroupTier> {
	public final FGroupTierCat mParentCategory;
	public final FParameterInt mColorID = new FParameterInt( this);
	public final FParameterIntegerArr mWeights = new FParameterIntegerArr( this);

	public FGroupTier( FGroupTierCat parent, String name) {
		super( name);
		mParentCategory = parent;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forGroupTier( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.GROUP_TIER;
	}

	@Override
	public ACategory<FGroupTier> getHierarchy() {
		return mParentCategory;
	}

	@Override
	public ACategory<FGroupTier> getParent() {
		return mParentCategory;
	}
}
