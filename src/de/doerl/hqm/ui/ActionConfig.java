package de.doerl.hqm.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;

class ActionConfig extends ABundleAction {
	private static final long serialVersionUID = 8243516768017016082L;
	private Window mParent;

	public ActionConfig( Window parent) {
		super( "hqm.help.config");
		mParent = parent;
	}

	@Override
	public void actionPerformed( ActionEvent evt) {
		ConfigDialog.update( mParent);
	}
}
