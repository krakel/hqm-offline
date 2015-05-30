package de.doerl.hqm.base;

import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.TaskTyp;

public abstract class AQuestTask extends ANamed {
	public final FQuest mParentQuest;
	public String mDescr;

	AQuestTask( FQuest parent, String name) {
		super( name);
		mParentQuest = parent;
		mDescr = getTaskTyp().getDescr();
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_TASK;
	}

	@Override
	public FQuest getHierarchy() {
		return mParentQuest;
	}

	@Override
	public FQuest getParent() {
		return mParentQuest;
	}

	public abstract TaskTyp getTaskTyp();

	public void remove() {
		mParentQuest.removeTask( this);
	}
}
