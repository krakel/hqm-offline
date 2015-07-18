package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FHqm;

public class GroupTierOfID extends AHQMWorker<FGroupTier, Integer> {
	private static final GroupTierOfID WORKER = new GroupTierOfID();

	private GroupTierOfID() {
	}

	public static FGroupTier get( FGroupTierCat set, int id) {
		return set.forEachMember( WORKER, id);
	}

	public static FGroupTier get( FGroupTierCat set, String ident) {
		return set.forEachMember( WORKER, FGroupTier.fromIdent( ident));
	}

	public static FGroupTier get( FHqm hqm, int id) {
		return get( hqm.mGroupTierCat, id);
	}

	@Override
	public FGroupTier forGroupTier( FGroupTier tier, Integer id) {
		return tier.getID() == id.intValue() ? tier : null;
	}
}
