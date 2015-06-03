package de.doerl.hqm.ui;

import java.awt.event.ActionEvent;

import de.doerl.hqm.base.FHqm;

class NewAction extends ABundleAction {
	private static final long serialVersionUID = -44344993759630964L;
	private EditFrame mFrame;

	public NewAction( EditFrame frame) {
		super( "hqm.new");
		mFrame = frame;
	}

	public void actionPerformed( ActionEvent e) {
		FHqm hqm = new FHqm( null);
		mFrame.getModel().loadHQM( hqm);
		mFrame.showHqm( hqm);
	}
}
