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

@SuppressWarnings( "nls")
public class EditModel {
	private static final Logger LOGGER = Logger.getLogger( EditModel.class.getName());
	private List<IModelListener> mListener = new ArrayList<IModelListener>();
//	private ABase mActive;
	private Vector<FHqm> mHQMs = new Vector<FHqm>();

	public void addListener( IModelListener l) {
		if (!mListener.contains( l)) {
			mListener.add( l);
		}
	}

	public void fireBaseAdded( ABase base) {
		ModelEvent event = new ModelEvent( base);
		for (IModelListener l : mListener) {
			try {
				l.baseAdded( event);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, "EditModel.event.error", ex);
			}
		}
	}

	public void fireBaseChanged( ABase base) {
		ModelEvent event = new ModelEvent( base);
		for (IModelListener l : mListener) {
			try {
				l.baseChanged( event);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, "EditModel.event.error", ex);
			}
		}
	}

	public void fireBaseRemoved( ABase base) {
		ModelEvent event = new ModelEvent( base);
		for (IModelListener l : mListener) {
			try {
				l.baseRemoved( event);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, "EditModel.event.error", ex);
			}
		}
	}

	public void fireBaseUpdate( ABase base) {
		ModelEvent event = new ModelEvent( base);
		for (IModelListener l : mListener) {
			try {
				l.baseUpdate( event);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.WARNING, "EditModel.event.error", ex);
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
				Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
			}
		}
		return null;
	}

	public void loadHQM( FHqm hqm) {
		mHQMs.add( hqm);
		LoaderFactory.create( hqm, this);
		setActive( hqm);
	}

	public void removeListener( IModelListener l) {
		mListener.remove( l);
	}

	public void setActive( ABase base) {
//		mActive = base;
		fireBaseUpdate( base);
	}
}
