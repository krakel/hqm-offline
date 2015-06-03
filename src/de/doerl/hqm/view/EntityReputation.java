package de.doerl.hqm.view;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.ui.EditFrame;

class EntityReputation extends AEntity<FReputation> {
	private static final long serialVersionUID = 2046344393475287723L;
//	private static final Logger LOGGER = Logger.getLogger( EntityReputation.class.getName());
	private final FReputation mRep;
	private JToolBar mTool = EditFrame.createToolBar();

	EntityReputation( FReputation rep, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 2));
		mRep = rep;
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
		leaf.add( new LeafLabel( "Reputation not ready :("));
	}

	@Override
	protected void createRight( JPanel leaf) {
	}

	@Override
	public FReputation getBase() {
		return mRep;
	}

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}
}
