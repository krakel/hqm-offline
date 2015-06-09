package de.doerl.hqm.view;

import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class LeafList<E> extends JList<E> {
	private static final long serialVersionUID = -8417821463588699381L;
	private ClickHandler mHandler = new ClickHandler();
	private DefaultListModel<E> mModel = new DefaultListModel<E>();

	public LeafList() {
		setModel( mModel);
		setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		addMouseListener( mHandler);
	}

	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	@Override
	public DefaultListModel<E> getModel() {
		return mModel;
	}

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}
}
