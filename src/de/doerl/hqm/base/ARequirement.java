package de.doerl.hqm.base;

import de.doerl.hqm.quest.ItemPrecision;

public abstract class ARequirement extends ABase {
	public final AQuestTaskItems mParentTask;
	public final FParameterEnum<ItemPrecision> mPrecision = new FParameterEnum<ItemPrecision>( this);

	ARequirement( AQuestTaskItems parent) {
		mParentTask = parent;
	}

	@Override
	public AQuestTaskItems getHierarchy() {
		return mParentTask;
	}

	@Override
	public AQuestTaskItems getParent() {
		return mParentTask;
	}

	public abstract FParameterStack getStack();
}
