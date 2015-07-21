package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.data.FData;
import de.doerl.hqm.base.data.FPlayer;
import de.doerl.hqm.base.data.FPlayerStats;
import de.doerl.hqm.base.data.FQuestDataTaskDeath;
import de.doerl.hqm.base.data.FQuestDataTaskItems;
import de.doerl.hqm.base.data.FQuestDataTaskLocation;
import de.doerl.hqm.base.data.FQuestDataTaskMob;
import de.doerl.hqm.base.data.FQuestDataTaskReputationKill;
import de.doerl.hqm.base.data.FQuestDataTaskReputationTarget;
import de.doerl.hqm.base.data.FTeam;
import de.doerl.hqm.base.data.FTeamStats;

public interface IDataWorker<T, U> extends IWorker {
	T forData( FData data, U p);

	T forDataTaskDeath( FQuestDataTaskDeath task, U p);

	T forDataTaskItems( FQuestDataTaskItems task, U p);

	T forDataTaskLocation( FQuestDataTaskLocation task, U p);

	T forDataTaskMob( FQuestDataTaskMob task, U p);

	T forDataTaskReputationKill( FQuestDataTaskReputationKill task, U p);

	T forDataTaskReputationTarget( FQuestDataTaskReputationTarget task, U p);

	T forPlayer( FPlayer player, U p);

	T forPlayerStats( FPlayerStats stats, U p);

	T forTeam( FTeam team, U p);

	T forTeamStats( FTeamStats stats, U p);
}
