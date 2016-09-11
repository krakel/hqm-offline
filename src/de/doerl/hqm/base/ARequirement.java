package de.doerl.hqm.base;

import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.utils.nbt.FCompound;

public abstract class ARequirement extends ABase implements IElement {
	public final AQuestTaskItems mParentTask;
	public int mAmount;

	ARequirement( AQuestTaskItems parent) {
		mParentTask = parent;
	}

	@Override
	public FHqm getHqm() {
		return mParentTask.getHqm();
	}

	public abstract FCompound getNBT();

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
}
