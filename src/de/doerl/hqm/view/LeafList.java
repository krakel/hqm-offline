package de.doerl.hqm.view;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import de.doerl.hqm.view.ClickHandler.ClickListener;

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

	public ClickListener getHandler() {
		return mHandler.getListener();
	}

	@Override
	public DefaultListModel<E> getModel() {
		return mModel;
	}
}
