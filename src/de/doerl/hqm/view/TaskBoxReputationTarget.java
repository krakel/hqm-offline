package de.doerl.hqm.view;

import java.awt.Window;

import de.doerl.hqm.base.FQuestTaskReputationTarget;

class TaskBoxReputationTarget extends ATaskBox {
	private static final long serialVersionUID = -7259046301859360457L;
	private FQuestTaskReputationTarget mTask;

	public TaskBoxReputationTarget( FQuestTaskReputationTarget task) {
		mTask = task;
	}

	@Override
	public FQuestTaskReputationTarget getTask() {
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
