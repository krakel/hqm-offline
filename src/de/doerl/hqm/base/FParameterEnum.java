package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public class FParameterEnum<E extends Enum<?>> extends AParameter {
	public E mValue;

	public FParameterEnum( ABase parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forParameterEnum( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.PARAMETER_STRING;
	}

	@Override
	public String toString() {
		return String.valueOf( mValue);
	}
}
