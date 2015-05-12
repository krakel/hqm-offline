package de.doerl.hqm.ui.treetable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class TreeTableCellRenderer extends JTree implements TableCellRenderer, TreeExpansionListener {
	private static final long serialVersionUID = -631071931584099554L;
	private TreeTable mTable;
	private int mLast;

	public TreeTableCellRenderer( TreeTableModel model, TreeTable table) {
		super( model);
		mTable = table;
		setRootVisible( false);
		setShowsRootHandles( true);
		setCellRenderer( new TreeCellRenderer());
		getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION);
		setRowHeight( getRowHeight());
		addTreeExpansionListener( this);
	}

	@Override
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setBackground( table.getSelectionBackground());
		}
		else {
			setBackground( table.getBackground());
		}
//		setFont( getFont().deriveFont( Font.BOLD));
		mLast = row;
		if (mTable != null) {
			TreeTableModel model = (TreeTableModel) mTable.getModel();
			ATreeTableRow node = model.getRow( row);
			table.setForeground( node.hasConfig() ? Color.MAGENTA : Color.BLACK);
		}
		return this;
	}

	@Override
	public void paint( Graphics g) {
		g.translate( 0, -mLast * getRowHeight());
		super.paint( g);
	}

	@Override
	public void setBounds( int x, int y, int w, int h) {
		if (mTable != null) {
			super.setBounds( x, 0, w, mTable.getHeight());
		}
	}

	@Override
	public void setRowHeight( int rowHeight) {
		if (rowHeight > 0) {
			super.setRowHeight( rowHeight);
			if (mTable != null && mTable.getRowHeight() != rowHeight) {
				mTable.setRowHeight( getRowHeight());
			}
		}
	}

	public void treeCollapsed( TreeExpansionEvent event) {
		TreeTableModel model = (TreeTableModel) getModel();
		model.fireTableDataChanged();
	}

	public void treeExpanded( TreeExpansionEvent event) {
		TreeTableModel model = (TreeTableModel) getModel();
		model.fireTableDataChanged();
	}
}
