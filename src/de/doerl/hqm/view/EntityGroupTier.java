package de.doerl.hqm.view;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.ui.EditFrame;

class EntityGroupTier extends AEntity<FGroupTier> {
	private static final long serialVersionUID = 7878679321832390389L;
//	private static final Logger LOGGER = Logger.getLogger( EntityGroupTier.class.getName());
	private final FGroupTier mTier;
	private JToolBar mTool = EditFrame.createToolBar();

	EntityGroupTier( FGroupTier tier, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 2));
		mTier = tier;
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
		leaf.add( new LeafLabel( "GroupTier not ready :("));
	}

	@Override
	protected void createRight( JPanel leaf) {
	}

	@Override
	public FGroupTier getBase() {
		return mTier;
	}

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}
}
