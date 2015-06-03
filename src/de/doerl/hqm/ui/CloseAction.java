package de.doerl.hqm.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.IRefreshListener;
import de.doerl.hqm.medium.RefreshEvent;

public class CloseAction extends ABundleAction implements IRefreshListener, Runnable {
	private static final long serialVersionUID = 3759442083520823583L;
	private EditFrame mFrame;

	public CloseAction( EditFrame frame) {
		super( "hqm.close");
		mFrame = frame;
		setEnabled( false);
	}

	@Override
	public void action( Window frame) {
	}

	@Override
	public void actionPerformed( ActionEvent e) {
		FHqm hqm = mFrame.getCurrent();
		if (hqm != null) {
			if (!hqm.isModified()) {
				mFrame.getModel().removeHQM( hqm);
			}
			else if (WarnDialogs.askMissing( mFrame)) {
				mFrame.getModel().removeHQM( hqm);
			}
		}
	}

	@Override
	public void run() {
		FHqm hqm = mFrame.getCurrent();
		setEnabled( hqm != null);
	}

	@Override
	public void updateAction( RefreshEvent event) {
		SwingUtilities.invokeLater( this);
	}
}
