package de.doerl.hqm.base;

import java.util.Vector;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FGroup extends AMember<FGroup> {
	public final FGroupCat mParentCategory;
	public FGroupTier mTier;
	public Integer mLimit;
	public Vector<FItemStack> mStacks = new Vector<>();

	public FGroup( FGroupCat parent, String name) {
		super( name);
		mParentCategory = parent;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forGroup( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.GROUP;
	}

	@Override
	public ACategory<FGroup> getHierarchy() {
		return mParentCategory;
	}

	@Override
	public ACategory<FGroup> getParent() {
		return mParentCategory;
	}
}
