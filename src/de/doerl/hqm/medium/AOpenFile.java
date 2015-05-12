package de.doerl.hqm.medium;

import javax.swing.SwingUtilities;

public abstract class AOpenFile extends ADialogFile implements Runnable {
	private static final long serialVersionUID = 6409267805670417917L;

	public AOpenFile( String name, ICallback cb) {
		super( name, cb);
	}

	@Override
	public void updateAction( RefreshEvent event) {
		SwingUtilities.invokeLater( this);
	}
}
