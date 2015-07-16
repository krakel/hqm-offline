package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.TaskTyp;

public abstract class AQuestTask extends ANamed implements IElement {
	private static final String BASE = "task";
	public final FQuest mParentQuest;
	private int mID;
	public String mDescr;

	AQuestTask( FQuest parent, String name) {
		super( name);
		mParentQuest = parent;
		mDescr = getTaskTyp().getDescr();
		mID = MaxIdOf.getTasks( parent) + 1;
	}

	public static int fromIdent( String ident) {
		return fromIdent( BASE, ident);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_TASK;
	}

	public int getID() {
		return mID;
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

	public void setID( String ident) {
		if (ident != null) {
			int id = fromIdent( ident);
			if (id >= 0) {
				mID = id;
			}
		}
	}

	public String toIdent() {
		return toIdent( BASE, mID);
	}
}
