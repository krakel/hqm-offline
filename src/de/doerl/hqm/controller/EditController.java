package de.doerl.hqm.controller;

import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.model.EditModel;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;

public class EditController implements IModelListener {
//	private static final Logger LOGGER = Logger.getLogger( EditController.class.getName());
	private EditModel mModel;

	public EditController( EditModel model) {
		model.addListener( this);
		mModel = model;
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

	public EditModel getModel() {
		return mModel;
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
