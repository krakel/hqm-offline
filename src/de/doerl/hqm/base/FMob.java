package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;

public final class FMob extends AIdent implements IElement {
	private static final String BASE = "mob";
	public final FQuestTaskMob mParentTask;
	public FItemStack mIcon;
	public String mMob;
	public int mKills;
	public boolean mExact;

	FMob( FQuestTaskMob parent) {
		super( BASE, MaxIdOf.getMob( parent) + 1);
		mParentTask = parent;
	}

	FMob( FQuestTaskMob parent, int id) {
		super( BASE, id);
		mParentTask = parent;
	}

	public static int fromIdent( String ident) {
		return AIdent.fromIdent( BASE, ident);
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
	void localeDefault( LocaleInfo info) {
		info.mInfo1 = "New Mob";
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
}
