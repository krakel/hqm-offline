package de.doerl.hqm.view;

import java.awt.GridLayout;

import javax.swing.JPanel;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;

class EntityEmpty extends AEntity<ABase> {
	private static final long serialVersionUID = -5245406021362050386L;

	public EntityEmpty( EditController ctrl) {
		super( ctrl, new GridLayout( 1, 2));
		createLeafs();
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	@Override
	public void baseAdded( ModelEvent event) {
	}

	@Override
	public void baseChanged( ModelEvent event) {
	}

	@Override
	public void baseModified( ModelEvent event) {
	}

	@Override
	public void baseRemoved( ModelEvent event) {
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
