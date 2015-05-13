package de.doerl.hqm.base;

import java.util.Vector;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FGroup extends AMember<FGroup> {
	public final FParameterInt mID = new FParameterInt( this, "ID");
	public final FParameterInt mTierID = new FParameterInt( this, "GroupTier");
	public final FParameterInt mLimit = new FParameterInt( this, "Limit");;
	public final Vector<FParameterStack> mStacks = new Vector<FParameterStack>();

	public FGroup( FGroups parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forGroup( this, p);
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.GROUP;
	}
}
