package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FParameterInt extends AParameter implements Comparable<FParameterInt> {
	public int mValue;

	public FParameterInt( ABase parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forParameterInt( this, p);
	}

	public int compareTo( FParameterInt other) {
		return Integer.compare( mValue, other.mValue);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.PARAMETER_INT;
	}

	@Override
	public String toString() {
		return Integer.toString( mValue);
	}
}
