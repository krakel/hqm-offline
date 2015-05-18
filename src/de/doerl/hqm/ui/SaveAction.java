package de.doerl.hqm.ui;

import java.awt.event.ActionEvent;

import de.doerl.hqm.ui.ADialog.DialogResult;
import de.doerl.hqm.utils.ResourceManager;

@SuppressWarnings( "nls")
public class SaveAction extends ABundleAction {
	private static final long serialVersionUID = 7582625921614808973L;
	private EditFrame mFrame;

	public SaveAction( EditFrame frame) {
		super( "hqm.save", ResourceManager.RESOURCE);
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
