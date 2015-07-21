package de.doerl.hqm.base.data;

public abstract class AQuestDataTask extends AGame {
	public final FQuestData mParentQuest;

	AQuestDataTask( FQuestData parentQuest) {
		mParentQuest = parentQuest;
	}

	@Override
	public FData getData() {
		return mParentQuest.getData();
	}

	@Override
	public FQuestData getParent() {
		return mParentQuest;
	}
}
