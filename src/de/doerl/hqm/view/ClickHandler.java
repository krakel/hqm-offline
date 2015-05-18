package de.doerl.hqm.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import de.doerl.hqm.utils.Utils;

class ClickHandler implements MouseListener {
	private static final Logger LOGGER = Logger.getLogger( ClickHandler.class.getName());
	private static final Timer TIMER = new Timer( "doubleclickTimer", true);
	private static final Border BORDER = BorderFactory.createBevelBorder( BevelBorder.LOWERED);
	private ClickListener mListener = new ClickListener();
	private volatile boolean mOneClick;
	private JComponent mComp;
	private Border mOldBorder;

	public ClickHandler( JComponent comp) {
		mComp = comp;
	}

	public ClickListener getListener() {
		return mListener;
	}

	@Override
	public void mouseClicked( MouseEvent evt) {
		switch (evt.getButton()) {
			case MouseEvent.BUTTON1:
				if (mOneClick) {
					mListener.fireDoubleEvent( evt);
					mOneClick = false;
				}
				else {
					mListener.fireSingleEvent( evt);
					mOneClick = true;
					TIMER.schedule( new SingleTask(), 300);
				}
				break;
			case MouseEvent.BUTTON3:
				mListener.fireSingleEvent( evt);
				break;
		}
	}

	@Override
	public void mouseEntered( MouseEvent evt) {
		mOldBorder = mComp.getBorder();
		mComp.setBorder( BORDER);
	}

	@Override
	public void mouseExited( MouseEvent evt) {
		mComp.setBorder( mOldBorder);
		mOldBorder = null;
	}

	@Override
	public void mousePressed( MouseEvent evt) {
	}

	@Override
	public void mouseReleased( MouseEvent evt) {
	}

	public static class ClickListener {
		private List<IClickListener> mListener = new ArrayList<IClickListener>();

		public void addClickListener( IClickListener l) {
			if (!mListener.contains( l)) {
				mListener.add( l);
			}
		}

		void fireDoubleEvent( MouseEvent evt) {
			for (IClickListener l : mListener) {
				try {
					l.onDoubleClick( evt);
				}
				catch (Exception ex) {
					Utils.logThrows( LOGGER, Level.WARNING, ex);
				}
			}
		}

		void fireSingleEvent( MouseEvent evt) {
			for (IClickListener l : mListener) {
				try {
					l.onSingleClick( evt);
				}
				catch (Exception ex) {
					Utils.logThrows( LOGGER, Level.WARNING, ex);
				}
			}
		}

		public void removeClickListener( IClickListener l) {
			mListener.remove( l);
		}
	}

	private final class SingleTask extends TimerTask {
		public SingleTask() {
		}

		@Override
		public void run() {
			mOneClick = false;
		}
	}
}
