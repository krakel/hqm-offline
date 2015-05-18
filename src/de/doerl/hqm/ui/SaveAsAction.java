package de.doerl.hqm.ui;

import java.awt.event.ActionEvent;

import de.doerl.hqm.ui.ADialog.DialogResult;
import de.doerl.hqm.utils.ResourceManager;

@SuppressWarnings( "nls")
public class SaveAsAction extends ABundleAction {
	private static final long serialVersionUID = 6577332345420018356L;
	private EditFrame mFrame;

	public SaveAsAction( EditFrame frame) {
		super( "hqm.saveAs", ResourceManager.RESOURCE);
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
