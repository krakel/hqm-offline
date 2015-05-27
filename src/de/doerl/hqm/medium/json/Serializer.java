package de.doerl.hqm.medium.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupCat;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemRequirement;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FParameterStack;
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
import de.doerl.hqm.base.FReward;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.IStackWorker;
import de.doerl.hqm.base.dispatch.MarkerIndex;
import de.doerl.hqm.base.dispatch.QuestIndex;
import de.doerl.hqm.base.dispatch.QuestSetIndex;
import de.doerl.hqm.base.dispatch.ReputationIndex;
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

	private void doTask( AQuestTask task, String type) {
		mDst.print( "type", type);
		mDst.print( "name", task.mName);
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
		mDst.print( "name", grp.mName);
		mDst.print( "tierID", grp.mTierID);
		writeStacks( grp.mStacks, "stacks");
		mDst.endObject();
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier gt, Object p) {
		mDst.beginObject();
		mDst.print( "name", gt.mName);
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
		writeQuestSetCat( hqm.mQuestSetCat);
		writeQuests( hqm);
		writeReputations( hqm.mReputationCat);
		writeGroupTiers( hqm.mGroupTierCat);
		writeGroups( hqm.mGroupCat);
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
		mDst.print( "size", stk.getCount());
		mDst.print( "dmg", stk.getDamage());
		mDst.printNBT( "nbt", stk.getNBT());
		mDst.endObject();
		return null;
	}

	@Override
	public Object forLocation( FLocation loc, Object p) {
		mDst.beginObject();
		mDst.print( "name", loc.mName);
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
	public Object forMarker( FMarker mark, Object p) {
		mDst.beginObject();
		mDst.print( "name", mark.mName);
		mDst.print( "value", mark.mMark);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forMob( FMob mob, Object p) {
		mDst.beginObject();
		mDst.print( "name", mob.mName);
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
		mDst.print( "name", quest.mName);
		if (!quest.isDeleted()) {
			mDst.print( "description", quest.mDesc);
			mDst.print( "x", quest.mX);
			mDst.print( "y", quest.mY);
			mDst.print( "big", quest.mBig);
			mDst.print( "setID", QuestSetIndex.get( quest.mQuestSet));
			mDst.print( "icon", quest.mIcon);
			if (quest.mRequirements != null) {
				writeQuestArr( quest.mRequirements, "requirements");
			}
			if (quest.mOptionLinks != null) {
				writeQuestArr( quest.mOptionLinks, "optionLinks");
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
		}
		mDst.endObject();
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		mDst.beginObject();
		mDst.print( "name", set.mName);
		mDst.print( "decription", set.mDesc);
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
		mDst.print( "name", rep.mName);
		mDst.print( "id", rep.mID);
		mDst.print( "neutral", rep.mNeutral);
		writeMarkers( rep);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forReward( FReward rr, Object p) {
		mDst.beginObject();
		mDst.print( "reputation", rr.mRepID);
		mDst.print( "value", rr.mValue);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forSetting( FSetting rs, Object p) {
		mDst.beginObject();
		mDst.print( "reputationID", ReputationIndex.get( rs.mRep));
		mDst.printIf( "lowerID", MarkerIndex.get( rs.mLower));
		mDst.printIf( "upperID", MarkerIndex.get( rs.mUpper));
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
		mDst.print( "kills", task.mKills);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, Object p) {
		mDst.beginObject();
		doTask( task, "reputationTarget");
		mDst.beginArray( "settings");
		task.forEachSetting( this, mDst);
		mDst.endArray();
		mDst.endObject();
		return null;
	}

	@Override
	public void writeDst( FHqm hqm, ICallback cb) {
		hqm.accept( this, null);
	}

	private void writeGroups( FGroupCat set) {
		mDst.beginArray( "groupCat");
		set.forEachMember( this, mDst);
		mDst.endArray();
		mDst.println();
	}

	private void writeGroupTiers( FGroupTierCat set) {
		mDst.beginArray( "groupTierCat");
		set.forEachMember( this, mDst);
		mDst.endArray();
		mDst.println();
	}

	private void writeMarkers( FReputation rep) {
		mDst.beginArray( "markers");
		rep.forEachMarker( this, mDst);
		mDst.endArray();
	}

	private void writeQuestArr( Vector<FQuest> arr, String key) {
		mDst.beginArray( key);
		for (FQuest req : arr) {
			if (req != null) {
				mDst.print( QuestIndex.get( req));
			}
		}
		mDst.endArray();
		mDst.println();
	}

	private void writeQuests( FHqm hqm) {
		mDst.beginArray( "quests");
		hqm.forEachQuest( this, mDst);
		mDst.endArray();
		mDst.println();
	}

	private void writeQuestSetCat( FQuestSetCat set) {
		mDst.beginArray( "questSetCat");
		set.forEachMember( this, mDst);
		mDst.endArray();
		mDst.println();
	}

	private void writeReputations( FReputationCat set) {
		mDst.beginArray( "reputationCat");
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
