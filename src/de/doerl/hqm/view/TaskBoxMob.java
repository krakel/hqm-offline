package de.doerl.hqm.view;

import java.awt.Window;

import de.doerl.hqm.base.FQuestTaskMob;

class TaskBoxMob extends ATaskBox {
	private static final long serialVersionUID = 410081438393816262L;
	private FQuestTaskMob mTask;

	public TaskBoxMob( FQuestTaskMob task) {
		mTask = task;
	}

	@Override
	public FQuestTaskMob getTask() {
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
