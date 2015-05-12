package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FReputationSetting extends ABase {
	public final AQuestTaskReputation mParentTask;
	public final FParameterInt mRepID = new FParameterInt( this, "RepID");
	public final FParameterInteger mLowerID = new FParameterInteger( this, "LowerID");
	public final FParameterInteger mUpperID = new FParameterInteger( this, "UpperID");
	public final FParameterBoolean mInverted = new FParameterBoolean( this, "Inverted");

	public FReputationSetting( AQuestTaskReputation parent) {
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
	public AQuestTaskReputation getParent() {
		return mParentTask;
	}
}
