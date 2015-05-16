package de.doerl.hqm.ui;

import java.util.ArrayList;
import java.util.List;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IRefreshListener;
import de.doerl.hqm.medium.RefreshEvent;
import de.doerl.hqm.model.EditModel;

public class EditCallback implements ICallback {
	private List<IRefreshListener> mRefreshListener = new ArrayList<IRefreshListener>();
	private EditFrame mFrame;
	private EditModel mModel;

	public EditCallback( EditFrame frame) {
		mFrame = frame;
		mModel = frame.getModel();
	}

	@Override
	public void addRefreshListener( IRefreshListener l) {
		mRefreshListener.add( l);
	}

	@Override
	public boolean askOverwrite() {
		return WarnDialogs.askOverwrite( mFrame);
	}

	@Override
	public boolean canOpenDialog() {
		return true;
	}

	@Override
	public boolean canOpenHQM() {
		return true;
	}

	protected void fireActionUpdate() {
		RefreshEvent event = new RefreshEvent();
		for (IRefreshListener l : mRefreshListener) {
			l.updateAction( event);
		}
	}

	@Override
	public boolean isModifiedHQM() {
		return false;
	}

	@Override
	public void openDialogAction( String location) {
	}

	@Override
	public void openHQMAction( FHqm hqm) {
		mModel.loadHQM( hqm);
		mFrame.showHqm( hqm);
//		mModel.getUndoable().getUndoMgr().fireChangeEvent();
	}

	@Override
	public void removeRefreshListener( IRefreshListener l) {
		mRefreshListener.remove( l);
	}

	@Override
	public void savedHQMAction() {
//		mModel.getUndoable().getUndoMgr().markSaved();
//		FHqm def = mModel.getHQM();
//		DescriptionManager.changed( mModel.getDescription());
		fireActionUpdate();
	}

	@Override
	public FHqm updateHQM() {
		return null;
	}
}
