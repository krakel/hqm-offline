package de.doerl.hqm.base;

import java.util.ArrayList;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOfGroup;
import de.doerl.hqm.quest.ElementTyp;

public final class FGroup extends AUUID implements IElement {
	private static final String BASE = "grp";
	public final FGroupTier mParentTier;
	public Integer mLimit;
	public ArrayList<FItemStack> mStacks = new ArrayList<>();

	FGroup( FGroupTier parent) {
		super( BASE, MaxIdOfGroup.get( parent));
		mParentTier = parent;
	}

	FGroup( FGroupTier parent, int id) {
		super( BASE, id);
		mParentTier = parent;
	}

	public static int fromIdent( String ident) {
		return AIdent.fromIdent( BASE, ident);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forGroup( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.GROUP;
	}

	@Override
	public FGroupTier getParent() {
		return mParentTier;
	}

	@Override
	public boolean isFirst() {
		return ABase.isFirst( mParentTier.mGroups, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentTier.mGroups, this);
	}

	@Override
	void localeDefault( LocaleInfo info) {
		info.mInfo1 = "New Group";
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentTier.mGroups, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentTier.mGroups, this);
	}

	@Override
	public void remove() {
		ABase.remove( mParentTier.mGroups, this);
	}
}
