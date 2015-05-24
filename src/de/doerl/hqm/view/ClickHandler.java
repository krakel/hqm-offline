package de.doerl.hqm.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private static final Timer TIMER = new Timer( "DoubleClickTimer", true);
	private List<ActionListener> mListener = new ArrayList<>();
	private volatile boolean mOneClick;

	public ClickHandler() {
	}

	public void addClickListener( ActionListener l) {
		if (!mListener.contains( l)) {
			mListener.add( l);
		}
	}

	void fireDoubleEvent( ActionEvent evt) {
		for (ActionListener l : mListener) {
			try {
				l.actionPerformed( evt);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
	}

	@Override
	public void mouseClicked( MouseEvent evt) {
		if (evt.getButton() == MouseEvent.BUTTON1) {
			if (mOneClick) {
				fireDoubleEvent( new ActionEvent( evt, ActionEvent.ACTION_PERFORMED, "DoubleClick"));
				mOneClick = false;
			}
			else {
				mOneClick = true;
				TIMER.schedule( new SingleTask(), 500);
			}
		}
	}

	public void removeClickListener( ActionListener l) {
		mListener.remove( l);
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
