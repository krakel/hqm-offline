package de.doerl.hqm.view;

import java.awt.Window;

import de.doerl.hqm.base.FQuestTaskReputationKill;

class TaskBoxReputationKill extends ATaskBox {
	private static final long serialVersionUID = 8798185855270975248L;
	private FQuestTaskReputationKill mTask;

	public TaskBoxReputationKill( FQuestTaskReputationKill task) {
		mTask = task;
	}

	@Override
	public FQuestTaskReputationKill getTask() {
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
