package de.doerl.hqm.base;

import java.util.Vector;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;

public final class FGroupTierCat extends ACategory<FGroupTier> {
	FGroupTierCat( FHqm parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forGroupTierCat( this, p);
	}

	@Override
	public Vector<FGroupTier> asVector() {
		return new Vector<>( mArr);
	}

	public FGroupTier createGroupTier( int id) {
		FGroupTier tier = new FGroupTier( this, id);
		addMember( tier);
		return tier;
	}

	@Override
	public FGroupTier createMember() {
		FGroupTier tier = new FGroupTier( this);
		addMember( tier);
		return tier;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.GROUP_TIER_CAT;
	}

	@Override
	public String getNodeName() {
		return "Group Tiers";
	}
}
