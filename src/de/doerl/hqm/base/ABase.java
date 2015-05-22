package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IBase;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.utils.ToString;

public abstract class ABase implements IBase {
	ABase() {
	}

	public abstract ElementTyp getElementTyp();

	public abstract ABase getHierarchy();

	public abstract ABase getParent();

	@Override
	public String toString() {
		return ToString.clsName( this);
	}
}
