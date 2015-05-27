package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FParameterStack;
import de.doerl.hqm.utils.ImageLoader;

class LeafStacks extends JPanel {
	private static final long serialVersionUID = -2203511730691517504L;
	private ClickHandler mHandler = new ClickHandler();
	private Vector<FParameterStack> mList;
	private JLabel mBtn;
	private Runnable mCallback = new Runnable() {
		@Override
		public void run() {
			updateIcons( null);
		}
	};

	public LeafStacks( int height, Vector<FParameterStack> list, JLabel btn) {
		mList = list;
		mBtn = btn;
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

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}

	public void update() {
		updateIcons( mCallback);
	}

	private void updateIcons( Runnable cb) {
		removeAll();
		for (FParameterStack param : mList) {
			AStack stk = param.mValue;
			Image img = ImageLoader.getImage( stk.getKey(), cb);
			add( new LeafIcon( new StackIcon( stk.countOf(), img, 0.8)));
			add( Box.createHorizontalStrut( 3));
		}
		add( Box.createHorizontalGlue());
		if (mBtn != null) {
			add( mBtn);
		}
		revalidate();
		repaint();
	}
}
