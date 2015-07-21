package de.doerl.hqm.base.data;

import java.util.Vector;

import de.doerl.hqm.base.dispatch.IDataWorker;
import de.doerl.hqm.quest.TaskTyp;

public final class FQuestData extends AGame {
	public boolean mCompleted;
	public boolean mClaimed;
	public boolean mAvailable;
	public int mTime;
	public boolean[] mReward;
	final Vector<AQuestDataTask> mTasks = new Vector<>();

	FQuestData() {
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return null;
	}

	public AQuestDataTask createQuestDataTask( TaskTyp type) {
		AQuestDataTask task = null;
		switch (type) {
			case TASK_ITEMS_CONSUME:
				task = new FQuestDataTaskItems( this);
				break;
			case TASK_ITEMS_CRAFTING:
				task = new FQuestDataTaskItems( this);
				break;
			case TASK_LOCATION:
				task = new FQuestDataTaskLocation( this);
				break;
			case TASK_ITEMS_CONSUME_QDS:
				task = new FQuestDataTaskItems( this);
				break;
			case TASK_ITEMS_DETECT:
				task = new FQuestDataTaskItems( this);
				break;
			case TASK_MOB:
				task = new FQuestDataTaskMob( this);
				break;
			case TASK_DEATH:
				task = new FQuestDataTaskDeath( this);
				break;
			case TASK_REPUTATION_TARGET:
				task = new FQuestDataTaskReputationTarget( this);
				break;
			case TASK_REPUTATION_KILL:
				task = new FQuestDataTaskReputationKill( this);
				break;
		}
		mTasks.add( task);
		return task;
	}

	@Override
	public FData getData() {
		return null;
	}

	@Override
	public AGame getParent() {
		return null;
	}

	public void preRead( int playerCount) {
		mReward = new boolean[playerCount];
	}
}
