package de.doerl.hqm.base;

import java.util.HashMap;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.Visibility;

public final class FLocation extends ANamed implements IElement {
	public final FQuestTaskLocation mParentTask;
	public FItemStack mIcon;
	public int mX, mY, mZ;
	public int mRadius;
	public int mDim;
	public Visibility mVisibility;
	private HashMap<String, String> mInfo = new HashMap<>();

	FLocation( FQuestTaskLocation parent) {
		mParentTask = parent;
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
}
