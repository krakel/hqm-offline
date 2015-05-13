package de.doerl.hqm.medium.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.AQuestTaskReputation;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTiers;
import de.doerl.hqm.base.FGroups;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemRequirement;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FParameterStack;
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
import de.doerl.hqm.base.FReputationMarker;
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.FReputationSetting;
import de.doerl.hqm.base.FReputations;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.IStackWorker;
import de.doerl.hqm.base.dispatch.QuestSetIndex;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IHqmWriter;
import de.doerl.hqm.quest.TriggerType;

class Serializer extends AHQMWorker<Object, Object> implements IHqmWriter, IStackWorker<Object, Object> {
	private JsonWriter mDst;

	public Serializer( OutputStream dst) throws IOException {
		mDst = new JsonWriter( dst);
	}

	@Override
	public void closeDst() {
		mDst.flush();
		mDst.close();
	}

	private void doReputation( AQuestTaskReputation task, String type) {
		doTask( task, type);
		mDst.beginArray( "settings");
		task.forEachSetting( this, mDst);
		mDst.endArray();
	}

	private void doTask( AQuestTask task, String type) {
		mDst.print( "type", type);
		mDst.print( "name", task.getName());
		mDst.print( "description", task.mDesc);
	}

	private void doTaskItem( AQuestTaskItems task, String type) {
		mDst.beginObject();
		doTask( task, type);
		mDst.beginArray( "requirements");
		task.forEachRequirement( this, mDst);
		mDst.endArray();
		mDst.endObject();
	}

	@Override
	public Object forFluidRequirement( FFluidRequirement fluid, Object p) {
		mDst.beginObject();
		mDst.print( "fluid", fluid.getStack());
		mDst.print( "precision", fluid.mPrecision);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forFluidStack( FFluidStack stk, Object p) {
		mDst.printNBT( stk.getNBT());
		return null;
	}

	@Override
	public Object forGroup( FGroup grp, Object p) {
		mDst.beginObject();
		mDst.print( "id", grp.mID);
		mDst.print( "name", grp.getName());
		mDst.print( "tierID", grp.mTierID);
		writeStacks( grp.mStacks, "stacks");
		mDst.endObject();
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier gt, Object p) {
		mDst.beginObject();
		mDst.print( "name", gt.getName());
		mDst.print( "color", gt.mColorID);
		mDst.print( "weights", gt.mWeights);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forHQM( FHqm hqm, Object p) {
		mDst.beginObject();
		mDst.print( "version", hqm.getVersion());
		mDst.printIf( "passcode", hqm.mPassCode);
		mDst.print( "decription", hqm.mDesc);
		writeSets( hqm.mQuestSets);
		writeReputations( hqm.mRepSets);
		writeGroupTiers( hqm.mGroupTiers);
		writeGroups( hqm.mGroups);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forItemRequirement( FItemRequirement item, Object p) {
		mDst.beginObject();
		mDst.print( "item", item.getStack());
		mDst.print( "required", item.getRequired());
		mDst.print( "precision", item.mPrecision);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forItemStack( FItemStack stk, Object p) {
		mDst.beginObject();
		mDst.print( "item", stk.getItem());
		mDst.print( "size", stk.getSize());
		mDst.print( "dmg", stk.getDmg());
		mDst.printNBT( "nbt", stk.getNBT());
		mDst.endObject();
		return null;
	}

	@Override
	public Object forLocation( FLocation loc, Object p) {
		mDst.beginObject();
		mDst.print( "name", loc.getName());
		mDst.print( "icon", loc.mIcon);
		mDst.print( "x", loc.mX);
		mDst.print( "y", loc.mY);
		mDst.print( "z", loc.mZ);
		mDst.print( "radius", loc.mRadius);
		mDst.print( "visible", loc.mVisibility);
		mDst.print( "dim", loc.mDim);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forMob( FMob mob, Object p) {
		mDst.beginObject();
		mDst.print( "name", mob.getName());
		mDst.print( "icon", mob.mIcon);
		mDst.print( "mob", mob.mMob);
		mDst.print( "count", mob.mKills);
		mDst.print( "exact", mob.mExact);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forQuest( FQuest quest, Object p) {
		mDst.beginObject();
		mDst.print( "name", quest.getName());
		mDst.print( "description", quest.mDesc);
		mDst.print( "x", quest.mX);
		mDst.print( "y", quest.mY);
		mDst.print( "big", quest.mBig);
		mDst.print( "setID", QuestSetIndex.get( quest.mParentSet));
		mDst.print( "icon", quest.mIcon);
		if (quest.mRequirements != null) {
			mDst.print( "requirements", quest.mRequirements);
		}
		if (quest.mOptionLinks != null) {
			mDst.print( "optionLinks", quest.mOptionLinks);
		}
		FRepeatInfo ri = quest.getRepeatInfo();
		if (ri != null) {
			ri.accept( this, mDst);
		}
		TriggerType tt = quest.mTriggerType.mValue;
		if (tt != null) {
			mDst.print( "triggerType", tt);
			if (tt.isUseTaskCount()) {
				mDst.print( "triggerTasks", quest.mTriggerTasks);
			}
		}
		if (quest.mReqUseModified.mValue) {
			mDst.print( "parentRequirementCount", quest.mReqCount);
		}
		writeTasks( quest);
		writeStacks( quest.mRewards, "reward");
		writeStacks( quest.mChoices, "choice");
		quest.forEachReputationReward( this, mDst);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet qs, Object p) {
		mDst.beginObject();
		mDst.print( "name", qs.getName());
		mDst.print( "decription", qs.mDesc);
		qs.forEachQuest( this, mDst);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forRepeatInfo( FRepeatInfo info, Object p) {
		mDst.printKey( "repeatInfo");
		mDst.beginObject();
		mDst.print( "type", info.mType);
		if (info.mType.mValue.isUseTime()) {
			mDst.print( "total", info.mTotal);
		}
		mDst.endObject();
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, Object p) {
		mDst.beginObject();
		mDst.print( "name", rep.getName());
		mDst.print( "id", rep.mID);
		mDst.print( "neutral", rep.mNeutral);
		writeMarkers( rep);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forReputationMarker( FReputationMarker mark, Object p) {
		mDst.beginObject();
		mDst.print( "name", mark.getName());
		mDst.print( "value", mark.mValue);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forReputationReward( FReputationReward rr, Object p) {
		mDst.beginObject();
		mDst.print( "reputation", rr.mRepID);
		mDst.print( "value", rr.mValue);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forSetting( FReputationSetting rs, Object p) {
		mDst.beginObject();
		mDst.print( "reputationID", rs.mRepID);
		mDst.printIf( "lowerID", rs.mLowerID);
		mDst.printIf( "upperID", rs.mUpperID);
		mDst.print( "inverted", rs.mInverted);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, Object p) {
		mDst.beginObject();
		doTask( task, "death");
		mDst.print( "deaths", task.mDeaths);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskItemsConsume( FQuestTaskItemsConsume task, Object p) {
		doTaskItem( task, "itemsConsume");
		return null;
	}

	@Override
	public Object forTaskItemsConsumeQDS( FQuestTaskItemsConsumeQDS task, Object p) {
		doTaskItem( task, "itemsConsumeQDS");
		return null;
	}

	@Override
	public Object forTaskItemsCrafting( FQuestTaskItemsCrafting task, Object p) {
		doTaskItem( task, "itemsCrafting");
		return null;
	}

	@Override
	public Object forTaskItemsDetect( FQuestTaskItemsDetect task, Object p) {
		doTaskItem( task, "itemsDetect");
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, Object p) {
		mDst.beginObject();
		doTask( task, "location");
		mDst.beginArray( "locations");
		task.forEachLocation( this, mDst);
		mDst.endArray();
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, Object p) {
		mDst.beginObject();
		doTask( task, "mob");
		mDst.beginArray( "mobs");
		task.forEachMob( this, mDst);
		mDst.endArray();
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, Object p) {
		mDst.beginObject();
		doReputation( task, "reputationKill");
		mDst.print( "kills", task.mKills);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, Object p) {
		mDst.beginObject();
		doReputation( task, "reputationTarget");
		mDst.endObject();
		return null;
	}

	@Override
	public void writeDst( FHqm hqm, ICallback cb) {
		hqm.accept( this, null);
	}

	private void writeGroups( FGroups set) {
		mDst.beginArray( "groups");
		set.forEachMember( this, mDst);
		mDst.endArray();
		mDst.println();
	}

	private void writeGroupTiers( FGroupTiers set) {
		mDst.beginArray( "groupTiers");
		set.forEachMember( this, mDst);
		mDst.endArray();
		mDst.println();
	}

	private void writeMarkers( FReputation rep) {
		mDst.beginArray( "markers");
		rep.forEachMarker( this, mDst);
		mDst.endArray();
	}

	private void writeReputations( FReputations set) {
		mDst.beginArray( "reputations");
		set.forEachMember( this, mDst);
		mDst.endArray();
		mDst.println();
	}

	private void writeSets( FQuestSets set) {
		mDst.beginArray( "questSets");
		set.forEachMember( this, mDst);
		mDst.endArray();
		mDst.println();
	}

	private void writeStacks( Vector<FParameterStack> stacks, String key) {
		mDst.beginArray( key);
		for (FParameterStack stk : stacks) {
			stk.mValue.accept( this, null);
		}
		mDst.endArray();
		mDst.println();
	}

	private void writeTasks( FQuest quest) {
		mDst.beginArray( "tasks");
		quest.forEachQuestTask( this, mDst);
		mDst.endArray();
		mDst.println();
	}
}
