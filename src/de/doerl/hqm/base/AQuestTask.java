package de.doerl.hqm.base;

public abstract class AQuestTask extends ANamed {
	public final FQuest mParentQuest;
	public final FParameterString mDesc = new FParameterString( this, "Description");

	AQuestTask( FQuest parent, String name) {
		super( name);
		mParentQuest = parent;
	}

	@Override
	public FQuest getParent() {
		return mParentQuest;
	}
}
