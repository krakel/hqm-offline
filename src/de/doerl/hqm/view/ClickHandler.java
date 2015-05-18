package de.doerl.hqm.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import de.doerl.hqm.utils.Utils;

class ClickHandler implements MouseListener {
	private static final Logger LOGGER = Logger.getLogger( ClickHandler.class.getName());
	private static final Timer TIMER = new Timer( "doubleclickTimer", false);
	private static final Border BORDER = BorderFactory.createBevelBorder( BevelBorder.LOWERED);
	private EventListenerList mListener = new EventListenerList();
	private boolean mOneClick;
	private ChangeEvent mEvent;
	private JComponent mComp;
	private Border mOldBorder;

	public ClickHandler( JComponent comp) {
		mComp = comp;
		mEvent = new ChangeEvent( this);
	}

	public void addActionListener( ChangeListener l) {
		mListener.add( ChangeListener.class, l);
	}

	protected void fireStateChanged() {
		Object[] listeners = mListener.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				try {
					((ChangeListener) listeners[i + 1]).stateChanged( mEvent);
				}
				catch (ClassCastException ex) {
					Utils.logThrows( LOGGER, Level.WARNING, ex);
				}
			}
		}
	}

	@Override
	public void mouseClicked( MouseEvent evt) {
		if (mOneClick) {
			fireStateChanged();
			mOneClick = false;
		}
		else {
			mOneClick = true;
			TIMER.schedule( new TimerTask() {
				@Override
				public void run() {
					mOneClick = false;
				}
			}, 500);
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

	public void removeChangeListener( ChangeListener l) {
		mListener.remove( ChangeListener.class, l);
	}
}
