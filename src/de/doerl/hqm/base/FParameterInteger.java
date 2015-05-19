package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IParameterWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FParameterInteger extends AParameter {
	public Integer mValue;

	public FParameterInteger( ABase parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IParameterWorker<T, U> w, U p) {
		return w.forParameterInteger( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.PARAMETER_INTEGER;
	}

	@Override
	public String toString() {
		return Integer.toString( mValue);
	}
}
