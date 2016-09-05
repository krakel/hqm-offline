package de.doerl.hqm.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;

class ActionNEI extends ABundleAction {
	private static final long serialVersionUID = -5986402650215920764L;
	private Window mParent;

	public ActionNEI( Window parent) {
		super( "hqm.help.nei");
		mParent = parent;
	}

	@Override
	public void actionPerformed( ActionEvent evt) {
		ConfigNEI.update( mParent);
	}
}
