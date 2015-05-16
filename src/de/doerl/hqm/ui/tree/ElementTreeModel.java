package de.doerl.hqm.ui.tree;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.utils.Utils;

public class ElementTreeModel extends DefaultTreeModel implements IModelListener {
	private static final long serialVersionUID = -3566772435718046914L;
	private static final Logger LOGGER = Logger.getLogger( ElementTreeModel.class.getName());

	public ElementTreeModel() {
		super( new DefaultMutableTreeNode( "root"), true);
	}

	@Override
	public void baseAdded( ModelEvent event) {
		try {
			ABase base = event.getBase();
			if (base != null) {
				TreeFactory.get( base, this);
			}
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	@Override
	public void baseChanged( ModelEvent event) {
	}

	@Override
	public void baseRemoved( ModelEvent event) {
	}

	@Override
	public void baseUpdate( ModelEvent event) {
	}

	MutableTreeNode createNode( MutableTreeNode parent, ANode elem) {
		MutableTreeNode node = new ElementTreeNode( elem);
		insertNodeInto( node, parent, parent.getChildCount());
		return node;
	}

	private MutableTreeNode getChild( MutableTreeNode parent, ABase base) {
		if (base != null) {
			for (int i = 0; i < parent.getChildCount(); ++i) {
				try {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt( i);
					ANode user = (ANode) node.getUserObject();
					if (Utils.equals( base, user.getBase())) {
						return node;
					}
				}
				catch (ClassCastException ex) {
					Utils.logThrows( LOGGER, Level.WARNING, ex);
				}
			}
		}
		return null;
	}

	MutableTreeNode getNode( ABase base) {
		MutableTreeNode parent = getParentNode( base);
		if (parent != null && parent.getChildCount() > 0) {
			return getChild( parent, base);
		}
		return null;
	}

	private MutableTreeNode getParentNode( ABase base) {
		ABase parent = base != null ? base.getParent() : null;
		if (parent != null) {
			return getNode( parent);
		}
		else {
			return getRoot();
		}
	}

	@Override
	public DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode) super.getRoot();
	}
}
