package de.doerl.hqm.ui.tree;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.dispatch.AHQMWorker;

public class TreeFactory extends AHQMWorker<Object, ElementTreeModel> {
	private static final TreeFactory WORKER = new TreeFactory();

	private TreeFactory() {
	}

	public static void get( ABase base, ElementTreeModel model) {
		base.accept( WORKER, model);
	}

	@Override
	protected Object doSet( ACategory<? extends ANamed> set, ElementTreeModel model) {
		NodeFactory.get( set, model);
		set.forEachMember( this, model);
		return null;
	}

	@Override
	public Object forGroup( FGroup grp, ElementTreeModel model) {
		NodeFactory.get( grp, model);
//		NodeFactory.get( grp.mID, model);
//		NodeFactory.get( grp.mTierID, model);
//		NodeFactory.get( grp.mLimit, model);
//		doParameterStacks( grp.mStacks, model);
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier gt, ElementTreeModel model) {
		NodeFactory.get( gt, model);
//		NodeFactory.get( gt.mColorID, model);
//		NodeFactory.get( gt.mWeights, model);
		return null;
	}

	@Override
	public Object forHQM( FHqm hqm, ElementTreeModel model) {
		NodeFactory.get( hqm, model);
		hqm.mQuestSets.accept( this, model);
		hqm.mRepSets.accept( this, model);
		hqm.forEachQuest( this, model);
		hqm.mGroupTiers.accept( this, model);
		hqm.mGroups.accept( this, model);
		return null;
	}

	@Override
	public Object forQuest( FQuest quest, ElementTreeModel model) {
		if (!quest.isDeleted()) {
			NodeFactory.get( quest, model);
//			NodeFactory.get( quest.mDesc, model);
//			NodeFactory.get( quest.mX, model);
//			NodeFactory.get( quest.mY, model);
//			NodeFactory.get( quest.mBig, model);
//			NodeFactory.get( quest.mSetID, model);
//			NodeFactory.get( quest.mIcon, model);
//			NodeFactory.get( quest.mRequirements, model);
//			NodeFactory.get( quest.mOptionLinks, model);
//			quest.getRepeatInfo().accept( this, model);
//			NodeFactory.get( quest.mTriggerTasks, model);
//			NodeFactory.get( quest.mTriggerType, model);
//			NodeFactory.get( quest.mReqUseModified, model);
//			NodeFactory.get( quest.mReqCount, model);
//			quest.forEachQuestTask( this, model);
//			doParameterStacks( quest.mRewards, model);
//			doParameterStacks( quest.mChoices, model);
//			quest.forEachReputationReward( this, model);
		}
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet qs, ElementTreeModel model) {
		NodeFactory.get( qs, model);
//		NodeFactory.get( qs.mDesc, model);
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, ElementTreeModel model) {
		NodeFactory.get( rep, model);
//		NodeFactory.get( rep.mNeutral, model);
//		rep.forEachMarker( this, model);
		return null;
	}
}
