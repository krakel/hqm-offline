package de.doerl.hqm.medium;

public abstract class ASaveAsFile extends ADialogFile {
	private static final long serialVersionUID = -5266818287056807037L;

	public ASaveAsFile( String name, ICallback cb) {
		super( name, cb);
	}

	public void updateAction( RefreshEvent event) {
	}
}
