package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.data.FData;
import de.doerl.hqm.base.data.FPlayer;
import de.doerl.hqm.base.data.FPlayerStats;
import de.doerl.hqm.base.data.FTeam;
import de.doerl.hqm.base.data.FTeamStats;

public interface IDataWorker<T, U> extends IWorker {
	T forData( FData data, U p);

	T forPlayer( FPlayer player, U p);

	T forPlayerStats( FPlayerStats stats, U p);

	T forTeam( FTeam team, U p);

	T forTeamStats( FTeamStats stats, U p);
}
