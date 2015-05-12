package de.doerl.hqm.view;

import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

class ListLeaf<E> extends JPanel {
	private static final long serialVersionUID = -3474402067796441059L;
	private JList<E> mList = new JList<E>( new DefaultListModel<E>());

	public ListLeaf() {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
		setOpaque( false);
		setBorder( BorderFactory.createEmptyBorder( 40, 40, 40, 10));
		mList.setOpaque( false);
		mList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
		mList.setSelectedIndex( 0);
		JScrollPane scroll = new JScrollPane( mList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setOpaque( false);
		scroll.getViewport().setOpaque( false);
		scroll.setBorder( null);
		add( scroll);
	}

	@Override
	public void addMouseListener( MouseListener listener) {
		mList.addMouseListener( listener);
	}

	public DefaultListModel<E> getModel() {
		return (DefaultListModel<E>) mList.getModel();
	}

	public void setCellRenderer( ListCellRenderer<E> renderer) {
		mList.setCellRenderer( renderer);
	}

	public void unselect() {
		mList.clearSelection();
	}
}
