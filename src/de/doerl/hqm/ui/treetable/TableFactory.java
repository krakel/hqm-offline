package de.doerl.hqm.ui.treetable;

import java.util.Vector;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AMember;
import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.ASet;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemRequirement;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FParameterStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
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
import de.doerl.hqm.base.FReputationMarker;
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.dispatch.AHQMWorker;

class TableFactory extends AHQMWorker<Object, TreeTableModel> {
	private static final TableFactory WORKER = new TableFactory();

	private TableFactory() {
	}

	public static void get( ABase base, TreeTableModel model) {
		base.accept( WORKER, model);
	}

	@Override
	protected Object doMember( AMember<? extends ANamed> member, TreeTableModel model) {
		RowFactory.get( member, model);
		return null;
	}

	private void doParameterStacks( Vector<FParameterStack> arr, TreeTableModel model) {
		for (FParameterStack stack : arr) {
			RowFactory.get( stack, model);
		}
	}

	@Override
	protected Object doSet( ASet<? extends ANamed> set, TreeTableModel model) {
		RowFactory.get( set, model);
		set.forEachMember( this, model);
		return null;
	}

	@Override
	public Object forFluidRequirement( FFluidRequirement fluid, TreeTableModel model) {
		RowFactory.get( fluid, model);
		RowFactory.get( fluid.getStack(), model);
		RowFactory.get( fluid.mPrecision, model);
		return null;
	}

	@Override
	public Object forGroup( FGroup grp, TreeTableModel model) {
		RowFactory.get( grp, model);
		RowFactory.get( grp.mID, model);
		RowFactory.get( grp.mTierID, model);
		RowFactory.get( grp.mLimit, model);
		doParameterStacks( grp.mStacks, model);
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier gt, TreeTableModel model) {
		RowFactory.get( gt, model);
		RowFactory.get( gt.mColorID, model);
		RowFactory.get( gt.mWeights, model);
		return null;
	}

	@Override
	public Object forHQM( FHqm hqm, TreeTableModel model) {
		RowFactory.get( hqm, model);
		hqm.mQuestSets.accept( this, model);
		hqm.mRepSets.accept( this, model);
		hqm.mGroupTiers.accept( this, model);
		hqm.mGroups.accept( this, model);
		return null;
	}

	@Override
	public Object forItemRequirement( FItemRequirement item, TreeTableModel model) {
		RowFactory.get( item, model);
		RowFactory.get( item.getStack(), model);
		RowFactory.get( item.getRequired(), model);
		RowFactory.get( item.mPrecision, model);
		return null;
	}

	@Override
	public Object forLocation( FLocation loc, TreeTableModel model) {
		RowFactory.get( loc, model);
		RowFactory.get( loc.mIcon, model);
		RowFactory.get( loc.mX, model);
		RowFactory.get( loc.mY, model);
		RowFactory.get( loc.mZ, model);
		RowFactory.get( loc.mRadius, model);
		RowFactory.get( loc.mVisibility, model);
		RowFactory.get( loc.mDim, model);
		return null;
	}

	@Override
	public Object forMob( FMob mob, TreeTableModel model) {
		RowFactory.get( mob, model);
		RowFactory.get( mob.mIcon, model);
		RowFactory.get( mob.mMob, model);
		RowFactory.get( mob.mKills, model);
		RowFactory.get( mob.mExact, model);
		return null;
	}

	@Override
	public Object forQuest( FQuest quest, TreeTableModel model) {
		RowFactory.get( quest, model);
		RowFactory.get( quest.mDesc, model);
		RowFactory.get( quest.mX, model);
		RowFactory.get( quest.mY, model);
		RowFactory.get( quest.mBig, model);
//		RowFactory.get( quest.mSetID, model);
		RowFactory.get( quest.mIcon, model);
		RowFactory.get( quest.mRequirements, model);
		RowFactory.get( quest.mOptionLinks, model);
		quest.getRepeatInfo().accept( this, model);
		RowFactory.get( quest.mTriggerTasks, model);
		RowFactory.get( quest.mTriggerType, model);
		RowFactory.get( quest.mReqUseModified, model);
		RowFactory.get( quest.mReqCount, model);
		quest.forEachQuestTask( this, model);
		doParameterStacks( quest.mRewards, model);
		doParameterStacks( quest.mChoices, model);
		quest.forEachReputationReward( this, model);
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet qs, TreeTableModel model) {
		RowFactory.get( qs, model);
		RowFactory.get( qs.mDesc, model);
		qs.forEachQuest( this, model);
		return null;
	}

	@Override
	public Object forRepeatInfo( FRepeatInfo info, TreeTableModel model) {
		RowFactory.get( info, model);
		RowFactory.get( info.mType, model);
		RowFactory.get( info.mTotal, model);
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, TreeTableModel model) {
		RowFactory.get( rep, model);
		RowFactory.get( rep.mNeutral, model);
		rep.forEachMarker( this, model);
		return null;
	}

	@Override
	public Object forReputationMarker( FReputationMarker mark, TreeTableModel model) {
		RowFactory.get( mark, model);
		RowFactory.get( mark.mValue, model);
		return null;
	}

	@Override
	public Object forReputationReward( FReputationReward rr, TreeTableModel model) {
		RowFactory.get( rr, model);
		RowFactory.get( rr.mRepID, model);
		RowFactory.get( rr.mValue, model);
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, TreeTableModel model) {
		RowFactory.get( task, model);
		RowFactory.get( task.mDesc, model);
		RowFactory.get( task.mDeaths, model);
		return null;
	}

	@Override
	public Object forTaskItemsConsume( FQuestTaskItemsConsume task, TreeTableModel model) {
		RowFactory.get( task, model);
		RowFactory.get( task.mDesc, model);
		task.forEachRequirement( this, model);
		return null;
	}

	@Override
	public Object forTaskItemsConsumeQDS( FQuestTaskItemsConsumeQDS task, TreeTableModel model) {
		RowFactory.get( task, model);
		RowFactory.get( task.mDesc, model);
		task.forEachRequirement( this, model);
		return null;
	}

	@Override
	public Object forTaskItemsCrafting( FQuestTaskItemsCrafting task, TreeTableModel model) {
		RowFactory.get( task, model);
		RowFactory.get( task.mDesc, model);
		task.forEachRequirement( this, model);
		return null;
	}

	@Override
	public Object forTaskItemsDetect( FQuestTaskItemsDetect task, TreeTableModel model) {
		RowFactory.get( task, model);
		RowFactory.get( task.mDesc, model);
		task.forEachRequirement( this, model);
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, TreeTableModel model) {
		RowFactory.get( task, model);
		RowFactory.get( task.mDesc, model);
		task.forEachLocation( this, model);
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, TreeTableModel model) {
		RowFactory.get( task, model);
		RowFactory.get( task.mDesc, model);
		task.forEachMob( this, model);
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, TreeTableModel model) {
		RowFactory.get( task, model);
		RowFactory.get( task.mDesc, model);
		RowFactory.get( task.mKills, model);
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, TreeTableModel model) {
		RowFactory.get( task, model);
		RowFactory.get( task.mDesc, model);
		return null;
	}
}
