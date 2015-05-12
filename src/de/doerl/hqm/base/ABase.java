package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IBase;
import de.doerl.hqm.quest.ElementTyp;

public abstract class ABase implements IBase {
	ABase() {
	}

	public abstract ElementTyp getElementTyp();

	public abstract ABase getParent();
}
