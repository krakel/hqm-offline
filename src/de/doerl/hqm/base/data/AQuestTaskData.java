package de.doerl.hqm.base.data;


public abstract class AQuestTaskData extends AGame implements IElement {
	public final FQuestData mParentQuestData;
	public boolean mCompleted;

	AQuestTaskData( FQuestData parentQuestData) {
		mParentQuestData = parentQuestData;
	}

	@Override
	public FData getData() {
		return mParentQuestData.getData();
	}

	@Override
	public FQuestData getParent() {
		return mParentQuestData;
	}

	@Override
	public void remove() {
		AGame.remove( mParentQuestData.mTaskDatas, this);
	}
}
