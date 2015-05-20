package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IParameterWorker;
import de.doerl.hqm.quest.ParameterType;

public final class FParameterInt extends AParameter implements Comparable<FParameterInt> {
	public int mValue;

	public FParameterInt( ABase parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IParameterWorker<T, U> w, U p) {
		return w.forParameterInt( this, p);
	}

	public int compareTo( FParameterInt other) {
		return Integer.compare( mValue, other.mValue);
	}

	@Override
	public ParameterType getElementTyp() {
		return ParameterType.PARAMETER_INT;
	}

	@Override
	public String toString() {
		return Integer.toString( mValue);
	}
}
