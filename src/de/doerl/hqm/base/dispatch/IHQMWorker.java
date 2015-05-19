package de.doerl.hqm.base.dispatch;

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

public interface IHQMWorker<T, U> extends IWorker {
	T forFluidRequirement( FFluidRequirement fluid, U p);

	T forGroup( FGroup grp, U p);

	T forGroups( FGroups set, U p);

	T forGroupTier( FGroupTier gt, U p);

	T forGroupTiers( FGroupTiers set, U p);

	T forHQM( FHqm hqm, U p);

	T forItemRequirement( FItemRequirement item, U p);

	T forLocation( FLocation loc, U p);

	T forMarker( FMarker mark, U p);

	T forMob( FMob mob, U p);

	T forQuest( FQuest quest, U p);

	T forQuestSet( FQuestSet qs, U p);

	T forQuestSets( FQuestSets set, U p);

	T forRepeatInfo( FRepeatInfo info, U p);

	T forReputation( FReputation rep, U p);

	T forReputations( FReputations set, U p);

	T forReward( FReward rr, U p);

	T forSetting( FSetting rs, U p);

	T forTaskDeath( FQuestTaskDeath task, U p);

	T forTaskItemsConsume( FQuestTaskItemsConsume task, U p);

	T forTaskItemsConsumeQDS( FQuestTaskItemsConsumeQDS task, U p);

	T forTaskItemsCrafting( FQuestTaskItemsCrafting task, U p);

	T forTaskItemsDetect( FQuestTaskItemsDetect task, U p);

	T forTaskLocation( FQuestTaskLocation task, U p);

	T forTaskMob( FQuestTaskMob task, U p);

	T forTaskReputationKill( FQuestTaskReputationKill task, U p);

	T forTaskReputationTarget( FQuestTaskReputationTarget task, U p);
}
