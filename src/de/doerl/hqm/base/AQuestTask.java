package de.doerl.hqm.base;

public abstract class AQuestTask extends ANamed {
	public final FQuest mParentQuest;
	public final FParameterString mDesc = new FParameterString( this);

	AQuestTask( FQuest parent, String name) {
		super( name);
		mParentQuest = parent;
	}

	@Override
	public FQuest getHierarchy() {
		return mParentQuest;
	}

	@Override
	public FQuest getParent() {
		return mParentQuest;
	}
}
