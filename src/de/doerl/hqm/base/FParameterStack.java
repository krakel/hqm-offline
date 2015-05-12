package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FParameterStack extends AParameter {
	public AStack mValue;

	public FParameterStack( ABase parent, String name) {
		super( parent, name);
	}

	public FParameterStack( ABase parent, String name, AStack value) {
		super( parent, name);
		mValue = value;
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forParameterStack( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.PARAMETER_STACK;
	}

	@Override
	public String toString() {
		return String.valueOf( mValue);
	}
}
