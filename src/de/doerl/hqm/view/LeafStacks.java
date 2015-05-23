package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.doerl.hqm.base.FParameterStack;

class LeafStacks extends JPanel {
	private static final long serialVersionUID = -2203511730691517504L;
	private ClickHandler mHandler = new ClickHandler();

	public LeafStacks( int height) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		setMinimumSize( new Dimension( 0, height));
		setPreferredSize( new Dimension( 0, height));
		setMaximumSize( new Dimension( Short.MAX_VALUE, height));
		addMouseListener( mHandler);
		addMouseListener( new BorderAdapter( this));
	}

	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	public void createIconList( Vector<FParameterStack> list, JLabel btn) {
		removeAll();
		for (FParameterStack stk : list) {
			add( new LeafIcon( stk.mValue));
			add( Box.createHorizontalStrut( 3));
		}
		add( Box.createHorizontalGlue());
		if (btn != null) {
			add( btn);
		}
		revalidate();
		repaint();
	}

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}
}
