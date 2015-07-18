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

	public String getDescr() {
		return getInfo().mInfo2;
	}

	public String getDescr( String lang) {
		return getInfo( lang).mInfo2;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_TASK;
	}

	@Override
	public String getName() {
		return getInfo().mInfo1;
	}

	public String getName( String lang) {
		return getInfo( lang).mInfo1;
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

	public void setDescr( String descr) {
		getInfo().mInfo2 = descr;
	}

	public void setDescr( String lang, String descr) {
		getInfo( lang).mInfo2 = descr;
	}

	@Override
	public void setName( String name) {
		getInfo().mInfo1 = name;
	}

	public void setName( String lang, String name) {
		getInfo( lang).mInfo1 = name;
	}
}
