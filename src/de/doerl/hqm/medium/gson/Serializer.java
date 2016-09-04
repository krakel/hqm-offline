package de.doerl.hqm.medium.gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.AQuestTaskReputation;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemRequirement;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FLanguage;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.FQuestTaskDeath;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FQuestTaskReputationKill;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FRepeatInfo;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationBar;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.IndexOf;
import de.doerl.hqm.base.dispatch.SizeOf;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.quest.RepeatType;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.json.JsonWriter;
import de.doerl.hqm.utils.nbt.SerializerAtNEI;

class Serializer extends AHQMWorker<Object, JsonWriter> implements IToken {
	private static final Logger LOGGER = Logger.getLogger( Serializer.class.getName());
	private FLanguage mLang;
	private File mBase;

	public Serializer( File base, FLanguage lang) {
		mBase = base;
		mLang = lang;
	}

	@Override
	protected Object doTask( AQuestTask task, JsonWriter dst) {
		dst.print( TASK_TYPE, task.getTaskTyp());
		dst.printIf( TASK_NAME, task.getName( mLang));
		dst.printIf( TASK_DESC, task.getDescr( mLang));
		return null;
	}

	@Override
	protected Object doTaskItems( AQuestTaskItems task, JsonWriter dst) {
		dst.beginObject();
		doTask( task, dst);
		dst.beginArray( TASK_REQUIREMENTS);
		task.forEachRequirement( this, dst);
		dst.endArray();
		dst.endObject();
		return null;
	}

	@Override
	protected Object doTaskReputation( AQuestTaskReputation task, JsonWriter dst) {
		doTask( task, dst);
		dst.beginArray( TASK_REPUTATIONS);
		task.forEachSetting( this, dst);
		dst.endArray();
		return null;
	}

	@Override
	public Object forFluidRequirement( FFluidRequirement fluid, JsonWriter dst) {
		dst.beginObject();
		dst.print( REQUIREMENT_FLUID, fluid.getStack().getName());
		dst.print( REQUIREMENT_REQUIRED, fluid.mAmount);
		dst.endObject();
		return null;
	}

	@Override
	public Object forGroup( FGroup grp, JsonWriter dst) {
		dst.beginObject();
		dst.print( GROUP_UUID, grp.getUUID());
		dst.print( GROUP_NAME, grp.getName( mLang));
		dst.print( GROUP_LIMIT, grp.mLimit);
		writeStackArr( GROUP_STACKS, grp.mStacks, dst);
		dst.endObject();
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, JsonWriter dst) {
		dst.beginObject();
		dst.print( GROUP_TIER_NAME, tier.getName( mLang));
		dst.print( GROUP_TIER_COLOR, tier.mColorID);
		dst.printArr( GROUP_TIER_WEIGHTS, tier.mWeights);
		writeGroups( tier, dst);
		dst.endObject();
		return null;
	}

	@Override
	public Object forItemRequirement( FItemRequirement item, JsonWriter dst) {
		dst.beginObject();
		writeItemRequirement( item.getStack(), dst);
		if (item.mAmount > 1) {
			dst.print( REQUIREMENT_REQUIRED, item.mAmount);
		}
		if (item.mPrecision != ItemPrecision.PRECISE) {
			dst.print( REQUIREMENT_PRECISION, item.mPrecision);
		}
		dst.endObject();
		return null;
	}

	@Override
	public Object forLocation( FLocation loc, JsonWriter dst) {
		dst.beginObject();
		dst.print( LOCATION_NAME, loc.getName( mLang));
		writeIcon( LOCATION_ICON, loc.mIcon, dst);
		dst.print( LOCATION_X, loc.mX);
		dst.print( LOCATION_Y, loc.mY);
		dst.print( LOCATION_Z, loc.mZ);
		dst.print( LOCATION_RADIUS, loc.mRadius);
		dst.print( LOCATION_VISIBLE, loc.mVisibility);
		dst.print( LOCATION_DIM, loc.mDim);
		dst.endObject();
		return null;
	}

	@Override
	public Object forMarker( FMarker mark, JsonWriter dst) {
		dst.beginObject();
		dst.print( MARKER_NAME, mark.getName( mLang));
		dst.print( MARKER_VALUE, mark.mMark);
		dst.endObject();
		return null;
	}

	@Override
	public Object forMob( FMob mob, JsonWriter dst) {
		dst.beginObject();
		dst.print( MOB_NAME, mob.getName( mLang));
		writeIcon( MOB_ICON, mob.mIcon, dst);
		dst.print( MOB_OBJECT, mob.mMob);
		dst.print( MOB_COUNT, mob.mKills);
		dst.print( MOB_EXACT, mob.mExact);
		dst.endObject();
		return null;
	}

	@Override
	public Object forQuest( FQuest quest, JsonWriter dst) {
		dst.beginObject();
		dst.print( QUEST_UUID, quest.getUUID());
		dst.print( QUEST_NAME, quest.getName( mLang));
		dst.printIf( QUEST_DESC, quest.getDescr( mLang));
		dst.print( QUEST_X, quest.mX);
		dst.print( QUEST_Y, quest.mY);
		if (quest.mBig) {
			dst.print( QUEST_BIG, quest.mBig);
		}
		writeIcon( QUEST_ICON, quest.mIcon, dst);
		writeQuestArr( QUEST_PREREQUISITES, quest.mRequirements, dst);
		writeQuestArr( QUEST_OPTION_LINKS, quest.mOptionLinks, dst);
		if (quest.mRepeatInfo.mType != RepeatType.NONE) {
			quest.mRepeatInfo.accept( this, dst);
		}
		if (quest.mTriggerType != TriggerType.NONE) {
			dst.print( QUEST_TRIGGER_TYPE, quest.mTriggerType);
		}
		if (quest.mTriggerTasks > 0) {
			dst.print( QUEST_TRIGGER_TASKS, quest.mTriggerTasks);
		}
		dst.print( QUEST_PARENT_REQUIREMENT, quest.mCount);
		writeTasks( quest, dst);
		writeStackArr( QUEST_REWARD, quest.mRewards, dst);
		writeStackArr( QUEST_CHOICE, quest.mChoices, dst);
		writeCommands( quest, dst);
		writeRewards( quest, dst);
		dst.endObject();
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, JsonWriter dst2) {
		dst2.printValue( set.getName());
		File file = Medium.getFile( set.getName().replaceAll( " ", "_"), mBase);
		OutputStream os = null;
		try {
			os = new FileOutputStream( file);
			JsonWriter dst = new JsonWriter( os);
			writeQuestSet( set, dst);
			dst.flush();
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( os);
		}
		return null;
	}

	@Override
	public Object forRepeatInfo( FRepeatInfo info, JsonWriter dst) {
		dst.beginObject( QUEST_REPEAT_INFO);
		dst.print( REPEAT_INFO_TYPE, info.mType);
		if (info.mType.isUseTime()) {
			dst.print( REPEAT_INFO_DAYS, info.mTotal / 24);
			dst.print( REPEAT_INFO_HOURS, info.mTotal % 24);
		}
		dst.endObject();
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, JsonWriter dst) {
		dst.beginObject();
		dst.print( REPUTATION_UUID, rep.getUUID());
		dst.print( REPUTATION_NAME, rep.getName( mLang));
		dst.printIf( REPUTATION_NEUTRAL, rep.getDescr( mLang));
		writeMarkers( rep, dst);
		dst.endObject();
		return null;
	}

	@Override
	public Object forReputationBar( FReputationBar bar, JsonWriter dst) {
		dst.beginObject();
		dst.print( REPUTATION_UUID, bar.mRep.getUUID());
		dst.print( REPUTATION_BAR_X, bar.mX);
		dst.print( REPUTATION_BAR_Y, bar.mY);
		dst.endObject();
		return null;
	}

	@Override
	public Object forReputationReward( FReputationReward rr, JsonWriter dst) {
		dst.beginObject();
		dst.print( REWARD_REPUTATION, rr.mRep.getUUID());
		dst.print( REWARD_VALUE, rr.mValue);
		dst.endObject();
		return null;
	}

	@Override
	public Object forSetting( FSetting rs, JsonWriter dst) {
		dst.beginObject();
		dst.print( SETTING_REPUTATION, rs.mRep.getUUID());
		if (rs.mLower != null) {
			dst.print( SETTING_LOWER, IndexOf.getMarker( rs.mLower));
		}
		if (rs.mUpper != null) {
			dst.print( SETTING_UPPER, IndexOf.getMarker( rs.mUpper));
		}
		dst.print( SETTING_INVERTED, rs.mInverted);
		dst.endObject();
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, JsonWriter dst) {
		dst.beginObject();
		doTask( task, dst);
		dst.print( TASK_DEATHS, task.mDeaths);
		dst.endObject();
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, JsonWriter dst) {
		dst.beginObject();
		doTask( task, dst);
		dst.beginArray( TASK_LOCATIONS);
		task.forEachLocation( this, dst);
		dst.endArray();
		dst.endObject();
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, JsonWriter dst) {
		dst.beginObject();
		doTask( task, dst);
		dst.beginArray( TASK_MOBS);
		task.forEachMob( this, dst);
		dst.endArray();
		dst.endObject();
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, JsonWriter dst) {
		dst.beginObject();
		doTaskReputation( task, dst);
		dst.print( TASK_KILLS, task.mKills);
		dst.endObject();
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, JsonWriter dst) {
		dst.beginObject();
		doTaskReputation( task, dst);
		dst.endObject();
		return null;
	}

	private void writeBars( FQuestSet set, JsonWriter dst) {
		if (SizeOf.getBars( set) > 0) {
			dst.beginArray( QUEST_SET_BARS);
			set.forEachBar( this, dst);
			dst.endArray();
		}
	}

	private void writeCommands( FQuest quest, JsonWriter dst) {
		if (quest.mCommands.size() > 0) {
			dst.beginArray( QUEST_COMMANDS);
			for (String cmd : quest.mCommands) {
				if (cmd != null) {
					dst.printValue( cmd);
				}
			}
			dst.endArray();
		}
	}

	void writeDst( FHqm hqm) {
		Medium.saveTxt( hqm.getDescr(), Medium.getFile( Medium.DESCRIPTION_FILE, mBase));
		writeReputationCat( hqm.mReputationCat, Medium.getFile( Medium.REPUTATION_FILE, mBase));
		writeQuestSetCat( hqm.mQuestSetCat, Medium.getFile( Medium.SET_FILE, mBase));
		writeGroupTierCat( hqm.mGroupTierCat, Medium.getFile( Medium.BAG_FILE, mBase));
	}

	private void writeGroups( FGroupTier tier, JsonWriter dst) {
		dst.beginArray( GROUP_TIER_GROUPS);
		tier.forEachGroup( this, dst);
		dst.endArray();
	}

	private void writeGroupTierCat( FGroupTierCat cat, File file) {
		OutputStream os = null;
		try {
			os = new FileOutputStream( file);
			JsonWriter dst = new JsonWriter( os);
			writeGroupTierCats( cat, dst);
			dst.flush();
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( os);
		}
	}

	private void writeGroupTierCats( FGroupTierCat cat, JsonWriter dst) {
		dst.beginArray();
		cat.forEachMember( this, dst);
		dst.endArray();
	}

	private void writeIcon( String key, FItemStack icon, JsonWriter dst) {
		if (icon != null) {
			dst.beginObject( key);
			writeItemStack( icon, dst);
			dst.endObject();
		}
	}

	private void writeItemRequirement( FItemStack stk, JsonWriter dst) {
		dst.beginObject( REQUIREMENT_ITEM);
		writeItemStack( stk, dst);
		dst.endObject();
	}

	private void writeItemStack( FItemStack stk, JsonWriter dst) {
		dst.print( ITEM_ID, stk.getName());
		int dmg = stk.getDamage();
		if (dmg > 0) {
			dst.print( ITEM_DAMAGE, dmg);
		}
		int size = stk.getStackSize();
		if (size > 1) {
			dst.print( ITEM_SIZE, size);
		}
		String nbt = SerializerAtNEI.write( stk.getNBT());
		if (Utils.validString( nbt)) {
			dst.print( ITEM_NBT, nbt);
		}
	}

	private void writeMarkers( FReputation rep, JsonWriter dst) {
		dst.beginArray( REPUTATION_MARKERS);
		rep.forEachMarker( this, dst);
		dst.endArray();
	}

	private void writeQuestArr( String key, ArrayList<FQuest> arr, JsonWriter dst) {
		if (arr != null && !arr.isEmpty()) {
			dst.beginArray( key);
			for (FQuest quest : arr) {
				if (quest != null) {
					dst.printValue( quest.getUUID());
				}
			}
			dst.endArray();
		}
	}

	private void writeQuests( FQuestSet set, JsonWriter dst) {
		dst.beginArray( QUEST_SET_QUESTS);
		set.forEachQuest( this, dst);
		dst.endArray();
	}

	private void writeQuestSet( FQuestSet set, JsonWriter dst) {
		dst.beginObject();
		dst.print( QUEST_SET_NAME, set.getName( mLang));
		dst.printIf( QUEST_SET_DECR, set.getDescr( mLang));
		writeQuests( set, dst);
		writeBars( set, dst);
		dst.endObject();
	}

	private void writeQuestSetCat( FQuestSetCat cat, File file) {
		OutputStream os = null;
		try {
			os = new FileOutputStream( file);
			JsonWriter dst = new JsonWriter( os);
			writeQuestSetCats( cat, dst);
			dst.flush();
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( os);
		}
	}

	private void writeQuestSetCats( FQuestSetCat cat, JsonWriter dst) {
		dst.beginArray();
		cat.forEachMember( this, dst);
		dst.endArray();
	}

	private void writeReputationCat( FReputationCat cat, File file) {
		OutputStream os = null;
		try {
			os = new FileOutputStream( file);
			JsonWriter dst = new JsonWriter( os);
			writeReputationCats( cat, dst);
			dst.flush();
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( os);
		}
	}

	private void writeReputationCats( FReputationCat cat, JsonWriter dst) {
		dst.beginArray();
		cat.forEachMember( this, dst);
		dst.endArray();
	}

	private void writeRewards( FQuest quest, JsonWriter dst) {
		if (SizeOf.getReward( quest) > 0) {
			dst.beginArray( QUEST_REP_REWRDS);
			quest.forEachReward( this, dst);
			dst.endArray();
		}
	}

	private void writeStackArr( String key, ArrayList<FItemStack> arr, JsonWriter dst) {
		if (arr != null && !arr.isEmpty()) {
			dst.beginArray( key);
			for (FItemStack stk : arr) {
				dst.beginObject();
				writeItemStack( stk, dst);
				dst.endObject();
			}
			dst.endArray();
		}
	}

	private void writeTasks( FQuest quest, JsonWriter dst) {
		if (SizeOf.getTasks( quest) > 0) {
			dst.beginArray( QUEST_TASKS);
			quest.forEachTask( this, dst);
			dst.endArray();
		}
	}
}
