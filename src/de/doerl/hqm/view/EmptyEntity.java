package de.doerl.hqm.view;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.doerl.hqm.base.ABase;

class EmptyEntity extends AEntity<ABase> {
	private static final long serialVersionUID = -5245406021362050386L;
	private JPanel mLeafLeft = leafPanel( true);
	private JPanel mLeafRight = leafPanel( false);

	public EmptyEntity( EditView view) {
		super( view, new GridLayout( 1, 2));
		add( mLeafLeft);
		add( mLeafRight);
	}

	@Override
	public ABase getBase() {
		return null;
	}

	@Override
	protected JComponent getLeftTool() {
		return null;
	}

	@Override
	protected JComponent getRightTool() {
		return null;
	}
}
