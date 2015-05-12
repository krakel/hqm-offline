package de.doerl.hqm.ui.treetable;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellEditor;

public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 5326354061530603294L;
	private JTree mTree;
	private JTable mTable;

	public TreeTableCellEditor( JTree tree, JTable table) {
		mTree = tree;
		mTable = table;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row, int col) {
		return mTree;
	}

	@Override
	public boolean isCellEditable( EventObject event) {
		if (event instanceof MouseEvent) {
			MouseEvent old = (MouseEvent) event;
			int clicks = old.getClickCount();
			if (clicks > 0) {
				int x = old.getX() - mTable.getCellRect( 0, 0, true).x;
				int y = old.getY();
				MouseEvent me = new MouseEvent( mTree, old.getID(), old.getWhen(), old.getModifiers(), x, y, 2, old.isPopupTrigger());
				mTree.dispatchEvent( me);
			}
		}
		return false;
	}
}
