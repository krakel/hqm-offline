package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FReputationReward extends ABase implements IElement {
	public final FQuest mParentQuest;
	public FReputation mRep;
	public int mValue;

	public FReputationReward( FQuest parent) {
		mParentQuest = parent;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forReputationReward( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.REPUTATION_REWARD;
	}

	@Override
	public FHqm getHqm() {
		return mParentQuest.getHqm();
	}

	@Override
	public FQuest getParent() {
		return mParentQuest;
	}

	@Override
	public boolean isFirst() {
		return ABase.isFirst( mParentQuest.mRepRewards, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentQuest.mRepRewards, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentQuest.mRepRewards, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentQuest.mRepRewards, this);
	}

	@Override
	public void remove() {
		ABase.remove( mParentQuest.mRepRewards, this);
	}
}
