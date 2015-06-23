package de.doerl.hqm.view;

import java.awt.Window;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import javax.swing.JComponent;
import javax.swing.JLabel;

import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.view.ADialogList.ICreator;

abstract class ADialogEdit<E> extends ADialog {
	private static final long serialVersionUID = -281546652357185300L;

	public ADialogEdit( Window owner) {
		super( owner);
	}

	public abstract E addElement( ICreator<E> creator);

	protected Group addLine( GroupLayout layout, Group left, Group right, String descr, JComponent comp) {
		JLabel lbl = new JLabel( descr);
		left.addComponent( lbl);
		right.addComponent( comp);
		return layout.createParallelGroup( Alignment.BASELINE).addComponent( lbl).addComponent( comp);
	}

	public abstract E changeElement( E entry);
}
