package de.doerl.hqm.ui;

import java.awt.event.ActionEvent;

import de.doerl.hqm.utils.ResourceManager;

@SuppressWarnings( "nls")
public class NewAction extends ABundleAction {
	private static final long serialVersionUID = -44344993759630964L;
	private EditFrame mFrame;

	public NewAction( EditFrame frame) {
		super( "hqm.new", ResourceManager.RESOURCE);
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
