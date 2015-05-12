package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FItemRequirement extends ARequirement {
	private FParameterStack mStack = new FParameterStack( this, "item");
	private FParameterInt mRequired = new FParameterInt( this, "Required");

	public FItemRequirement( AQuestTaskItems parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forItemRequirement( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.ITEM_REQUIREMENT;
	}

	public FParameterInt getRequired() {
		return mRequired;
	}

	@Override
	public FParameterStack getStack() {
		return mStack;
	}
}
