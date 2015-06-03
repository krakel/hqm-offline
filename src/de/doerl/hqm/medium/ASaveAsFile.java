package de.doerl.hqm.medium;

import java.awt.Window;

import javax.swing.SwingUtilities;

import de.doerl.hqm.base.FHqm;

public abstract class ASaveAsFile extends ADialogFile implements Runnable {
	private static final long serialVersionUID = -5266818287056807037L;

	public ASaveAsFile( String name, ICallback cb) {
		super( name, cb);
		setEnabled( false);
	}

	@Override
	public void action( Window frame) {
	}

	@Override
	public void run() {
		FHqm hqm = mCallback.updateHQM();
		setEnabled( hqm != null);
	}

	public void updateAction( RefreshEvent event) {
		SwingUtilities.invokeLater( this);
	}
}
