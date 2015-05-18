package de.doerl.hqm.ui;

import java.awt.event.ActionEvent;

import de.doerl.hqm.ui.ADialog.DialogResult;
import de.doerl.hqm.utils.ResourceManager;

@SuppressWarnings( "nls")
public class OpenAction extends ABundleAction {
	private static final long serialVersionUID = -8745034689875059841L;
	private EditFrame mFrame;

	public OpenAction( EditFrame frame) {
		super( "hqm.open", ResourceManager.RESOURCE);
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
