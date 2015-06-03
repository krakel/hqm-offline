package de.doerl.hqm.view;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.ui.EditFrame;

class EntityReputationCat extends AEntity<FReputationCat> {
	private static final long serialVersionUID = -2408903593673459841L;
//	private static final Logger LOGGER = Logger.getLogger( EntityReputationCat.class.getName());
	private final FReputationCat mCategory;
	private JToolBar mTool = EditFrame.createToolBar();

	EntityReputationCat( FReputationCat cat, EditController ctrl) {
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
		leaf.add( new LeafLabel( "ReputationCat not ready :("));
	}

	@Override
	protected void createRight( JPanel leaf) {
	}

	@Override
	public FReputationCat getBase() {
		return mCategory;
	}

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}
}
