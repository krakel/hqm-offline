package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.data.FData;
import de.doerl.hqm.base.data.FTeamData;

public class TeamOfID extends ADataWorker<FTeamData, Integer> {
	private static final TeamOfID WORKER = new TeamOfID();

	private TeamOfID() {
	}

	public static FTeamData get( FData data, int id) {
		return data.forEachTeam( WORKER, id);
	}

//	public static FTeamData get( FData quest, String ident) {
//		return quest.forEachTeam( WORKER, AQuestTask.fromIdent( ident));
//	}
//
	@Override
	public FTeamData forTeam( FTeamData teamData, Integer id) {
		return teamData.getID() == id.intValue() ? teamData : null;
	}
}
