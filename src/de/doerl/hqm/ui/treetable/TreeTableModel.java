package de.doerl.hqm.ui.treetable;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.utils.Utils;

public class TreeTableModel extends AbstractTableModel implements TreeModel, IModelListener {
	private static final long serialVersionUID = 6258794666345834372L;
	private static final Logger LOGGER = Logger.getLogger( TreeTableModel.class.getName());
	static final int COL_TREE = 0;
	static final int COL_VALUE = 1;
	private EventListenerList mListener = new EventListenerList();
	private HashMap<ABase, ATreeTableRow> mRows = new HashMap<ABase, ATreeTableRow>();
	private ATreeTableRow mRoot = new RootRow();
	private JTree mTree;

	public TreeTableModel() {
	}

	void addNewNode( ATreeTableRow parent, ABase elem, ATreeTableRow node) {
		mRows.put( elem, node);
		parent.getChildren().add( node);
		ATreeTableRow[] arr = getPathToRoot( parent);
		fireTreeStructureChanged( this, arr, null, null);
	}

	@Override
	public void addTreeModelListener( TreeModelListener l) {
		mListener.add( TreeModelListener.class, l);
	}

	@Override
	public void baseAdded( ModelEvent event) {
		try {
			ABase base = event.getBase();
			if (base != null && !mRows.containsKey( base)) {
				TableFactory.get( base, this);
			}
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
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
		ABase base = event.getBase();
		if (base == null) {
			ATreeTableRow[] arr = getPathToRoot( mRoot);
			mTree.expandPath( new TreePath( arr));
		}
		else if (base instanceof FHqm) {
			FHqm hqm = (FHqm) base;
			ATreeTableRow node = getNode( hqm); // .mQuests
			ATreeTableRow[] arr = getPathToRoot( node);
			mTree.expandPath( new TreePath( arr));
		}
		else {
			ATreeTableRow node = getNode( base.getParent());
			ATreeTableRow[] arr = getPathToRoot( node);
			mTree.expandPath( new TreePath( arr));
		}
		fireTableDataChanged();
	}

	boolean containsKey( ABase elem) {
		return mRows.containsKey( elem);
	}

	protected void fireTreeNodesChanged( Object source, Object[] path, int[] childIndices, Object[] children) {
		Object[] listeners = mListener.getListenerList();
		TreeModelEvent event = new TreeModelEvent( source, path, childIndices, children);
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				try {
					((TreeModelListener) listeners[i + 1]).treeNodesChanged( event);
				}
				catch (ClassCastException ex) {
					Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
				}
			}
		}
	}

	protected void fireTreeNodesInserted( Object source, Object[] path, int[] childIndices, Object[] children) {
		Object[] listeners = mListener.getListenerList();
		TreeModelEvent event = new TreeModelEvent( source, path, childIndices, children);
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				try {
					((TreeModelListener) listeners[i + 1]).treeNodesInserted( event);
				}
				catch (ClassCastException ex) {
					Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
				}
			}
		}
	}

	protected void fireTreeNodesRemoved( Object source, Object[] path, int[] childIndices, Object[] children) {
		Object[] listeners = mListener.getListenerList();
		TreeModelEvent event = new TreeModelEvent( source, path, childIndices, children);
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				try {
					((TreeModelListener) listeners[i + 1]).treeNodesRemoved( event);
				}
				catch (ClassCastException ex) {
					Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
				}
			}
		}
	}

	protected void fireTreeStructureChanged( Object source, Object[] path, int[] childIndices, Object[] children) {
		Object[] listeners = mListener.getListenerList();
		TreeModelEvent event = new TreeModelEvent( source, path, childIndices, children);
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				try {
					((TreeModelListener) listeners[i + 1]).treeStructureChanged( event);
				}
				catch (ClassCastException ex) {
					Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
				}
			}
		}
	}

	@Override
	public Object getChild( Object parent, int index) {
		ATreeTableRow node = (ATreeTableRow) parent;
		return node.getChildren().get( index);
	}

	@Override
	public int getChildCount( Object parent) {
		ATreeTableRow node = (ATreeTableRow) parent;
		return node.getChildren().size();
	}

	@Override
	public Class<?> getColumnClass( int col) {
		switch (col) {
			case COL_TREE:
				return TreeTableModel.class;
			case COL_VALUE:
				return String.class;
			default:
				return String.class;
		}
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName( int col) {
		switch (col) {
			case COL_TREE:
				return "Key";
			case COL_VALUE:
				return "Value";
			default:
				return "Unknown";
		}
	}

	@Override
	public int getIndexOfChild( Object parent, Object child) {
		return 0;
	}

	ATreeTableRow getNode( ABase base) {
		return mRows.get( base);
	}

	ATreeTableRow[] getPathToRoot( ATreeTableRow node) {
		return getPathToRoot( node, 0);
	}

	private ATreeTableRow[] getPathToRoot( ATreeTableRow node, int depth) {
		if (node != null) {
			++depth;
			ATreeTableRow[] retNodes = null;
			if (node == mRoot) {
				retNodes = new ATreeTableRow[depth];
			}
			else {
				retNodes = getPathToRoot( node.getParent(), depth);
			}
			retNodes[retNodes.length - depth] = node;
			return retNodes;
		}
		if (depth > 0) {
			return new ATreeTableRow[depth];
		}
		return null;
	}

	@Override
	public Object getRoot() {
		return mRoot;
	}

	public ATreeTableRow getRow( int row) {
		TreePath treePath = mTree.getPathForRow( row);
		return (ATreeTableRow) treePath.getLastPathComponent();
	}

	@Override
	public int getRowCount() {
		return mTree.getRowCount();
	}

	@Override
	public Object getValueAt( int row, int col) {
		try {
			switch (col) {
				case COL_TREE:
					return getRow( row).getName();
				case COL_VALUE:
					return getRow( row).getValue();
			}
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
		}
		return null;
	}

	@Override
	public boolean isCellEditable( int row, int col) {
		switch (col) {
			case COL_TREE:
			case COL_VALUE:
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean isLeaf( Object node) {
		return getChildCount( node) == 0;
	}

	@Override
	public void removeTreeModelListener( TreeModelListener l) {
		mListener.remove( TreeModelListener.class, l);
	}

	void setTree( JTree tree) {
		mTree = tree;
	}

	@Override
	public void setValueAt( Object value, int row, int col) {
		try {
			switch (col) {
				case COL_VALUE:
					ATreeTableRow ttr = getRow( row);
					ttr.apply( this, String.valueOf( value));
			}
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
		}
	}

	@Override
	public void valueForPathChanged( TreePath path, Object newValue) {
	}
}
