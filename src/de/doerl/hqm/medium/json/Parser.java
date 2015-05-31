package de.doerl.hqm.medium.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.AStack;
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
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.FReward;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.GroupTierOfIdx;
import de.doerl.hqm.base.dispatch.MarkerOfIdx;
import de.doerl.hqm.base.dispatch.QuestSetOfIdx;
import de.doerl.hqm.base.dispatch.ReputationOfIdx;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IHqmReader;
import de.doerl.hqm.medium.json.JsonReader.FObject;
import de.doerl.hqm.medium.json.JsonReader.IJson;
import de.doerl.hqm.medium.json.JsonReader.JSONofObject;
import de.doerl.hqm.medium.json.JsonReader.JSONofValue;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.quest.RepeatType;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.quest.Visibility;
import de.doerl.hqm.utils.Utils;

class Parser implements IHqmReader, IToken {
//	private static final Logger LOGGER = Logger.getLogger( Parser.class.getName());
	private HashMap<FQuest, int[]> mRequirements = new HashMap<>();
	private HashMap<FQuest, int[]> mOptionLinks = new HashMap<>();
	private IJson mJson;

	public Parser( InputStream is) throws IOException {
		mJson = JsonReader.get( is);
	}

	private static int parseID( String s) {
		int p1 = s.indexOf( '[');
		int p2 = s.indexOf( ']');
		if (p1 > 0 && p2 > p1) {
			return Utils.parseInteger( s.substring( p1 + 1, p2), 0);
		}
		return 0;
	}

	@Override
	public void closeSrc() {
	}

	private void readGroup( FGroupCat cat, ArrayList<IJson> arr) {
		for (IJson json : arr) {
			FObject obj = JSONofObject.get( json);
			if (obj != null) {
				FGroup grp = cat.createMember( obj.getString( IToken.GROUP_NAME));
				grp.mTier = GroupTierOfIdx.get( cat.mParentHQM, parseID( obj.getString( IToken.GROUP_TIER)));
				grp.mLimit = obj.getInt( IToken.GROUP_LIMIT);
				readStacks( grp.mStacks, obj.getArray( IToken.GROUP_STACKS));
			}
		}
	}

	private void readGroupTiers( FGroupTierCat cat, ArrayList<IJson> arr) {
		for (IJson json : arr) {
			FObject obj = JSONofObject.get( json);
			if (obj != null) {
				FGroupTier tier = cat.createMember( obj.getString( IToken.GROUP_TIER_NAME));
				tier.mColorID = obj.getInt( IToken.GROUP_TIER_COLOR);
				ArrayList<IJson> weights = obj.getArray( IToken.GROUP_TIER_WEIGHTS);
				int[] ww = new int[weights.size()];
				for (int i = 0; i < ww.length; ++i) {
					ww[i] = JSONofValue.getInt( weights.get( i));
				}
				tier.mWeights = ww;
			}
		}
	}

	private void readMarker( FReputation rep, ArrayList<IJson> arr) {
		for (IJson json : arr) {
			FObject obj = JSONofObject.get( json);
			if (obj != null) {
				FMarker marker = rep.createMarker( obj.getString( IToken.MARKER_NAME));
				marker.mMark = obj.getInt( IToken.MARKER_VALUE);
			}
		}
	}

	private void readQuestArr( FQuest quest, HashMap<FQuest, int[]> cache, ArrayList<IJson> arr) {
		if (arr != null) {
			int size = arr.size();
			int[] result = new int[size];
			for (int i = 0; i < size; ++i) {
				result[i] = parseID( JSONofValue.get( arr.get( i)).toString());
			}
			cache.put( quest, result);
		}
	}

	private void readQuestInfo( FRepeatInfo info, FObject obj) {
		info.mType = RepeatType.valueOf( obj.getString( IToken.REPEAT_INFO_TYPE));
		if (info.mType.isUseTime()) {
			info.mTotal = obj.getInt( IToken.REPEAT_INFO_TOTAL);
		}
	}

	private void readQuests( FHqm hqm, ArrayList<IJson> arr) {
		for (IJson json : arr) {
			FObject obj = JSONofObject.get( json);
			if (obj != null) {
				boolean isDelete = obj.getBoolean( IToken.QUEST_DELETED);
				if (isDelete) {
					hqm.addDeletedQuest();
				}
				else {
					FQuest quest = hqm.createQuest( obj.getString( IToken.QUEST_NAME));
					quest.mDescr = obj.getString( IToken.QUEST_DESC);
					quest.mX = obj.getInt( IToken.QUEST_X);
					quest.mY = obj.getInt( IToken.QUEST_Y);
					quest.mBig = obj.getBoolean( IToken.QUEST_BIG);
					int setID = parseID( obj.getString( IToken.QUEST_SET));
					FQuestSet qs = QuestSetOfIdx.get( hqm.mQuestSetCat, setID);
					if (qs == null) {
						qs = hqm.mQuestSetCat.createMember( "--Missing--");
					}
					quest.mQuestSet = qs;
					quest.mIcon = FItemStack.parse( obj.getString( IToken.QUEST_ICON));
					readQuestArr( quest, mRequirements, obj.getArray( IToken.QUEST_REQUIREMENTS));
					readQuestArr( quest, mOptionLinks, obj.getArray( IToken.QUEST_OPTION_LINKS));
					readQuestInfo( quest.mRepeatInfo, obj.getObject( IToken.QUEST_REPEAT_INFO));
					String trigger = obj.getString( IToken.QUEST_TRIGGER_TYPE);
					if (trigger != null) {
						quest.mTriggerType = TriggerType.valueOf( trigger);
						if (TriggerType.valueOf( trigger).isUseTaskCount()) {
							quest.mTriggerTasks = obj.getInt( IToken.QUEST_TRIGGER_TASKS);
						}
					}
					quest.mReqCount = obj.getInteger( IToken.QUEST_PARENT_REQUIREMENT_COUNT);
					readTasks( quest, obj.getArray( IToken.QUEST_TASKS));
					readStacks( quest.mRewards, obj.getArray( IToken.QUEST_REWARD));
					readStacks( quest.mChoices, obj.getArray( IToken.QUEST_CHOICE));
					readRewards( quest, obj.getArray( IToken.QUEST_REPUTATIONS));
				}
			}
		}
	}

	private void readQuestSetCat( FQuestSetCat cat, ArrayList<IJson> arr) {
		for (IJson json : arr) {
			FObject obj = JSONofObject.get( json);
			if (obj != null) {
				FQuestSet member = cat.createMember( obj.getString( IToken.QUEST_SET_NAME));
				member.mDescr = obj.getString( IToken.QUEST_SET_DECR);
			}
		}
	}

	private void readReputations( FReputationCat cat, ArrayList<IJson> arr) {
		for (IJson json : arr) {
			FObject obj = JSONofObject.get( json);
			if (obj != null) {
				FReputation member = cat.createMember( obj.getString( IToken.REPUTATION_NAME));
				member.mNeutral = obj.getString( IToken.REPUTATION_NEUTRAL);
				readMarker( member, obj.getArray( IToken.REPUTATION_MARKERS));
			}
		}
	}

	private void readRewards( FQuest quest, ArrayList<IJson> arr) {
		for (IJson json : arr) {
			FObject obj = JSONofObject.get( json);
			if (obj != null) {
				FReward reward = quest.createReputationReward();
				reward.mRep = ReputationOfIdx.get( quest.mParentHQM, parseID( obj.getString( IToken.REWARD_REPUTATION)));
				reward.mValue = obj.getInt( IToken.REWARD_VALUE);
			}
		}
	}

	@Override
	public void readSrc( FHqm hqm, ICallback cb) {
		FObject obj = JSONofObject.get( mJson);
		if (obj != null) {
			hqm.setVersion( FileVersion.valueOf( obj.getString( IToken.HQM_VERSION)));
			hqm.mPassCode = obj.getString( IToken.HQM_PASSCODE);
			hqm.mDescr = obj.getString( IToken.HQM_DECRIPTION);
			readQuestSetCat( hqm.mQuestSetCat, obj.getArray( IToken.HQM_QUEST_SET_CAT));
			readReputations( hqm.mReputationCat, obj.getArray( IToken.HQM_REPUTATION_CAT));
			readQuests( hqm, obj.getArray( IToken.HQM_QUESTS));
			readGroupTiers( hqm.mGroupTierCat, obj.getArray( IToken.HQM_GROUP_TIER_CAT));
			readGroup( hqm.mGroupCat, obj.getArray( IToken.HQM_GROUP_CAT));
		}
	}

	private void readStacks( Vector<AStack> param, ArrayList<IJson> arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject oo = JSONofObject.get( json);
				if (oo != null) {
					String name = oo.getString( IToken.ITEM_NAME);
					if (name != null) {
						String nbt = oo.getString( IToken.ITEM_NBT);
						param.add( FItemStack.parse( name, nbt));
					}
					else {
						String nbt = oo.getString( IToken.FLUID_NBT);
						param.add( FFluidStack.parse( nbt));
					}
				}
			}
		}
	}

	private void readTasks( FQuest quest, ArrayList<IJson> arr) {
		for (IJson json : arr) {
			FObject obj = JSONofObject.get( json);
			if (obj != null) {
				TaskTyp type = TaskTyp.valueOf( obj.getString( IToken.TASK_TYPE));
				AQuestTask task = quest.createQuestTask( type, obj.getString( IToken.TASK_NAME));
				task.mDescr = obj.getString( IToken.TASK_DESC);
				TaskWorker.get( task, obj);
			}
		}
	}

	private static class TaskWorker extends AHQMWorker<Object, FObject> {
		private static final TaskWorker WORKER = new TaskWorker();

		private TaskWorker() {
		}

		public static void get( AQuestTask task, FObject obj) {
			task.accept( WORKER, obj);
		}

		@Override
		protected Object doTaskItems( AQuestTaskItems task, FObject obj) {
			ArrayList<IJson> arr = obj.getArray( IToken.TASK_REQUIREMENTS);
			for (IJson json : arr) {
				FObject oo = JSONofObject.get( json);
				if (oo != null) {
					String name = oo.getString( IToken.REQUIREMENT_ITEM);
					if (name != null) {
						FItemRequirement item = task.createItemRequirement();
						item.mStack = FItemStack.parse( name);
						item.mRequired = oo.getInt( IToken.REQUIREMENT_REQUIRED);
						item.mPrecision = ItemPrecision.valueOf( oo.getString( IToken.REQUIREMENT_PRECISION));
					}
					else {
						FFluidRequirement fluid = task.createFluidRequirement();
						fluid.mStack = FFluidStack.parse( oo.getString( IToken.REQUIREMENT_FLUID));
					}
				}
			}
			return null;
		}

		@Override
		public Object forTaskDeath( FQuestTaskDeath task, FObject obj) {
			task.mDeaths = obj.getInt( IToken.TASK_DEATHS);
			return null;
		}

		@Override
		public Object forTaskLocation( FQuestTaskLocation task, FObject obj) {
			ArrayList<IJson> arr = obj.getArray( IToken.TASK_LOCATIONS);
			for (IJson json : arr) {
				FObject oo = JSONofObject.get( json);
				if (oo != null) {
					FItemStack icon = FItemStack.parse( oo.getString( IToken.LOCATION_ICON));
					FLocation loc = task.createLocation( icon, oo.getString( IToken.LOCATION_NAME));
					loc.mX = oo.getInt( IToken.LOCATION_X);
					loc.mY = oo.getInt( IToken.LOCATION_Y);
					loc.mZ = oo.getInt( IToken.LOCATION_Z);
					loc.mRadius = oo.getInt( IToken.LOCATION_RADIUS);
					loc.mVisibility = Visibility.valueOf( oo.getString( IToken.LOCATION_VISIBLE));
					loc.mDim = oo.getInt( IToken.LOCATION_DIM);
				}
			}
			return null;
		}

		@Override
		public Object forTaskMob( FQuestTaskMob task, FObject obj) {
			ArrayList<IJson> arr = obj.getArray( IToken.TASK_MOBS);
			for (IJson json : arr) {
				FObject oo = JSONofObject.get( json);
				if (oo != null) {
					FItemStack icon = FItemStack.parse( oo.getString( IToken.MOB_ICON));
					FMob mob = task.createMob( icon, oo.getString( IToken.MOB_NAME));
					mob.mMob = oo.getString( IToken.MOB_MOB2);
					mob.mKills = oo.getInt( IToken.MOB_COUNT);
					mob.mExact = oo.getBoolean( IToken.MOB_EXACT);
				}
			}
			return null;
		}

		@Override
		public Object forTaskReputationKill( FQuestTaskReputationKill task, FObject obj) {
			task.mKills = obj.getInt( IToken.TASK_KILLS);
			return null;
		}

		@Override
		public Object forTaskReputationTarget( FQuestTaskReputationTarget task, FObject obj) {
			ArrayList<IJson> arr = obj.getArray( IToken.TASK_SETTINGS);
			for (IJson json : arr) {
				FObject oo = JSONofObject.get( json);
				if (oo != null) {
					FSetting res = task.createSetting();
					res.mRep = ReputationOfIdx.get( task, parseID( oo.getString( IToken.SETTING_REPUTATION)));
					res.mLower = MarkerOfIdx.get( res.mRep, parseID( oo.getString( IToken.SETTING_LOWER)));
					res.mUpper = MarkerOfIdx.get( res.mRep, parseID( oo.getString( IToken.SETTING_UPPER)));
					res.mInverted = oo.getBoolean( IToken.SETTING_INVERTED);
				}
			}
			return null;
		}
	}
}
