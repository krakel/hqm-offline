package de.doerl.hqm.view;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.ui.EditFrame;

class EntityGroupTierCat extends AEntity<FGroupTierCat> {
	private static final long serialVersionUID = -1554359878811465741L;
//	private static final Logger LOGGER = Logger.getLogger( EntityGroupTierCat.class.getName());
	private final FGroupTierCat mCategory;
	private JToolBar mTool = EditFrame.createToolBar();

	EntityGroupTierCat( FGroupTierCat cat, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 2));
		mCategory = cat;
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
	public void baseRemoved( ModelEvent event) {
	}

	@Override
	protected void createLeft( JPanel leaf) {
		leaf.add( new LeafLabel( "GroupTierCat not ready :("));
	}

	@Override
	protected void createRight( JPanel leaf) {
	}

	@Override
	public FGroupTierCat getBase() {
		return mCategory;
	}

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}
}
