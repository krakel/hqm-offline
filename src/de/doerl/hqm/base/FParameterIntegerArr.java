package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IParameterWorker;
import de.doerl.hqm.quest.ParameterType;
import de.doerl.hqm.utils.Helper;

public final class FParameterIntegerArr extends AParameter {
	public int[] mValue;

	public FParameterIntegerArr( ABase parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IParameterWorker<T, U> w, U p) {
		return w.forParameterIntegerArr( this, p);
	}

	@Override
	public ParameterType getElementTyp() {
		return ParameterType.PARAMETER_INT_ARR;
	}

	@Override
	public String toString() {
		return mValue != null ? Helper.toString( mValue) : "null";
	}
}
