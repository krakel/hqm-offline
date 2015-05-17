package de.doerl.hqm.view;

import java.awt.GridLayout;

import javax.swing.JComponent;

import de.doerl.hqm.base.ABase;

class EmptyEntity extends AEntity<ABase> {
	private static final long serialVersionUID = -5245406021362050386L;
	private LeafPanel mLeafLeft = new LeafPanel( true);
	private LeafPanel mLeafRight = new LeafPanel( false);

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
