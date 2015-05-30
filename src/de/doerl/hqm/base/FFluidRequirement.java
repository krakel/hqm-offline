package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.ItemPrecision;

public final class FFluidRequirement extends ARequirement {
	public FFluidStack mStack;

	public FFluidRequirement( AQuestTaskItems parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forFluidRequirement( this, p);
	}

	@Override
	public int getCount() {
		return mStack != null ? mStack.getCount() : 0;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.FLUID_REQUIREMENT;
	}

	@Override
	public ItemPrecision getPrecision() {
		return ItemPrecision.PRECISE;
	}

	@Override
	public FFluidStack getStack() {
		return mStack;
	}
}
