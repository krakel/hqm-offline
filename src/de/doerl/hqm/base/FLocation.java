package de.doerl.hqm.base;

import java.util.HashMap;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.base.dispatch.MaxIdOf;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.Visibility;

public final class FLocation extends AIdented implements IElement {
	private static final String BASE = "loc";
	public final FQuestTaskLocation mParentTask;
	public FItemStack mIcon;
	public int mX, mY, mZ;
	public int mRadius;
	public int mDim;
	public Visibility mVisibility;
	private HashMap<String, String> mInfo = new HashMap<>();

	FLocation( FQuestTaskLocation parent) {
		super( BASE, MaxIdOf.getLocation( parent) + 1);
		mParentTask = parent;
	}

	FLocation( FQuestTaskLocation parent, int id) {
		super( BASE, id);
		mParentTask = parent;
	}

	public static int fromIdent( String ident) {
		return AIdented.fromIdent( BASE, ident);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forLocation( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.LOCATION;
	}

	@Override
	public String getName() {
		return mInfo.get( getHqm().mLang);
	}

	public String getName( String lang) {
		return mInfo.get( lang);
	}

	@Override
	public FQuestTaskLocation getParent() {
		return mParentTask;
	}

	@Override
	public boolean isFirst() {
		return ABase.isFirst( mParentTask.mLocations, this);
	}

	@Override
	public boolean isLast() {
		return ABase.isLast( mParentTask.mLocations, this);
	}

	@Override
	public void moveDown() {
		ABase.moveDown( mParentTask.mLocations, this);
	}

	@Override
	public void moveUp() {
		ABase.moveUp( mParentTask.mLocations, this);
	}

	@Override
	public void remove() {
		ABase.remove( mParentTask.mLocations, this);
	}

	@Override
	public void setName( String name) {
		mInfo.put( getHqm().mLang, name);
	}

	public void setName( String lang, String name) {
		mInfo.put( lang, name);
	}
}
