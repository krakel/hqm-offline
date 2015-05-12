package de.doerl.hqm.base;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.quest.Visibility;

public final class FLocation extends ANamed {
	public final FQuestTaskLocation mParentTask;
	public final FParameterStack mIcon = new FParameterStack( this, "Icon");
	public final FParameterInt mX = new FParameterInt( this, "X");
	public final FParameterInt mY = new FParameterInt( this, "Y");
	public final FParameterInt mZ = new FParameterInt( this, "Z");
	public final FParameterInt mRadius = new FParameterInt( this, "Radius");
	public final FParameterEnum<Visibility> mVisibility = new FParameterEnum<Visibility>( this, "Visibility");
	public final FParameterInt mDim = new FParameterInt( this, "Dimension");

	public FLocation( FQuestTaskLocation parent, FItemStack icon, String name) {
		super( name);
		mParentTask = parent;
		mIcon.mValue = icon;
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
	public FQuestTaskLocation getParent() {
		return mParentTask;
	}
}
