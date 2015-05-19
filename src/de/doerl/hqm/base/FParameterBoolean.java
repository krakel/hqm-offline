package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FParameterBoolean extends AParameter {
	public boolean mValue;

	public FParameterBoolean( ABase parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forParameterBoolean( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.PARAMETER_BOOLEAN;
	}

	@Override
	public String toString() {
		return String.valueOf( mValue);
	}
}
