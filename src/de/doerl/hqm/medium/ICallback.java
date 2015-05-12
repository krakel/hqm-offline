package de.doerl.hqm.medium;

import de.doerl.hqm.base.FHqm;

public interface ICallback {
	void addRefreshListener( IRefreshListener l);

	boolean askOverwrite();

	boolean canOpenDialog();

	boolean canOpenHQM();

	boolean isModifiedHQM();

	void openDialogAction( String location);

	void openHQMAction( FHqm hqm);

	void removeRefreshListener( IRefreshListener l);

	void savedHQMAction();

	FHqm updateHQM();
}
