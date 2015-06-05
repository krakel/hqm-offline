package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FMob extends ANamed implements IElement {
	public final FQuestTaskMob mParentTask;
	public AStack mIcon;
	public String mMob;
	public int mKills;
	public boolean mExact;

	public FMob( FQuestTaskMob parent, AStack icon, String name) {
		super( name);
		mParentTask = parent;
		mIcon = icon;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forMob( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.MOB;
	}

	@Override
	public FQuestTaskMob getHierarchy() {
		return mParentTask;
	}

	@Override
	public FQuestTaskMob getParent() {
		return mParentTask;
	}

	@Override
	public boolean isFirst() {
		return ABase.isFirst( mParentTask.mMobs, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentTask.mMobs, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentTask.mMobs, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentTask.mMobs, this);
	}

	@Override
	public void remove() {
		ABase.remove( mParentTask.mMobs, this);
	}
}
