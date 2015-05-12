package de.doerl.hqm.ui;

import java.awt.event.ActionEvent;

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
		int result = mFrame.getModifiedResult();
		switch (result) {
			case ADialog.APPROVE:
				mFrame.saveHQM();
			case ADialog.NO:
//				FHqm hqm = new FHqm( null);
				EditFrame.createNew();
		}
	}
}
