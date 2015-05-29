package de.doerl.hqm.view;

import java.awt.Window;

import de.doerl.hqm.base.FQuestTaskDeath;

class TaskBoxDeath extends ATaskBox {
	private static final long serialVersionUID = -6803523729510354404L;
	private FQuestTaskDeath mTask;

	public TaskBoxDeath( FQuestTaskDeath task) {
		mTask = task;
	}

	@Override
	public FQuestTaskDeath getTask() {
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
