package de.doerl.hqm.controller;

import java.util.Vector;

import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.model.EditModel;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.EditView;

public class EditController implements IModelListener {
//	private static final Logger LOGGER = Logger.getLogger( EditController.class.getName());
	private Vector<EditView> mViews = new Vector<EditView>();
	private EditModel mModel;

	public EditController( EditModel model) {
		model.addListener( this);
		mModel = model;
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

	public void removeView( ABase base) {
		EditView view = getView( base);
		if (view != null) {
//			view.removeListener();
			mViews.remove( view);
		}
	}

	public void setActive( ABase base) {
		SwingUtilities.invokeLater( new BaseAction( base));
	}

	private class BaseAction implements Runnable {
		private ABase mBase;

		public BaseAction( ABase base) {
			mBase = base;
		}

		@Override
		public void run() {
			mModel.setActive( mBase);
		}
	}
}
