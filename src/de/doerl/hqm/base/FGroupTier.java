package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FGroupTier extends AMember {
	public final FGroupTierCat mParentCategory;
	public int mColorID;
	public int[] mWeights;

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
	public FGroupTierCat getParent() {
		return mParentCategory;
	}

	public boolean isFirst() {
		return ABase.isFirst( mParentCategory.mArr, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentCategory.mArr, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentCategory.mArr, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentCategory.mArr, this);
	}

	public void remove() {
		ABase.remove( mParentCategory.mArr, this);
	}
}
