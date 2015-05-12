package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FFluidRequirement extends ARequirement {
	private FParameterStack mStack = new FParameterStack( this, "fluid");

	public FFluidRequirement( AQuestTaskItems parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forFluidRequirement( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.FLUID_REQUIREMENT;
	}

	@Override
	public FParameterStack getStack() {
		return mStack;
	}
}
