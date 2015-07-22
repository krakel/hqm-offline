package de.doerl.hqm.base.data;

import java.util.Vector;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.dispatch.IDataWorker;

public final class FQuestData extends AGame implements IElement {
	public final FTeamData mParentTeamData;
	public final FQuest mQuest;
	final Vector<AQuestTaskData> mTaskDatas = new Vector<>();
	public boolean mCompleted;
	public boolean mClaimed;
	public boolean mAvailable;
	public int mTime;
	public Vector<Boolean> mReward = new Vector<>();

	FQuestData( FTeamData parentTeamData, FQuest quest) {
		mParentTeamData = parentTeamData;
		mQuest = quest;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return null;
	}

	public AQuestTaskData createTaskData( AQuestTask task) {
		AQuestTaskData taskData = TaskDataOfTask.get( this, task);
		mTaskDatas.add( taskData);
		return taskData;
	}

	@Override
	public FData getData() {
		return mParentTeamData.getData();
	}

	@Override
	public AGame getParent() {
		return mParentTeamData;
	}

	@Override
	public void remove() {
	}
}
