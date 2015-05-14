package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FReward extends ABase {
	public final FQuest mParentQuest;
	public final FParameterInt mRepID = new FParameterInt( this, "ReputationID");
	public final FParameterInt mValue = new FParameterInt( this, "Value");

	public FReward( FQuest parent) {
		mParentQuest = parent;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forReward( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.REPUTATION_REWARD;
	}

	@Override
	public FQuest getParent() {
		return mParentQuest;
	}
}
