package de.doerl.hqm.view.leafs;

import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.doerl.hqm.view.AEntity;
import de.doerl.hqm.view.BorderAdapter;
import de.doerl.hqm.view.ClickHandler;

public class LeafStacks extends JPanel {
	private static final long serialVersionUID = -2203511730691517504L;
	private ClickHandler mHandler = new ClickHandler();

	public LeafStacks() {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		AEntity.setSizes( this, AEntity.ICON_SIZE);
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
