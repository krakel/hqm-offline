package de.doerl.hqm.base.dispatch;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AMember;
import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.AParameter;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.ARequirement;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTiers;
import de.doerl.hqm.base.FGroups;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemRequirement;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FParameterBoolean;
import de.doerl.hqm.base.FParameterEnum;
import de.doerl.hqm.base.FParameterInt;
import de.doerl.hqm.base.FParameterInteger;
import de.doerl.hqm.base.FParameterIntegerArr;
import de.doerl.hqm.base.FParameterStack;
import de.doerl.hqm.base.FParameterString;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSets;
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
import de.doerl.hqm.base.FReputations;
import de.doerl.hqm.base.FReward;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.utils.ToString;
import de.doerl.hqm.utils.Utils;

public abstract class AHQMWorker<T, U> implements IHQMWorker<T, U> {
	private static final Logger LOGGER = Logger.getLogger( AHQMWorker.class.getName());

	protected T doBase( ABase base, U p) {
		Utils.log( LOGGER, Level.WARNING, "{0} missing handler for {1}", ToString.clsName( this), base);
		return null;
	}

	protected T doMember( AMember<? extends ANamed> member, U p) {
		return doNamed( member, p);
	}

	protected T doNamed( ANamed named, U p) {
		return doBase( named, p);
	}

	protected T doParameter( AParameter par, U p) {
		return doBase( par, p);
	}

	protected T doRequirement( ARequirement req, U p) {
		return doBase( req, p);
	}

	protected T doSet( ACategory<? extends ANamed> cat, U p) {
		return doBase( cat, p);
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
		return doMember( grp, p);
	}

	@Override
	public T forGroups( FGroups set, U p) {
		return doSet( set, p);
	}

	@Override
	public T forGroupTier( FGroupTier gt, U p) {
		return doMember( gt, p);
	}

	@Override
	public T forGroupTiers( FGroupTiers set, U p) {
		return doSet( set, p);
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
	public T forParameterBoolean( FParameterBoolean par, U p) {
		return doParameter( par, p);
	}

	@Override
	public T forParameterEnum( FParameterEnum<? extends Enum<?>> par, U p) {
		return doParameter( par, p);
	}

	public T forParameterInt( FParameterInt par, U p) {
		return doParameter( par, p);
	}

	@Override
	public T forParameterInteger( FParameterInteger par, U p) {
		return doParameter( par, p);
	}

	@Override
	public T forParameterIntegerArr( FParameterIntegerArr par, U p) {
		return doParameter( par, p);
	}

	@Override
	public T forParameterStack( FParameterStack par, U p) {
		return doParameter( par, p);
	}

	@Override
	public T forParameterString( FParameterString par, U p) {
		return doParameter( par, p);
	}

	@Override
	public T forQuest( FQuest quest, U p) {
		return doNamed( quest, p);
	}

	@Override
	public T forQuestSet( FQuestSet qs, U p) {
		return doMember( qs, p);
	}

	@Override
	public T forQuestSets( FQuestSets set, U p) {
		return doSet( set, p);
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
	public T forReputations( FReputations set, U p) {
		return doSet( set, p);
	}

	@Override
	public T forReward( FReward rr, U p) {
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
