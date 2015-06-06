package de.doerl.hqm.ui.tree;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.controller.EditController;

public class ElementTree extends JTree implements TreeSelectionListener {
	private static final long serialVersionUID = -276846328014148131L;

	public ElementTree( EditController ctrl) {
		super( new ElementTreeModel());
		setRootVisible( false);
		setShowsRootHandles( true);
		setCellRenderer( new ElementTreeCellRenderer());
//		setSelectionModel( new ElementSelectionModel());
		getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION);
//		putClientProperty( "JTree.lineStyle", "None");
//		addTreeSelectionListener( this);
		addMouseListener( new SelectHandler( ctrl));
//		ToolTipManager.sharedInstance().registerComponent( this);
	}

	@Override
	public ElementTreeModel getModel() {
		return (ElementTreeModel) super.getModel();
	}

	public void showHqm( FHqm hqm) {
		ElementTreeModel model = getModel();
		MutableTreeNode node = model.getNode( hqm);
		if (node != null) {
			TreeNode[] arr = model.getPathToRoot( node);
			TreePath path = new TreePath( arr);
			this.expandPath( path);
			this.setSelectionPath( path);
		}
	}

	@Override
	public void valueChanged( TreeSelectionEvent evt) {
//		setSelectionPath( evt.getPath());
	}
}
