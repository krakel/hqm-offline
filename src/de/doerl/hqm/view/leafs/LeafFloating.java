package de.doerl.hqm.view.leafs;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import de.doerl.hqm.view.AEntity;
import de.doerl.hqm.view.BorderAdapter;
import de.doerl.hqm.view.ClickHandler;

public class LeafFloating extends JPanel {
	private static final long serialVersionUID = 5193402879918959911L;
	private ClickHandler mHandler = new ClickHandler();

	public LeafFloating() {
		setLayout( new FlowLayout( FlowLayout.LEFT, 3, AEntity.GAP / 2));
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
//		setPreferredSize( new Dimension( 7 * AEntity.ICON_SIZE, 4 * AEntity.ICON_SIZE));
		addMouseListener( mHandler);
		addMouseListener( new BorderAdapter( this));
	}

	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}
}
