package de.doerl.hqm.ui.treetable;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;

public class TreeTableSelectionModel extends DefaultTreeSelectionModel {
	private static final long serialVersionUID = -403286155546960141L;

	public TreeTableSelectionModel() {
		getListSelectionModel().addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged( ListSelectionEvent event) {
			}
		});
	}

	ListSelectionModel getListSelectionModel() {
		return listSelectionModel;
	}
}
