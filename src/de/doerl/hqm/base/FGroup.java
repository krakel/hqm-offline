package de.doerl.hqm.base;

import java.util.Vector;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FGroup extends ANamed implements IElement {
	public final FGroupTier mParentTier;
	public Integer mLimit;
	public Vector<FItemStack> mStacks = new Vector<>();

	public FGroup( FGroupTier parent, String name) {
		super( name);
		mParentTier = parent;
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
	public FGroupTier getParent() {
		return mParentTier;
	}

	public boolean isFirst() {
		return ABase.isFirst( mParentTier.mGroups, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentTier.mGroups, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentTier.mGroups, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentTier.mGroups, this);
	}

	public void remove() {
		ABase.remove( mParentTier.mGroups, this);
	}
}
