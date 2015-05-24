package de.doerl.hqm.ui;

import java.awt.event.ActionEvent;

import de.doerl.hqm.ui.ADialog.DialogResult;

class NewAction extends ABundleAction {
	private static final long serialVersionUID = -44344993759630964L;
	private EditFrame mFrame;

	public NewAction( EditFrame frame) {
		super( "hqm.new");
		mFrame = frame;
	}

	public void actionPerformed( ActionEvent e) {
		DialogResult result = mFrame.getModifiedResult();
		switch (result) {
			case APPROVE:
				mFrame.saveHQM();
			case NO:
//				FHqm hqm = new FHqm( null);
				EditFrame.createNew();
			default:
		}
	}
}
