package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IParameterWorker;
import de.doerl.hqm.quest.ParameterType;

public final class FParameterString extends AParameter {
	public String mValue;

	public FParameterString( ABase parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IParameterWorker<T, U> w, U p) {
		return w.forParameterString( this, p);
	}

	@Override
	public ParameterType getElementTyp() {
		return ParameterType.PARAMETER_STRING;
	}

	@Override
	public String toString() {
		return String.valueOf( mValue);
	}
}
