package de.doerl.hqm.medium;

import javax.swing.SwingUtilities;

public abstract class ASaveFile extends ADialogFile implements Runnable {
	private static final long serialVersionUID = -4487749812009945164L;

	public ASaveFile( String name, ICallback cb) {
		super( name, cb);
		setEnabled( false);
	}

	public void updateAction( RefreshEvent event) {
		SwingUtilities.invokeLater( this);
	}
}
