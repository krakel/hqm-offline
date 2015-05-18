package de.doerl.hqm.view;

import java.awt.GridLayout;

import javax.swing.JPanel;

import de.doerl.hqm.base.ABase;

class EntityEmpty extends AEntity<ABase> {
	private static final long serialVersionUID = -5245406021362050386L;

	public EntityEmpty( EditView view) {
		super( view, new GridLayout( 1, 2));
		createLeafs();
	}

	@Override
	protected void createLeft( JPanel leaf) {
	}

	@Override
	protected void createRight( JPanel leaf) {
	}

	@Override
	public ABase getBase() {
		return null;
	}
}
