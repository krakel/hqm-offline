package de.doerl.hqm.base;

import java.util.Vector;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FGroupCat extends ACategory<FGroup> {
	FGroupCat( FHqm parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forGroupCat( this, p);
	}

	@Override
	public FGroup createMember( String name) {
		FGroup reward = new FGroup( this, name);
		addMember( reward);
		return reward;
	}

	@Override
	public Vector<FGroup> getArr() {
		return mArr;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.GROUP_CAT;
	}

	@Override
	public String getNodeName() {
		return "Groups";
	}
}
