package de.doerl.hqm.ui.treetable;

import java.awt.Dimension;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import de.doerl.hqm.controller.EditController;

public class TreeTable extends JTable {
	private static final long serialVersionUID = 3375615766612013890L;
	private static DefaultCellEditor sPrefBoolean;
	static {
		JComboBox<String> combo = new JComboBox<String>();
		combo.addItem( Boolean.toString( false));
		combo.addItem( Boolean.toString( true));
		sPrefBoolean = new DefaultCellEditor( combo);
	}

	public TreeTable( EditController ctrl) {
		super( new TreeTableModel());
		TreeTableModel model = getModel();
		TreeTableCellRenderer tree = new TreeTableCellRenderer( model, this);
		model.setTree( tree);
		TreeTableSelectionModel selModel = new TreeTableSelectionModel();
		tree.setSelectionModel( selModel);
		setSelectionModel( selModel.getListSelectionModel());
		TableColumnModel colModel = getColumnModel();
		prepareColumnTree( colModel.getColumn( TreeTableModel.COL_TREE), tree);
		prepareColumnParam( colModel.getColumn( TreeTableModel.COL_VALUE));
//		setDefaultRenderer( TreeTableModel.class, renderer);
//		setDefaultEditor( TreeTableModel.class, new TreeTableCellEditor( renderer, this));
//		putClientProperty( "JTree.lineStyle", "None");
		setShowGrid( false);
		setIntercellSpacing( new Dimension( 3, 0));
		addMouseListener( new SelectHandler( ctrl));
	}

	@Override
	public TableCellEditor getCellEditor( int row, int col) {
		switch (col) {
			case TreeTableModel.COL_VALUE:
				TreeTableModel model = getModel();
				ATreeTableRow ttr = model.getRow( row);
				Class<?> cls = ttr.getRowClass();
				if (Boolean.class.equals( cls)) {
					return sPrefBoolean;
				}
				return getDefaultEditor( String.class);
			case TreeTableModel.COL_TREE:
			default:
				return super.getCellEditor( row, col);
		}
	}

	@Override
	public TreeTableModel getModel() {
		return (TreeTableModel) super.getModel();
	}

	private void prepareColumnParam( TableColumn col) {
		col.setPreferredWidth( 250);
//		col.setMaxWidth( 60);
		col.setResizable( true);
	}

	private void prepareColumnTree( TableColumn col, TreeTableCellRenderer renderer) {
		col.setCellRenderer( renderer);
		col.setPreferredWidth( 150);
//		col.setMaxWidth( 300);
		col.setResizable( true);
		col.setCellEditor( new TreeTableCellEditor( renderer, this));
	}
}
