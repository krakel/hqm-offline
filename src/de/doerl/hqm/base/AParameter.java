package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IParameter;
import de.doerl.hqm.quest.ElementTyp;

public abstract class AParameter implements IParameter {
	public final ABase mParent;

	AParameter( ABase parent) {
		mParent = parent;
	}

	public abstract ElementTyp getElementTyp();

	public ABase getParent() {
		return mParent;
	}
}
