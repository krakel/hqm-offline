package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FReputationSetting extends ABase {
	public final FQuestTaskReputationTarget mParentTask;
	public final FParameterInt mRepID = new FParameterInt( this, "RepID");
	public final FParameterInteger mLowerID = new FParameterInteger( this, "LowerID");
	public final FParameterInteger mUpperID = new FParameterInteger( this, "UpperID");
	public final FParameterBoolean mInverted = new FParameterBoolean( this, "Inverted");

	public FReputationSetting( FQuestTaskReputationTarget parent) {
		mParentTask = parent;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forSetting( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.REPUTATION_SETTING;
	}

	@Override
	public FQuestTaskReputationTarget getParent() {
		return mParentTask;
	}
}
