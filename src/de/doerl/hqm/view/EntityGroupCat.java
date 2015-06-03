package de.doerl.hqm.view;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import de.doerl.hqm.base.FGroupCat;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.ui.EditFrame;

class EntityGroupCat extends AEntity<FGroupCat> {
	private static final long serialVersionUID = 2046344393475287723L;
//	private static final Logger LOGGER = Logger.getLogger( EntityGroupCat.class.getName());
	private final FGroupCat mCategory;
	private JToolBar mTool = EditFrame.createToolBar();

	EntityGroupCat( FGroupCat cat, EditController ctrl) {
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
		leaf.add( new LeafLabel( "GroupCat not ready :("));
	}

	@Override
	protected void createRight( JPanel leaf) {
	}

	@Override
	public FGroupCat getBase() {
		return mCategory;
	}

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}
}
