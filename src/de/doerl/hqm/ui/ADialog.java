package de.doerl.hqm.ui;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JDialog;

public abstract class ADialog extends JDialog {
	private static final long serialVersionUID = -8555679578310005324L;
	public static final int ERROR = -1; // Return value if an error occured.
	public static final int APPROVE = 0; // Return value if ok is chosen.
	public static final int CANCEL = 1; // Return value if cancel is chosen.
	public static final int SAVE = 2; // Return value if save is chosen.
	public static final int NO = 3; // Return value if no is chosen.

	public ADialog() {
	}

	public ADialog( Window owner) {
		super( owner);
	}

	public ADialog( Window owner, ModalityType modalityType) {
		super( owner, modalityType);
	}

	public static Window getParentFrame( Container source) {
		if (source == null) {
			return null;
		}
		if (source instanceof Frame) {
			return (Window) source;
		}
		if (source instanceof Dialog) {
			return (Window) source;
		}
		return getParentFrame( source.getParent());
	}
}
