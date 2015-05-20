package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IParameterWorker;
import de.doerl.hqm.quest.ParameterType;

public final class FParameterBoolean extends AParameter {
	public boolean mValue;

	public FParameterBoolean( ABase parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IParameterWorker<T, U> w, U p) {
		return w.forParameterBoolean( this, p);
	}

	@Override
	public ParameterType getElementTyp() {
		return ParameterType.PARAMETER_BOOLEAN;
	}

	@Override
	public String toString() {
		return String.valueOf( mValue);
	}
}
