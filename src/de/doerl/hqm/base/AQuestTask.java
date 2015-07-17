package de.doerl.hqm.base;

import java.util.HashMap;

import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.TaskTyp;

public abstract class AQuestTask extends AIdented implements IElement {
	private static final String BASE = "task";
	public final FQuest mParentQuest;
	private HashMap<String, LangInfo> mInfo = new HashMap<>();

	AQuestTask( FQuest parent) {
		super( BASE, MaxIdOf.getTasks( parent) + 1);
		mParentQuest = parent;
	}

	public String getDescr() {
		return getInfo().mDescr;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_TASK;
	}

	private LangInfo getInfo() {
		String lang = getHqm().mLang;
		LangInfo info = mInfo.get( lang);
		if (info == null) {
			info = new LangInfo();
			mInfo.put( lang, info);
		}
		return info;
	}

	@Override
	public String getName() {
		return getInfo().mName;
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
		getInfo().mDescr = descr;
	}

	@Override
	public void setName( String name) {
		getInfo().mName = name;
	}

	private static class LangInfo {
		public String mName;
		public String mDescr;
	}
}
