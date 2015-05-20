package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IParameter;
import de.doerl.hqm.quest.ParameterType;

public abstract class AParameter implements IParameter {
	public final ABase mParent;

	AParameter( ABase parent) {
		mParent = parent;
	}

	public abstract ParameterType getElementTyp();

	public ABase getParent() {
		return mParent;
	}
}
