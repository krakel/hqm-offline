package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.data.FData;
import de.doerl.hqm.base.data.FTeamData;

public class TeamOfIdx extends ADataWorker<FTeamData, Object> {
	private int mIndex;

	private TeamOfIdx( int index) {
		mIndex = index;
	}

	public static FTeamData get( FData set, int idx) {
		TeamOfIdx worker = new TeamOfIdx( idx);
		return set.forEachTeam( worker, null);
	}

	@Override
	public FTeamData forTeam( FTeamData teamData, Object p) {
		if (mIndex == 0) {
			return teamData;
		}
		--mIndex;
		return null;
	}
}
