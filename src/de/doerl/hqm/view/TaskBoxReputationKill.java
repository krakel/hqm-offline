package de.doerl.hqm.view;

import java.awt.Window;

import de.doerl.hqm.base.FQuestTaskReputationKill;

class TaskBoxReputationKill extends ATaskBox {
	private static final long serialVersionUID = 8798185855270975248L;
	private FQuestTaskReputationKill mTask;
	private LeafLabel mKills = new LeafLabel( "");

	public TaskBoxReputationKill( FQuestTaskReputationKill task) {
		mTask = task;
		add( mKills);
	}

	@Override
	public FQuestTaskReputationKill getTask() {
		return mTask;
	}

	@Override
	public boolean onAction( Window owner) {
		return DialogIntegerField.update( mTask, owner);
	}

	@Override
	public void update() {
		int kills = mTask.mKills;
		mKills.setText( String.format( "You've killed 0 of %d %s", kills, kills <= 1 ? "player" : "players"));
	}
}
