package de.doerl.hqm.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;

public class ActionAbout extends ABundleAction {
	private static final long serialVersionUID = -7364269566865078131L;
	private Window mParent;

	public ActionAbout( Window parent) {
		super( "hqm.help.about");
		mParent = parent;
	}

	public void actionPerformed( ActionEvent event) {
		if (JFXHiddenApplication.isIsEnabled()) {
			AboutDialogFx.update( mParent);
		}
		else {
			AboutDialog.update( mParent);
		}
	}
}
