package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FReputationBar extends ABase implements IElement {
	public final FQuestSet mParentSet;
	public int mValue;

	FReputationBar( FQuestSet parent) {
		mParentSet = parent;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forReputationBar( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.REPUTATION_BAR;
	}

	@Override
	public FHqm getHqm() {
		return mParentSet.getHqm();
	}

	@Override
	public ABase getParent() {
		return mParentSet;
	}

	@Override
	public boolean isFirst() {
		return ABase.isFirst( mParentSet.mBars, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentSet.mBars, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentSet.mBars, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentSet.mBars, this);
	}

	@Override
	public void remove() {
		ABase.remove( mParentSet.mBars, this);
	}
}
