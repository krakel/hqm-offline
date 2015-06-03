package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FSetting extends ABase {
	public final FQuestTaskReputationTarget mParentTask;
	public FReputation mRep;
	public FMarker mLower;
	public FMarker mUpper;
	public boolean mInverted;

	public FSetting( FQuestTaskReputationTarget parent) {
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
	public FQuestTaskReputationTarget getHierarchy() {
		return mParentTask;
	}

	public FHqm getHqm() {
		return mParentTask.getHqm();
	}

	@Override
	public FQuestTaskReputationTarget getParent() {
		return mParentTask;
	}
}
