package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.utils.Helper;

public final class FParameterIntegerArr extends AParameter {
	public int[] mValue;

	public FParameterIntegerArr( ABase parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forParameterIntegerArr( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.PARAMETER_INT_ARR;
	}

	@Override
	public String toString() {
		return mValue != null ? Helper.toString( mValue) : "null";
	}
}
