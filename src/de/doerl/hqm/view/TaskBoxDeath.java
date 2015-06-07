package de.doerl.hqm.view;

import java.awt.Window;

import de.doerl.hqm.base.FQuestTaskDeath;

class TaskBoxDeath extends ATaskBox {
	private static final long serialVersionUID = -6803523729510354404L;
	private FQuestTaskDeath mTask;
	private LeafLabel mDeaths = new LeafLabel( "");

	public TaskBoxDeath( FQuestTaskDeath task) {
		mTask = task;
		add( mDeaths);
	}

	@Override
	public FQuestTaskDeath getTask() {
		return mTask;
	}

	@Override
	public boolean onAction( Window owner) {
		return DialogIntegerField.update( mTask, owner);
	}

	@Override
	public void update() {
		int deaths = mTask.mDeaths;
		mDeaths.setText( String.format( "You've died 0 of %d %s", deaths, deaths <= 1 ? "time" : "times"));
	}
}
