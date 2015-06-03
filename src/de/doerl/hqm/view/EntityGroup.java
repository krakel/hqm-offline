package de.doerl.hqm.view;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.ui.EditFrame;

class EntityGroup extends AEntity<FGroup> {
	private static final long serialVersionUID = 3970324784121337237L;
//	private static final Logger LOGGER = Logger.getLogger( EntityGroup.class.getName());
	private final FGroup mGroup;
	private JToolBar mTool = EditFrame.createToolBar();

	EntityGroup( FGroup grp, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 2));
		mGroup = grp;
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
		leaf.add( new LeafLabel( "Group not ready :("));
	}

	@Override
	protected void createRight( JPanel leaf) {
	}

	@Override
	public FGroup getBase() {
		return mGroup;
	}

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}
}
