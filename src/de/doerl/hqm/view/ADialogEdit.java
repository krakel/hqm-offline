package de.doerl.hqm.view;

import java.awt.Window;

import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.view.ADialogList.ICreator;

abstract class ADialogEdit<E> extends ADialog {
	private static final long serialVersionUID = -281546652357185300L;

	public ADialogEdit( Window owner) {
		super( owner);
	}

	public abstract E addElement( ICreator<E> creator);

	public abstract E changeElement( E entry);
}
