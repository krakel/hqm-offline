package de.doerl.hqm.view;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FItemStack;

class LeafStacks extends JPanel {
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

	public void update( Vector<FItemStack> list, JLabel btn) {
		removeAll();
		if (list.isEmpty()) {
			add( LeafIcon.createEmpty( 0.8));
			add( Box.createHorizontalStrut( 3));
		}
		else {
			for (AStack stk : list) {
				LeafIcon leaf = new LeafIcon();
				IconUpdate.create( leaf, stk, 0.8, stk.countOf());
				add( leaf);
				add( Box.createHorizontalStrut( 3));
			}
		}
		add( Box.createHorizontalGlue());
		if (btn != null) {
			add( btn);
		}
		revalidate();
		repaint();
	}
}
