package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.data.FData;
import de.doerl.hqm.base.data.FTeam;

public class TeamOfIdx extends ADataWorker<FTeam, Object> {
	private int mIndex;

	private TeamOfIdx( int index) {
		mIndex = index;
	}

	public static FTeam get( FData set, int idx) {
		TeamOfIdx worker = new TeamOfIdx( idx);
		return set.forEachTeam( worker, null);
	}

	@Override
	public FTeam forTeam( FTeam team, Object p) {
		if (mIndex == 0) {
			return team;
		}
		--mIndex;
		return null;
	}
}
