package de.doerl.hqm.view;

import java.awt.Window;

import de.doerl.hqm.base.AQuestTask;

class TaskBoxEmpty extends ATaskBox {
	private static final long serialVersionUID = -2015055814561013941L;

	public TaskBoxEmpty() {
	}

	@Override
	public AQuestTask getTask() {
		return null;
	}

	@Override
	public boolean onAction( Window owner) {
		return false;
	}

	@Override
	public void update() {
	}
}
