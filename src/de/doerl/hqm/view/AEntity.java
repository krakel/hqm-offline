package de.doerl.hqm.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import de.doerl.hqm.base.ABase;

abstract class AEntity<T extends ABase> extends JPanel {
	private static final long serialVersionUID = -3039298434411863516L;
	protected EditView mView;

	public AEntity( EditView view) {
		mView = view;
		setOpaque( true);
	}

	public abstract T getBase();

	public void remove() {
//		mView.removeMouse( this);
		mView.remove( this);
		setVisible( false);
	};

	public void update() {
//		mView.addMouse( this);
		setVisible( true);
		mView.add( this, BorderLayout.CENTER);
	};
}
