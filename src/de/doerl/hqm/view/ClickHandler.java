package de.doerl.hqm.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.Utils;

class ClickHandler extends MouseAdapter {
	private static final Logger LOGGER = Logger.getLogger( ClickHandler.class.getName());
	private static final Timer TIMER = new Timer( "doubleclickTimer", true);
	private ClickListener mListener = new ClickListener();
	private volatile boolean mOneClick;

	public ClickHandler() {
	}

	public void addClickListener( IClickListener l) {
		mListener.addClickListener( l);
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

	public void removeClickListener( IClickListener l) {
		mListener.removeClickListener( l);
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
