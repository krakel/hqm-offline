package de.doerl.hqm.base;

import java.util.HashMap;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;

public final class FMob extends AIdented implements IElement {
	private static final String BASE = "mob";
	public final FQuestTaskMob mParentTask;
	public FItemStack mIcon;
	public String mMob;
	public int mKills;
	public boolean mExact;
	private HashMap<String, String> mInfo = new HashMap<>();

	FMob( FQuestTaskMob parent) {
		super( BASE, MaxIdOf.getMob( parent) + 1);
		mParentTask = parent;
	}

	FMob( FQuestTaskMob parent, int id) {
		super( BASE, id);
		mParentTask = parent;
	}

	public static int fromIdent( String ident) {
		return AIdented.fromIdent( BASE, ident);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forMob( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.MOB;
	}

	@Override
	public String getName() {
		return mInfo.get( getHqm().mLang);
	}

	public String getName( String lang) {
		return mInfo.get( lang);
	}

	@Override
	public FQuestTaskMob getParent() {
		return mParentTask;
	}

	@Override
	public boolean isFirst() {
		return ABase.isFirst( mParentTask.mMobs, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentTask.mMobs, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentTask.mMobs, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentTask.mMobs, this);
	}

	@Override
	public void remove() {
		ABase.remove( mParentTask.mMobs, this);
	}

	@Override
	public void setName( String name) {
		mInfo.put( getHqm().mLang, name);
	}

	public void setName( String lang, String name) {
		mInfo.put( lang, name);
	}
}
