package de.doerl.hqm.ui;

import java.awt.event.ActionEvent;

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
