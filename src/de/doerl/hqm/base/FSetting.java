package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FSetting extends ABase implements IElement {
	public final AQuestTaskReputation mParentTask;
	public FReputation mRep;
	public FMarker mLower;
	public FMarker mUpper;
	public boolean mInverted;

	public FSetting( AQuestTaskReputation parent) {
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
	public FHqm getHqm() {
		return mParentTask.getHqm();
	}

	@Override
	public AQuestTaskReputation getParent() {
		return mParentTask;
	}

	@Override
	public boolean isFirst() {
		return ABase.isFirst( mParentTask.mSettings, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentTask.mSettings, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentTask.mSettings, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentTask.mSettings, this);
	}

	@Override
	public void remove() {
		ABase.remove( mParentTask.mSettings, this);
	}
}
