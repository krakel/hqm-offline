package de.doerl.hqm.base;

import de.doerl.hqm.quest.ItemPrecision;

public abstract class ARequirement extends ABase {
	public final AQuestTaskItems mParentTask;

	ARequirement( AQuestTaskItems parent) {
		mParentTask = parent;
	}

	public abstract int getCount();

	@Override
	public AQuestTaskItems getHierarchy() {
		return mParentTask;
	}

	@Override
	public AQuestTaskItems getParent() {
		return mParentTask;
	}

	public abstract ItemPrecision getPrecision();

	public abstract FParameterStack getStack();
}
