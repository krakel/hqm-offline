package de.doerl.hqm.view;

import java.awt.Window;

import de.doerl.hqm.base.FQuestTaskLocation;

class TaskBoxLocation extends ATaskBox {
	private static final long serialVersionUID = -5352886896563883981L;
	private FQuestTaskLocation mTask;

	public TaskBoxLocation( FQuestTaskLocation task) {
		mTask = task;
	}

	@Override
	public FQuestTaskLocation getTask() {
		return mTask;
	}

	@Override
	public boolean onAction( Window owner) {
		return false;
	}

	@Override
	public void update() {
	}
}
