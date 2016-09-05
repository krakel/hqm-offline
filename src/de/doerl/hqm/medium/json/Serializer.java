package de.doerl.hqm.medium.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

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
import de.doerl.hqm.base.dispatch.SizeOf;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.json.JsonWriter;
import de.doerl.hqm.utils.nbt.SerializerAtJson;

class Serializer extends AHQMWorker<Object, Object> implements IToken {
	private static final String PATTERN_ITEM = "%s size(%d) dmg(%d)";
	private JsonWriter mDst;
	private FLanguage mLang;
	private boolean mMain;
	private boolean mDocu;

	public Serializer( OutputStream os, FLanguage lang, boolean withMain, boolean withDocu) throws IOException {
		mDst = new JsonWriter( os);
		mLang = lang;
		mMain = withMain;
		mDocu = withDocu;
	}

	private String createItem( FItemStack stk) {
		return String.format( PATTERN_ITEM, stk.getName(), stk.getStackSize(), stk.getDamage());
	}

	private void doTask( AQuestTask task) {
		mDst.print( TASK_ID, task.toIdent());
		if (mMain) {
			mDst.print( TASK_TYPE, task.getTaskTyp());
		}
		if (mDocu) {
			mDst.printIf( TASK_NAME, task.getName( mLang));
			mDst.printIf( TASK_DESC, task.getDescr( mLang));
		}
	}

	@Override
	protected Object doTaskItems( AQuestTaskItems task, Object p) {
		mDst.beginObject();
		doTask( task);
		if (mMain) {
			mDst.beginArray( TASK_REQUIREMENTS);
			task.forEachRequirement( this, null);
			mDst.endArray();
		}
		mDst.endObject();
		return null;
	}

	private void doTaskReputation( AQuestTaskReputation task) {
		doTask( task);
		if (mMain) {
			mDst.beginArray( TASK_REPUTATIONS);
			task.forEachSetting( this, null);
			mDst.endArray();
		}
	}

	public void flushDst() {
		mDst.flush();
	}

	@Override
	public Object forFluidRequirement( FFluidRequirement fluid, Object p) {
		mDst.beginObject();
		mDst.print( FLUID_OBJECT, fluid.getStack().getName());
		mDst.print( REQUIREMENT_REQUIRED, fluid.mAmount);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forGroup( FGroup grp, Object p) {
		mDst.beginObject();
		mDst.print( GROUP_ID, grp.toIdent());
		if (mDocu) {
			mDst.print( GROUP_NAME, grp.getName( mLang));
		}
		if (mMain) {
			mDst.print( GROUP_UUID, grp.getUUID());
			mDst.print( GROUP_LIMIT, grp.mLimit);
			writeStackArr( GROUP_STACKS, grp.mStacks);
		}
		mDst.endObject();
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, Object p) {
		mDst.beginObject();
		mDst.print( GROUP_TIER_ID, tier.toIdent());
		if (mDocu) {
			mDst.print( GROUP_TIER_NAME, tier.getName( mLang));
		}
		if (mMain) {
			mDst.print( GROUP_TIER_COLOR, tier.mColorID);
			mDst.printArr( GROUP_TIER_WEIGHTS, tier.mWeights);
		}
		writeGroups( tier);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forItemRequirement( FItemRequirement item, Object p) {
		mDst.beginObject();
		FItemStack stk = item.getStack();
		mDst.print( ITEM_OBJECT, createItem( stk));
		String nbt = SerializerAtJson.write( stk.getNBT());
		if (Utils.validString( nbt)) {
			mDst.print( ITEM_NBT, nbt);
		}
		mDst.print( REQUIREMENT_REQUIRED, item.mAmount);
		mDst.print( REQUIREMENT_PRECISION, item.mPrecision);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forLocation( FLocation loc, Object p) {
		mDst.beginObject();
		mDst.print( LOCATION_ID, loc.toIdent());
		if (mDocu) {
			mDst.print( LOCATION_NAME, loc.getName( mLang));
		}
		if (mMain) {
			writeIcon( LOCATION_ICON, loc.mIcon);
			mDst.print( LOCATION_X, loc.mX);
			mDst.print( LOCATION_Y, loc.mY);
			mDst.print( LOCATION_Z, loc.mZ);
			mDst.print( LOCATION_RADIUS, loc.mRadius);
			mDst.print( LOCATION_VISIBLE, loc.mVisibility);
			mDst.print( LOCATION_DIM, loc.mDim);
		}
		mDst.endObject();
		return null;
	}

	@Override
	public Object forMarker( FMarker mark, Object p) {
		mDst.beginObject();
		mDst.print( MARKER_ID, mark.toIdent());
		if (mDocu) {
			mDst.print( MARKER_NAME, mark.getName( mLang));
		}
		if (mMain) {
			mDst.print( MARKER_VALUE, mark.mMark);
		}
		mDst.endObject();
		return null;
	}

	@Override
	public Object forMob( FMob mob, Object p) {
		mDst.beginObject();
		mDst.print( MOB_ID, mob.toIdent());
		if (mDocu) {
			mDst.print( MOB_NAME, mob.getName( mLang));
		}
		if (mMain) {
			writeIcon( MOB_ICON, mob.mIcon);
			mDst.print( MOB_OBJECT, mob.mMob);
			mDst.print( MOB_COUNT, mob.mKills);
			mDst.print( MOB_EXACT, mob.mExact);
		}
		mDst.endObject();
		return null;
	}

	@Override
	public Object forQuest( FQuest quest, Object p) {
		mDst.beginObject();
		mDst.print( QUEST_ID, quest.toIdent());
		if (mDocu) {
			mDst.print( QUEST_NAME, quest.getName( mLang));
			mDst.printIf( QUEST_DESC, quest.getDescr( mLang));
		}
		if (mMain) {
			mDst.print( QUEST_UUID, quest.getUUID());
			mDst.print( QUEST_X, quest.mX);
			mDst.print( QUEST_Y, quest.mY);
			mDst.printIf( QUEST_BIG, quest.mBig);
			writeIcon( QUEST_ICON, quest.mIcon);
			writeQuestArr( QUEST_REQUIREMENTS, quest.mRequirements);
			writeQuestArr( QUEST_OPTION_LINKS, quest.mOptionLinks);
			quest.mRepeatInfo.accept( this, null);
			mDst.print( QUEST_TRIGGER_TYPE, quest.mTriggerType);
			mDst.print( QUEST_TRIGGER_TASKS, quest.mTriggerTasks);
			mDst.print( QUEST_PARENT_REQUIREMENT, quest.mCount);
		}
		writeTasks( quest);
		if (mMain) {
			writeStackArr( QUEST_REWARD, quest.mRewards);
			writeStackArr( QUEST_CHOICE, quest.mChoices);
			writeCommands( quest);
			writeRewards( quest);
		}
		mDst.endObject();
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		mDst.beginObject();
		mDst.print( QUEST_SET_ID, set.toIdent());
		if (mDocu) {
			mDst.print( QUEST_SET_NAME, set.getName( mLang));
			mDst.printIf( QUEST_SET_DECR, set.getDescr( mLang));
		}
		writeBars( set);
		writeQuests( set);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forRepeatInfo( FRepeatInfo info, Object p) {
		mDst.beginObject( QUEST_REPEAT_INFO);
		mDst.print( REPEAT_INFO_TYPE, info.mType);
		if (info.mType.isUseTime()) {
			mDst.print( REPEAT_INFO_TOTAL, info.mTotal);
		}
		mDst.endObject();
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, Object p) {
		mDst.beginObject();
		mDst.print( REPUTATION_ID, rep.toIdent());
		if (mDocu) {
			mDst.print( REPUTATION_NAME, rep.getName( mLang));
			mDst.printIf( REPUTATION_NEUTRAL, rep.getDescr( mLang));
		}
		if (mMain) {
			mDst.print( REPUTATION_UUID, rep.getUUID());
		}
		writeMarkers( rep);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forReputationBar( FReputationBar bar, Object p) {
		mDst.print( REPUTATION_BAR_REP, bar.mRep.toIdent());
		mDst.print( REPUTATION_BAR_X, bar.mX);
		mDst.print( REPUTATION_BAR_Y, bar.mY);
		return null;
	}

	@Override
	public Object forReputationReward( FReputationReward rr, Object p) {
		mDst.beginObject();
		mDst.print( REWARD_REPUTATION, rr.mRep.toIdent());
		mDst.print( REWARD_VALUE, rr.mValue);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forSetting( FSetting rs, Object p) {
		mDst.beginObject();
		mDst.print( SETTING_REPUTATION, rs.mRep.toIdent());
		if (rs.mLower != null) {
			mDst.print( SETTING_LOWER, rs.mLower.toIdent());
		}
		if (rs.mUpper != null) {
			mDst.print( SETTING_UPPER, rs.mUpper.toIdent());
		}
		mDst.print( SETTING_INVERTED, rs.mInverted);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, Object p) {
		mDst.beginObject();
		doTask( task);
		if (mMain) {
			mDst.print( TASK_DEATHS, task.mDeaths);
		}
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, Object p) {
		mDst.beginObject();
		doTask( task);
		mDst.beginArray( TASK_LOCATIONS);
		task.forEachLocation( this, p);
		mDst.endArray();
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, Object p) {
		mDst.beginObject();
		doTask( task);
		mDst.beginArray( TASK_MOBS);
		task.forEachMob( this, p);
		mDst.endArray();
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, Object p) {
		mDst.beginObject();
		doTaskReputation( task);
		if (mMain) {
			mDst.print( TASK_KILLS, task.mKills);
		}
		mDst.endObject();
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, Object p) {
		mDst.beginObject();
		doTaskReputation( task);
		mDst.endObject();
		return null;
	}

	private void writeBars( FQuestSet set) {
		if (SizeOf.getBars( set) > 0) {
			mDst.beginArray( QUEST_SET_BARS);
			set.forEachBar( this, null);
			mDst.endArray();
		}
	}

	private void writeCommands( FQuest quest) {
		if (quest.mCommands.size() > 0) {
			mDst.beginArray( QUEST_COMMANDS);
			for (String cmd : quest.mCommands) {
				if (cmd != null) {
					mDst.printValue( cmd);
				}
			}
			mDst.endArray();
		}
	}

	void writeDst( FHqm hqm) {
		ReindexOfQuests.get( hqm);
		mDst.beginObject();
		if (mMain) {
			mDst.print( HQM_VERSION, hqm.getVersion());
			mDst.printIf( HQM_PASSCODE, hqm.mPassCode);
			writeLanguages( hqm.mLanguages);
		}
		else {
			mDst.print( HQM_PARENT, hqm.getName());
		}
		if (mDocu) {
			mDst.printIf( HQM_DESCRIPTION, hqm.getDescr( mLang));
		}
		else {
			mDst.print( HQM_MAIN, hqm.mMain.mLocale);
		}
		writeQuestSetCat( hqm.mQuestSetCat);
		writeReputationCat( hqm.mReputationCat);
		writeGroupTierCat( hqm.mGroupTierCat);
		mDst.endObject();
	}

	private void writeGroups( FGroupTier tier) {
		if (SizeOf.getGroups( tier) > 0) {
			mDst.beginArray( GROUP_TIER_GROUPS);
			tier.forEachGroup( this, null);
			mDst.endArray();
		}
	}

	private void writeGroupTierCat( FGroupTierCat cat) {
		mDst.beginArray( HQM_GROUP_TIER_CAT);
		cat.forEachMember( this, null);
		mDst.endArray();
	}

	private void writeIcon( String key, FItemStack icon) {
		if (icon != null) {
			String nbt = SerializerAtJson.write( icon.getNBT());
			if (Utils.validString( nbt)) {
				mDst.beginObject( key);
				mDst.print( ITEM_OBJECT, createItem( icon));
				mDst.print( ITEM_NBT, nbt);
				mDst.endObject();
			}
			else {
				mDst.print( key, createItem( icon));
			}
		}
	}

	private void writeLanguages( ArrayList<FLanguage> languages) {
		mDst.beginArray( HQM_LANGUAGES);
		for (FLanguage lang : languages) {
			if (lang != null) {
				mDst.printValue( lang.mLocale);
			}
		}
		mDst.endArray();
	}

	private void writeMarkers( FReputation rep) {
		if (SizeOf.getMarker( rep) > 0) {
			mDst.beginArray( REPUTATION_MARKERS);
			rep.forEachMarker( this, null);
			mDst.endArray();
		}
	}

	private void writeQuestArr( String key, ArrayList<FQuest> arr) {
		if (arr != null && !arr.isEmpty()) {
			mDst.beginArray( key);
			for (FQuest quest : arr) {
				if (quest != null) {
					mDst.printValue( quest.toIdent());
				}
			}
			mDst.endArray();
		}
	}

	private void writeQuests( FQuestSet set) {
		mDst.beginArray( QUEST_SET_QUESTS);
		set.forEachQuest( this, null);
		mDst.endArray();
	}

	private void writeQuestSetCat( FQuestSetCat cat) {
		mDst.beginArray( HQM_QUEST_SET_CAT);
		cat.forEachMember( this, null);
		mDst.endArray();
	}

	private void writeReputationCat( FReputationCat cat) {
		mDst.beginArray( HQM_REPUTATION_CAT);
		cat.forEachMember( this, null);
		mDst.endArray();
	}

	private void writeRewards( FQuest quest) {
		if (SizeOf.getReward( quest) > 0) {
			mDst.beginArray( QUEST_REP_REWRDS);
			quest.forEachReward( this, null);
			mDst.endArray();
		}
	}

	private void writeStackArr( String key, ArrayList<FItemStack> arr) {
		if (arr != null && !arr.isEmpty()) {
			mDst.beginArray( key);
			for (FItemStack stk : arr) {
				String nbt = SerializerAtJson.write( stk.getNBT());
				if (Utils.validString( nbt)) {
					mDst.beginObject();
					mDst.print( ITEM_OBJECT, createItem( stk));
					mDst.print( ITEM_NBT, nbt);
					mDst.endObject();
				}
				else {
					mDst.printValue( createItem( stk));
				}
			}
			mDst.endArray();
		}
	}

	private void writeTasks( FQuest quest) {
		if (SizeOf.getTasks( quest) > 0) {
			mDst.beginArray( QUEST_TASKS);
			quest.forEachTask( this, null);
			mDst.endArray();
		}
	}
}
