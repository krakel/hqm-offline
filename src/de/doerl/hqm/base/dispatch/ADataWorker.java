package de.doerl.hqm.base.dispatch;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.data.AGame;
import de.doerl.hqm.base.data.AQuestDataTask;
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
import de.doerl.hqm.utils.ToString;
import de.doerl.hqm.utils.Utils;

public abstract class ADataWorker<T, U> implements IDataWorker<T, U> {
	private static final Logger LOGGER = Logger.getLogger( ADataWorker.class.getName());

	protected T doGame( AGame base, U p) {
		Utils.log( LOGGER, Level.WARNING, "{0} missing handler for {1}", ToString.clsName( this), base);
		return null;
	}

	protected T doTask( AQuestDataTask task, U p) {
		return doGame( task, p);
	}

	@Override
	public T forData( FData data, U p) {
		return doGame( data, p);
	}

	@Override
	public T forDataTaskDeath( FQuestDataTaskDeath task, U p) {
		return doTask( task, p);
	}

	@Override
	public T forDataTaskItems( FQuestDataTaskItems task, U p) {
		return doTask( task, p);
	}

	@Override
	public T forDataTaskLocation( FQuestDataTaskLocation task, U p) {
		return doTask( task, p);
	}

	@Override
	public T forDataTaskMob( FQuestDataTaskMob task, U p) {
		return doTask( task, p);
	}

	@Override
	public T forDataTaskReputationKill( FQuestDataTaskReputationKill task, U p) {
		return doTask( task, p);
	}

	@Override
	public T forDataTaskReputationTarget( FQuestDataTaskReputationTarget task, U p) {
		return doTask( task, p);
	}

	@Override
	public T forPlayer( FPlayer player, U p) {
		return doGame( player, p);
	}

	@Override
	public T forPlayerStats( FPlayerStats stats, U p) {
		return doGame( stats, p);
	}

	@Override
	public T forTeam( FTeam team, U p) {
		return doGame( team, p);
	}

	@Override
	public T forTeamStats( FTeamStats stats, U p) {
		return doGame( stats, p);
	}
}
