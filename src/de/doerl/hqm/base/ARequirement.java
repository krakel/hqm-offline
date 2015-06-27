package de.doerl.hqm.base;

import de.doerl.hqm.quest.ItemPrecision;

public abstract class ARequirement extends ABase implements IElement {
	public final AQuestTaskItems mParentTask;

	ARequirement( AQuestTaskItems parent) {
		mParentTask = parent;
	}

	public abstract int getCount();

	@Override
	public FHqm getHqm() {
		return mParentTask.getHqm();
	}

	@Override
	public AQuestTaskItems getParent() {
		return mParentTask;
	}

	public abstract ItemPrecision getPrecision();

	public abstract AStack getStack();

	@Override
	public boolean isFirst() {
		return ABase.isFirst( mParentTask.mRequirements, this);
	}

	@Override
	public boolean isInformation() {
		return false;
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentTask.mRequirements, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentTask.mRequirements, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentTask.mRequirements, this);
	}

	@Override
	public void remove() {
		ABase.remove( mParentTask.mRequirements, this);
	}

	@Override
	public void setInformation( boolean information) {
	}
}
