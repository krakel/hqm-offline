package de.doerl.hqm.ui.tree;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import de.doerl.hqm.controller.EditController;

public class ElementTree extends JTree {
	private static final long serialVersionUID = -276846328014148131L;

	public ElementTree( EditController ctrl) {
		super( new ElementTreeModel());
		setRootVisible( false);
		setShowsRootHandles( true);
		setCellRenderer( new ElementTreeCellRenderer());
//		setSelectionModel( new ElementSelectionModel());
		getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION);
//		putClientProperty( "JTree.lineStyle", "None");
		addMouseListener( new SelectHandler( ctrl));
//		ToolTipManager.sharedInstance().registerComponent( this);
	}

	@Override
	public ElementTreeModel getModel() {
		return (ElementTreeModel) super.getModel();
	}
}
