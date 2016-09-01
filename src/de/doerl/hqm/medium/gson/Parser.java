package de.doerl.hqm.medium.gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.AQuestTaskReputation;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FFluidStack;
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
import de.doerl.hqm.base.dispatch.MarkerOfUUID;
import de.doerl.hqm.base.dispatch.QuestOfUUID;
import de.doerl.hqm.base.dispatch.ReputationOfUUID;
import de.doerl.hqm.quest.ItemPrecision;
import de.doerl.hqm.quest.RepeatType;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.quest.Visibility;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.json.FArray;
import de.doerl.hqm.utils.json.FObject;
import de.doerl.hqm.utils.json.FValue;
import de.doerl.hqm.utils.json.IJson;
import de.doerl.hqm.utils.nbt.FCompound;
import de.doerl.hqm.utils.nbt.NbtParser;

class Parser extends AHQMWorker<Object, FObject> implements IToken {
	private static final Logger LOGGER = Logger.getLogger( Parser.class.getName());
	private HashMap<FQuest, String[]> mRequirements = new HashMap<>();
	private HashMap<FQuest, String[]> mOptionLinks = new HashMap<>();
	private HashMap<String, ArrayList<FQuest>> mPosts = new HashMap<>();
	private FLanguage mLang;
	private File mBase;

	public Parser( FLanguage lang, File base) {
		mLang = lang;
		mBase = base;
	}

	private void addPost( FQuest quest, String uuid) {
		ArrayList<FQuest> p = mPosts.get( uuid);
		if (p == null) {
			p = new ArrayList<>();
			mPosts.put( uuid, p);
		}
		p.add( quest);
	}

	@Override
	protected Object doTaskItems( AQuestTaskItems task, FObject obj) {
		readTaskItems( task, FArray.to( obj.get( IToken.TASK_REQUIREMENTS)));
		return null;
	}

	@Override
	protected Object doTaskReputation( AQuestTaskReputation task, FObject obj) {
		readTaskReputations( task, FArray.to( obj.get( IToken.TASK_REPUTATIONS)));
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, FObject obj) {
		task.mDeaths = FValue.toInt( obj.get( IToken.TASK_DEATHS));
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, FObject obj) {
		readTaskLocations( task, FArray.to( obj.get( IToken.TASK_LOCATIONS)));
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, FObject obj) {
		readTaskMobs( task, FArray.to( obj.get( IToken.TASK_MOBS)));
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, FObject obj) {
		doTaskReputation( task, obj);
		task.mKills = FValue.toInt( obj.get( IToken.TASK_KILLS));
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, FObject obj) {
		doTaskReputation( task, obj);
		return null;
	}

	private void loadDescription( FHqm hqm, String descr) {
		hqm.setDescr( mLang, descr != null ? descr : "No description");
	}

	private void loadGroupTier( FGroupTierCat cat, FArray arr) {
		if (arr != null) {
			for (int id = 0, max = arr.size(); id < max; ++id) {
				FObject obj = FObject.to( arr.get( id));
				if (obj != null) {
					FGroupTier tier = cat.createGroupTier( id);
					tier.setName( mLang, FValue.toString( obj.get( IToken.GROUP_TIER_NAME)));
					tier.mColorID = FValue.toInt( obj.get( IToken.GROUP_TIER_COLOR));
					FArray ww = FArray.to( obj.get( IToken.GROUP_TIER_WEIGHTS));
					if (ww != null) {
						int size = Math.min( ww.size(), tier.mWeights.length);
						for (int i = 0; i < size; ++i) {
							tier.mWeights[i] = FValue.toInt( ww.get( i));
						}
					}
					readGroups( tier, FArray.to( obj.get( IToken.GROUP_TIER_GROUPS)));
				}
			}
		}
	}

	private void loadQuestSet( FQuestSetCat cat, File[] files) {
		if (files != null) {
			int id = 0;
			for (File file : files) {
				if (Medium.isQuestSet( file)) {
					loadQuestSet( cat, Medium.redJson( file), id++);
				}
			}
		}
	}

	private void loadQuestSet( FQuestSetCat cat, FObject obj, int id) {
		if (obj != null) {
			FQuestSet set = cat.createQuestSet( id);
			set.setName( mLang, FValue.toString( obj.get( IToken.QUEST_SET_NAME)));
			set.setDescr( mLang, FValue.toString( obj.get( IToken.QUEST_SET_DECR)));
			readQuests( set, FArray.to( obj.get( IToken.QUEST_SET_QUESTS)));
			readBars( set, FArray.to( obj.get( IToken.QUEST_SET_BARS)));
		}
	}

	private void loadReputation( FReputationCat cat, FArray arr) {
		if (arr != null) {
			for (int id = 0, max = arr.size(); id < max; ++id) {
				FObject obj = FObject.to( arr.get( id));
				if (obj != null) {
					FReputation rep = cat.createReputation( id);
					rep.setUUID( FValue.toString( obj.get( IToken.REPUTATION_UUID)));
					rep.setName( mLang, FValue.toString( obj.get( IToken.REPUTATION_NAME)));
					rep.setDescr( mLang, FValue.toString( obj.get( IToken.REPUTATION_NEUTRAL)));
					readMarker( rep, FArray.to( obj.get( IToken.REPUTATION_MARKERS)));
				}
			}
		}
	}

	private void loadState( FHqm hqm, FObject obj) {
		if (obj != null) {
			hqm.mQuesting = FValue.toBoolean( obj.get( IToken.HQM_QUESTING));
			hqm.mHadrcore = FValue.toBoolean( obj.get( IToken.HQM_HARDCORE));
		}
	}

	private void readBars( FQuestSet set, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FReputationBar bar = set.createReputationBar();
				bar.mX = FValue.toInt( json);
			}
		}
	}

	private void readCommands( FQuest quest, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				String cmd = FValue.toString( json);
				if (cmd != null) {
					quest.mCommands.add( cmd);
				}
			}
		}
	}

	private void readGroups( FGroupTier tier, FArray arr) {
		if (arr != null) {
			for (int id = 0, max = arr.size(); id < max; ++id) {
				FObject obj = FObject.to( arr.get( id));
				if (obj != null) {
					FGroup grp = tier.createGroup( id);
					grp.setUUID( FValue.toString( obj.get( IToken.GROUP_UUID)));
					grp.setName( mLang, FValue.toString( obj.get( IToken.GROUP_NAME)));
					grp.mLimit = FValue.toIntObj( obj.get( IToken.GROUP_LIMIT));
					readStacks( grp.mStacks, FArray.to( obj.get( IToken.GROUP_STACKS)));
				}
			}
		}
	}

	private FItemStack readIcon( IJson json) {
		FObject obj = FObject.to( json);
		if (obj != null) {
			return readItemStack( obj);
		}
		return null;
	}

	private FItemStack readItemStack( FObject obj) {
		String name = FValue.toString( obj.get( IToken.ITEM_NAME), "item:unknown");
		int dmg = FValue.toInt( obj.get( IToken.ITEM_DAMAGE), 0);
		int size = FValue.toInt( obj.get( IToken.ITEM_SIZE), 1);
		FCompound nbt = NbtParser.parse( FValue.toString( obj.get( IToken.ITEM_NBT)));
		return new FItemStack( name, dmg, size, nbt);
	}

	private void readMarker( FReputation rep, FArray arr) {
		if (arr != null) {
			for (int id = 0, max = arr.size(); id < max; ++id) {
				FObject obj = FObject.to( arr.get( id));
				if (obj != null) {
					FMarker marker = rep.createMarker( id);
					marker.setName( mLang, FValue.toString( obj.get( IToken.MARKER_NAME)));
					marker.mMark = FValue.toInt( obj.get( IToken.MARKER_VALUE));
				}
			}
			rep.sort();
		}
	}

	private void readQuestArr( FQuest quest, HashMap<FQuest, String[]> cache, FArray arr, boolean withPost) {
		if (arr != null) {
			int size = arr.size();
			String[] result = new String[size];
			for (int i = 0; i < size; ++i) {
				String uuid = FValue.toString( arr.get( i));
				result[i] = uuid;
				if (withPost) {
					addPost( quest, uuid);
				}
			}
			cache.put( quest, result);
		}
	}

	private void readQuestInfo( FRepeatInfo info, FObject obj) {
		if (obj != null) {
			info.mType = RepeatType.parse( FValue.toString( obj.get( IToken.REPEAT_INFO_TYPE)));
			if (info.mType.isUseTime()) {
				int hour = FValue.toInt( obj.get( IToken.REPEAT_INFO_HOURS));
				int days = FValue.toInt( obj.get( IToken.REPEAT_INFO_DAYS));
				info.mTotal = 24 * hour + days;
			}
		}
	}

	private void readQuests( FQuestSet set, FArray arr) {
		if (arr != null) {
			for (int id = 0, max = arr.size(); id < max; ++id) {
				FObject obj = FObject.to( arr.get( id));
				if (obj != null) {
					FQuest quest = set.createQuest( id);
					quest.mUUID = FValue.toString( obj.get( IToken.QUEST_UUID));
					quest.setName( mLang, FValue.toString( obj.get( IToken.QUEST_NAME)));
					quest.setDescr( mLang, FValue.toString( obj.get( IToken.QUEST_DESC)));
					quest.mX = FValue.toInt( obj.get( IToken.QUEST_X));
					quest.mY = FValue.toInt( obj.get( IToken.QUEST_Y));
					quest.mBig = FValue.toBoolean( obj.get( IToken.QUEST_BIG));
					quest.mIcon = readIcon( obj.get( IToken.QUEST_ICON));
					readQuestArr( quest, mRequirements, FArray.to( obj.get( IToken.QUEST_REQUIREMENTS)), true);
					readQuestArr( quest, mOptionLinks, FArray.to( obj.get( IToken.QUEST_OPTION_LINKS)), false);
					readQuestInfo( quest.mRepeatInfo, FObject.to( obj.get( IToken.QUEST_REPEAT_INFO)));
					String trigger = FValue.toString( obj.get( IToken.QUEST_TRIGGER_TYPE));
					if (trigger != null) {
						quest.mTriggerType = TriggerType.parse( trigger);
						if (quest.mTriggerType.isUseTaskCount()) {
							quest.mTriggerTasks = FValue.toInt( obj.get( IToken.QUEST_TRIGGER_TASKS));
						}
					}
					quest.mCount = FValue.toIntObj( obj.get( IToken.QUEST_PARENT_REQUIREMENT));
					readStacks( quest.mRewards, FArray.to( obj.get( IToken.QUEST_REWARD)));
					readStacks( quest.mChoices, FArray.to( obj.get( IToken.QUEST_CHOICE)));
					readCommands( quest, FArray.to( obj.get( IToken.QUEST_COMMANDS)));
					readRewards( quest, FArray.to( obj.get( IToken.QUEST_REP_REWRDS)));
					readTasks( quest, FArray.to( obj.get( IToken.QUEST_TASKS)));
				}
			}
		}
	}

	private void readRewards( FQuest quest, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FReputationReward reward = quest.createRepReward();
					String repUUID = FValue.toString( obj.get( IToken.REWARD_REPUTATION));
					reward.mRep = ReputationOfUUID.get( quest.getHqm(), repUUID);
					reward.mValue = FValue.toInt( obj.get( IToken.REWARD_VALUE));
				}
			}
		}
	}

	void readSrc( FHqm hqm) {
		loadState( hqm, Medium.redJson( mBase, Medium.STATE_FILE));
		loadDescription( hqm, Medium.loadTxt( mBase, Medium.DESCRIPTION_FILE));
		loadReputation( hqm.mReputationCat, FArray.to( Medium.redJson( mBase, Medium.REPUTATION_FILE)));
		loadQuestSet( hqm.mQuestSetCat, mBase.listFiles());
		loadGroupTier( hqm.mGroupTierCat, FArray.to( Medium.redJson( mBase, Medium.BAGS_FILE)));
		ReindexOfQuests.get( hqm);
		updateRequirements( hqm);
		updateOptionLinks( hqm);
		updatePosts( hqm);
	}

	private void readStacks( ArrayList<FItemStack> param, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					param.add( readItemStack( obj));
				}
			}
		}
	}

	private void readTaskItems( AQuestTaskItems task, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FObject reqItem = FObject.to( obj.get( IToken.REQUIREMENT_ITEM));
					if (reqItem != null) {
						FItemRequirement item = task.createItemRequirement();
						item.setStack( readItemStack( obj));
						item.mPrecision = ItemPrecision.parse( FValue.toString( obj.get( IToken.REQUIREMENT_PRECISION)));
						item.mAmount = FValue.toInt( obj.get( IToken.REQUIREMENT_REQUIRED));
					}
					FObject reqFluid = FObject.to( obj.get( IToken.REQUIREMENT_FLUID));
					if (reqFluid != null) {
						FFluidRequirement fluid = task.createFluidRequirement();
						fluid.setStack( new FFluidStack( FValue.toString( reqFluid)));
						fluid.mAmount = FValue.toInt( obj.get( IToken.REQUIREMENT_REQUIRED));
					}
				}
			}
		}
	}

	private void readTaskLocations( FQuestTaskLocation task, FArray arr) {
		if (arr != null) {
			for (int id = 0, max = arr.size(); id < max; ++id) {
				FObject obj = FObject.to( arr.get( id));
				if (obj != null) {
					FLocation loc = task.createLocation( id);
					loc.setName( mLang, FValue.toString( obj.get( IToken.LOCATION_NAME)));
					loc.mIcon = readIcon( obj.get( IToken.LOCATION_ICON));
					loc.mX = FValue.toInt( obj.get( IToken.LOCATION_X));
					loc.mY = FValue.toInt( obj.get( IToken.LOCATION_Y));
					loc.mZ = FValue.toInt( obj.get( IToken.LOCATION_Z));
					loc.mRadius = FValue.toInt( obj.get( IToken.LOCATION_RADIUS));
					loc.mVisibility = Visibility.parse( FValue.toString( obj.get( IToken.LOCATION_VISIBLE)));
					loc.mDim = FValue.toInt( obj.get( IToken.LOCATION_DIM));
				}
			}
		}
	}

	private void readTaskMobs( FQuestTaskMob task, FArray arr) {
		if (arr != null) {
			for (int id = 0, max = arr.size(); id < max; ++id) {
				FObject obj = FObject.to( arr.get( id));
				if (obj != null) {
					FMob mob = task.createMob();
					mob.setName( mLang, FValue.toString( obj.get( IToken.MOB_NAME)));
					mob.mIcon = readIcon( obj.get( IToken.MOB_ICON));
					mob.mMob = FValue.toString( obj.get( IToken.MOB_OBJECT));
					mob.mKills = FValue.toInt( obj.get( IToken.MOB_COUNT));
					mob.mExact = FValue.toBoolean( obj.get( IToken.MOB_EXACT));
				}
			}
		}
	}

	private void readTaskReputations( AQuestTaskReputation task, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FSetting res = task.createSetting();
					String repUUID = FValue.toString( obj.get( IToken.SETTING_REPUTATION));
					if (repUUID == null) {
						Utils.log( LOGGER, Level.WARNING, "missing repUUID");
					}
					else {
						res.mRep = ReputationOfUUID.get( task, repUUID);
					}
					String lowUUID = FValue.toString( obj.get( IToken.SETTING_LOWER));
					if (lowUUID == null) {
						Utils.log( LOGGER, Level.WARNING, "missing lowerUUID");
					}
					else {
						res.mLower = MarkerOfUUID.get( res.mRep, lowUUID);
					}
					String upperUUID = FValue.toString( obj.get( IToken.SETTING_UPPER));
					if (upperUUID == null) {
						Utils.log( LOGGER, Level.WARNING, "missing upperUUID");
					}
					else {
						res.mUpper = MarkerOfUUID.get( res.mRep, upperUUID);
					}
					res.mInverted = FValue.toBoolean( obj.get( IToken.SETTING_INVERTED));
				}
			}
		}
	}

	private void readTasks( FQuest quest, FArray arr) {
		if (arr != null) {
			for (int id = 0, max = arr.size(); id < max; ++id) {
				FObject obj = FObject.to( arr.get( id));
				if (obj != null) {
					AQuestTask task = null;
					TaskTyp type = TaskTyp.parse( FValue.toString( obj.get( IToken.TASK_TYPE)));
					task = quest.createQuestTask( type);
					task.setName( mLang, FValue.toString( obj.get( IToken.TASK_NAME)));
					task.setDescr( mLang, FValue.toString( obj.get( IToken.TASK_DESC)));
					task.accept( this, obj);
				}
			}
		}
	}

	private void updateOptionLinks( FHqm hqm) {
		for (FQuest quest : mOptionLinks.keySet()) {
			String[] uuids = mOptionLinks.get( quest);
			for (int i = 0; i < uuids.length; ++i) {
				String uuid = uuids[i];
				FQuest other = QuestOfUUID.get( hqm, uuid);
				if (other == null) {
					Utils.log( LOGGER, Level.WARNING, "missing option link [{0}] {1} for {2}", i, uuid, quest.getName());
				}
				else {
					quest.mOptionLinks.add( other);
				}
			}
		}
	}

	private void updatePosts( FHqm hqm) {
		for (String uuid : mPosts.keySet()) {
			FQuest other = QuestOfUUID.get( hqm, uuid);
			if (other == null) {
				Utils.log( LOGGER, Level.WARNING, "missing posts {0}", uuid);
			}
			else {
				other.mPosts.addAll( mPosts.get( uuid));
			}
		}
	}

	private void updateRequirements( FHqm hqm) {
		for (FQuest quest : mRequirements.keySet()) {
			String[] uuids = mRequirements.get( quest);
			for (int i = 0; i < uuids.length; ++i) {
				String uuid = uuids[i];
				FQuest other = QuestOfUUID.get( hqm, uuid);
				if (other == null) {
					Utils.log( LOGGER, Level.WARNING, "missing requirement [{0}] {1} for {2}", i, uuid, quest.getName());
				}
				else {
					quest.mRequirements.add( other);
				}
			}
		}
	}
}
