package de.doerl.hqm.base.dispatch;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.AMember;
import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.ARequirement;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemRequirement;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.FQuestTaskDeath;
import de.doerl.hqm.base.FQuestTaskItemsConsume;
import de.doerl.hqm.base.FQuestTaskItemsConsumeQDS;
import de.doerl.hqm.base.FQuestTaskItemsCrafting;
import de.doerl.hqm.base.FQuestTaskItemsDetect;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FQuestTaskReputationKill;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FRepeatInfo;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.utils.ToString;
import de.doerl.hqm.utils.Utils;

public abstract class AHQMWorker<T, U> implements IHQMWorker<T, U> {
	private static final Logger LOGGER = Logger.getLogger( AHQMWorker.class.getName());

	protected T doBase( ABase base, U p) {
		Utils.log( LOGGER, Level.WARNING, "{0} missing handler for {1}", ToString.clsName( this), base);
		return null;
	}

	protected T doCategory( ACategory<? extends ANamed> cat, U p) {
		return doBase( cat, p);
	}

	protected T doMember( AMember member, U p) {
		return doNamed( member, p);
	}

	protected T doNamed( ANamed named, U p) {
		return doBase( named, p);
	}

	protected T doRequirement( ARequirement req, U p) {
		return doBase( req, p);
	}

	protected T doTask( AQuestTask task, U p) {
		return doNamed( task, p);
	}

	protected T doTaskItems( AQuestTaskItems task, U p) {
		return doTask( task, p);
	}

	@Override
	public T forFluidRequirement( FFluidRequirement fluid, U p) {
		return doRequirement( fluid, p);
	}

	@Override
	public T forGroup( FGroup grp, U p) {
		return doNamed( grp, p);
	}

	@Override
	public T forGroupTier( FGroupTier tier, U p) {
		return doMember( tier, p);
	}

	@Override
	public T forGroupTierCat( FGroupTierCat cat, U p) {
		return doCategory( cat, p);
	}

	@Override
	public T forHQM( FHqm hqm, U p) {
		return doBase( hqm, p);
	}

	@Override
	public T forItemRequirement( FItemRequirement item, U p) {
		return doRequirement( item, p);
	}

	@Override
	public T forLocation( FLocation loc, U p) {
		return doNamed( loc, p);
	}

	@Override
	public T forMarker( FMarker mark, U p) {
		return doNamed( mark, p);
	}

	@Override
	public T forMob( FMob mob, U p) {
		return doNamed( mob, p);
	}

	@Override
	public T forQuest( FQuest quest, U p) {
		return doNamed( quest, p);
	}

	@Override
	public T forQuestSet( FQuestSet set, U p) {
		return doMember( set, p);
	}

	@Override
	public T forQuestSetCat( FQuestSetCat cat, U p) {
		return doCategory( cat, p);
	}

	@Override
	public T forRepeatInfo( FRepeatInfo info, U p) {
		return doBase( info, p);
	}

	@Override
	public T forReputation( FReputation rep, U p) {
		return doMember( rep, p);
	}

	@Override
	public T forReputationCat( FReputationCat cat, U p) {
		return doCategory( cat, p);
	}

	@Override
	public T forReputationReward( FReputationReward rr, U p) {
		return doBase( rr, p);
	}

	@Override
	public T forSetting( FSetting rs, U p) {
		return doBase( rs, p);
	}

	@Override
	public T forTaskDeath( FQuestTaskDeath task, U p) {
		return doTask( task, p);
	}

	@Override
	public T forTaskItemsConsume( FQuestTaskItemsConsume task, U p) {
		return doTaskItems( task, p);
	}

	@Override
	public T forTaskItemsConsumeQDS( FQuestTaskItemsConsumeQDS task, U p) {
		return doTaskItems( task, p);
	}

	@Override
	public T forTaskItemsCrafting( FQuestTaskItemsCrafting task, U p) {
		return doTaskItems( task, p);
	}

	@Override
	public T forTaskItemsDetect( FQuestTaskItemsDetect task, U p) {
		return doTaskItems( task, p);
	}

	@Override
	public T forTaskLocation( FQuestTaskLocation task, U p) {
		return doTask( task, p);
	}

	@Override
	public T forTaskMob( FQuestTaskMob task, U p) {
		return doTask( task, p);
	}

	@Override
	public T forTaskReputationKill( FQuestTaskReputationKill task, U p) {
		return doTask( task, p);
	}

	@Override
	public T forTaskReputationTarget( FQuestTaskReputationTarget task, U p) {
		return doTask( task, p);
	}
}
