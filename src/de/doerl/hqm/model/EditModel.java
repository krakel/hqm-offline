package de.doerl.hqm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.utils.Utils;

public class EditModel {
	private static final Logger LOGGER = Logger.getLogger( EditModel.class.getName());
	private static final IModelListener[] EMPTY = new IModelListener[0];
	private List<IModelListener> mListener = new ArrayList<>();
	private Vector<FHqm> mHQMs = new Vector<>();

	public void addListener( IModelListener l) {
		if (!mListener.contains( l)) {
			mListener.add( l);
		}
	}

	public void fireBaseActivate( ABase base) {
		ModelEvent event = new ModelEvent( base);
		for (IModelListener l : mListener.toArray( EMPTY)) {
			try {
				l.baseActivate( event);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
	}

	public void fireBaseAdded( ABase base) {
		ModelEvent event = new ModelEvent( base);
		for (IModelListener l : mListener.toArray( EMPTY)) {
			try {
				l.baseAdded( event);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
	}

	public void fireBaseChanged( ABase base) {
		ModelEvent event = new ModelEvent( base);
		for (IModelListener l : mListener.toArray( EMPTY)) {
			try {
				l.baseChanged( event);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
	}

	public void fireBaseRemoved( ABase base) {
		ModelEvent event = new ModelEvent( base);
		for (IModelListener l : mListener.toArray( EMPTY)) {
			try {
				l.baseRemoved( event);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
	}

	public <T, U> T forEachHQM( IHQMWorker<T, U> worker, U p) {
		for (FHqm disp : mHQMs) {
			try {
				if (disp != null) {
					T obj = disp.accept( worker, p);
					if (obj != null) {
						return obj;
					}
				}
			}
			catch (RuntimeException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return null;
	}

	public void loadHQM( FHqm hqm) {
		mHQMs.add( hqm);
		fireBaseAdded( hqm);
		fireBaseActivate( hqm);
	}

	public void removeListener( IModelListener l) {
		mListener.remove( l);
	}
}
