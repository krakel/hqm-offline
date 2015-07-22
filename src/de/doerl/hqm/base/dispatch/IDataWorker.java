package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.data.FData;
import de.doerl.hqm.base.data.FPlayer;
import de.doerl.hqm.base.data.FPlayerStats;
import de.doerl.hqm.base.data.FQuestTaskDataDeath;
import de.doerl.hqm.base.data.FQuestTaskDataItems;
import de.doerl.hqm.base.data.FQuestTaskDataLocation;
import de.doerl.hqm.base.data.FQuestTaskDataMob;
import de.doerl.hqm.base.data.FQuestTaskDataReputationKill;
import de.doerl.hqm.base.data.FQuestTaskDataReputationTarget;
import de.doerl.hqm.base.data.FTeamData;
import de.doerl.hqm.base.data.FTeamStats;

public interface IDataWorker<T, U> extends IWorker {
	T forData( FData data, U p);

	T forDataTaskDeath( FQuestTaskDataDeath taskData, U p);

	T forDataTaskItems( FQuestTaskDataItems taskData, U p);

	T forDataTaskLocation( FQuestTaskDataLocation taskData, U p);

	T forDataTaskMob( FQuestTaskDataMob taskData, U p);

	T forDataTaskReputationKill( FQuestTaskDataReputationKill taskData, U p);

	T forDataTaskReputationTarget( FQuestTaskDataReputationTarget taskData, U p);

	T forPlayer( FPlayer playerData, U p);

	T forPlayerStats( FPlayerStats stats, U p);

	T forTeam( FTeamData teamData, U p);

	T forTeamStats( FTeamStats stats, U p);
}
