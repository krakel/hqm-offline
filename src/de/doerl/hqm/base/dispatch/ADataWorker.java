package de.doerl.hqm.base.dispatch;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.data.AGame;
import de.doerl.hqm.base.data.AQuestTaskData;
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
import de.doerl.hqm.utils.ToString;
import de.doerl.hqm.utils.Utils;

public abstract class ADataWorker<T, U> implements IDataWorker<T, U> {
	private static final Logger LOGGER = Logger.getLogger( ADataWorker.class.getName());

	protected T doGame( AGame baseData, U p) {
		Utils.log( LOGGER, Level.WARNING, "{0} missing handler for {1}", ToString.clsName( this), baseData);
		return null;
	}

	protected T doTask( AQuestTaskData taskData, U p) {
		return doGame( taskData, p);
	}

	@Override
	public T forData( FData data, U p) {
		return doGame( data, p);
	}

	@Override
	public T forDataTaskDeath( FQuestTaskDataDeath taskData, U p) {
		return doTask( taskData, p);
	}

	@Override
	public T forDataTaskItems( FQuestTaskDataItems taskData, U p) {
		return doTask( taskData, p);
	}

	@Override
	public T forDataTaskLocation( FQuestTaskDataLocation taskData, U p) {
		return doTask( taskData, p);
	}

	@Override
	public T forDataTaskMob( FQuestTaskDataMob taskData, U p) {
		return doTask( taskData, p);
	}

	@Override
	public T forDataTaskReputationKill( FQuestTaskDataReputationKill taskData, U p) {
		return doTask( taskData, p);
	}

	@Override
	public T forDataTaskReputationTarget( FQuestTaskDataReputationTarget taskData, U p) {
		return doTask( taskData, p);
	}

	@Override
	public T forPlayer( FPlayer playerData, U p) {
		return doGame( playerData, p);
	}

	@Override
	public T forPlayerStats( FPlayerStats stats, U p) {
		return doGame( stats, p);
	}

	@Override
	public T forTeam( FTeamData teamData, U p) {
		return doGame( teamData, p);
	}

	@Override
	public T forTeamStats( FTeamStats stats, U p) {
		return doGame( stats, p);
	}
}
