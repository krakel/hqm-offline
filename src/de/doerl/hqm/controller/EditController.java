package de.doerl.hqm.controller;

import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.logging.Logger;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.model.EditModel;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.EditView;

public class EditController implements IModelListener {
	private static final Logger LOGGER = Logger.getLogger( EditController.class.getName());
	private Vector<EditView> mViews = new Vector<EditView>();
	private EditModel mModel;

	public EditController( EditModel model) {
		model.addListener( this);
		mModel = model;
	}

	public void activate( Object src, MouseEvent ev) {
//		try {
//			TreeTable table = (TreeTable) src;
//			int row = table.rowAtPoint( ev.getPoint());
//			if (row >= 0) {
//				TreeTableModel model = table.getModel();
//				ATreeTableRow node = model.getRow( row);
//				ABase base = node.getElementObject();
//				SwingUtilities.invokeLater( new BaseAction( this, base));
//			}
//		}
//		catch (ClassCastException ex) {
//			Utils.logThrows( LOGGER, Level.WARNING, ex);
//		}
	}

	public void addView( EditView view) {
		mViews.add( view);
	}

	@Override
	public void baseAdded( ModelEvent event) {
//		fireViewChanged( getView( event.getPart()));
	}

	@Override
	public void baseChanged( ModelEvent event) {
//		fireViewChanged( getView( event.getPart()));
	}

	@Override
	public void baseRemoved( ModelEvent event) {
//		fireViewChanged( getView( event.getPart()));
	}

	@Override
	public void baseUpdate( ModelEvent event) {
//		fireViewChanged( getView( event.getPart()));
	}

	public EditView createView( ABase base) {
		EditView view = new EditView( this, base);
		addView( view);
		return view;
	}

	public EditModel getModel() {
		return mModel;
	}

	public EditView getView( ABase base) {
		for (EditView view : mViews) {
			if (Utils.equals( base, view.getBase())) {
				return view;
			}
		}
		return null;
	}

	//	private List<IMasterListener> mListener = new ArrayList<IMasterListener>();
//	public void addMasterListener( IMasterListener l) {
//		if (!mListener.contains( l)) {
//			mListener.add( l);
//		}
//	}
//
//	private void fireViewChanged( EditView view) {
//		MasterEvent event = new MasterEvent( this, view);
//		for (IMasterListener l : mListener) {
//			l.viewChanged( event);
//		}
//	}
//
//	public void removeMasterManagerListener( IMasterListener l) {
//		mListener.remove( l);
//	}
//
	public void removeView( ABase base) {
		EditView view = getView( base);
		if (view != null) {
//			view.removeListener();
			mViews.remove( view);
		}
	}

	public void setActive( ABase base) {
		mModel.setActive( base);
	}

	private static class BaseAction implements Runnable {
		private EditController mCtrl;
		private ABase mBase;

		public BaseAction( EditController ctrl, ABase base) {
			mCtrl = ctrl;
			mBase = base;
		}

		@Override
		public void run() {
			mCtrl.setActive( mBase);
		}
	}
}
