package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.TaskTyp;

public abstract class AQuestTask extends AIdent implements IElement {
	private static final String BASE = "task";
	public final FQuest mParentQuest;

	AQuestTask( FQuest parent) {
		super( BASE, MaxIdOf.getTasks( parent) + 1);
		mParentQuest = parent;
	}

	public static int fromIdent( String ident) {
		return AIdent.fromIdent( BASE, ident);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_TASK;
	}

	@Override
	public FQuest getParent() {
		return mParentQuest;
	}

	public abstract TaskTyp getTaskTyp();

	public boolean isFirst() {
		return ABase.isFirst( mParentQuest.mTasks, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentQuest.mTasks, this);
	}

	@Override
	void localeDefault( LocaleInfo info) {
		TaskTyp taskTyp = getTaskTyp();
		info.mInfo1 = taskTyp.name();
		info.mInfo2 = taskTyp.getDescr();
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentQuest.mTasks, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentQuest.mTasks, this);
	}

	public void remove() {
		ABase.remove( mParentQuest.mTasks, this);
	}
}
