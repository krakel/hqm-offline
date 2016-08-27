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
import de.doerl.hqm.base.dispatch.MarkerOfID;
import de.doerl.hqm.base.dispatch.QuestOfID;
import de.doerl.hqm.base.dispatch.ReputationOfID;
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
	private HashMap<FQuest, int[]> mRequirements = new HashMap<>();
	private HashMap<FQuest, int[]> mOptionLinks = new HashMap<>();
	private HashMap<Integer, ArrayList<FQuest>> mPosts = new HashMap<>();
	private FLanguage mLang;
	private File mBase;

	public Parser( FLanguage lang, File base) {
		mLang = lang;
		mBase = base;
	}

	private void addPost( FQuest quest, Integer id) {
		ArrayList<FQuest> p = mPosts.get( id);
		if (p == null) {
			p = new ArrayList<>();
			mPosts.put( id, p);
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
		readTaskReputations( task, FArray.to( obj.get( IToken.TASK_SETTINGS)));
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

	private void loadQuestingData( FHqm hqm, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
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
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FReputation rep = cat.createMember();
					rep.setID( FValue.toString( obj.get( IToken.REPUTATION_ID)));
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
				bar.mValue = FValue.toInt( json);
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
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FGroup grp = tier.createGroup();
					grp.setID( FValue.toString( obj.get( IToken.GROUP_ID)));
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
		String str = FValue.toString( json);
		if (str != null) {
			return FItemStack.parse( str);
		}
		return null;
	}

	private FItemStack readItemStack( FObject obj) {
		FCompound nbt = NbtParser.parse( FValue.toString( obj.get( IToken.ITEM_NBT)));
		String id = FValue.toString( obj.get( IToken.ITEM_ID));
		int dmg = FValue.toInt( obj.get( IToken.ITEM_DAMAGE));
		int size = FValue.toInt( obj.get( IToken.ITEM_SIZE));
		return new FItemStack( nbt, id, dmg, size);
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

	private void readQuestArr( FQuest quest, HashMap<FQuest, int[]> cache, FArray arr, boolean withPost) {
		if (arr != null) {
			int size = arr.size();
			int[] result = new int[size];
			for (int i = 0; i < size; ++i) {
				int id = FQuest.fromIdent( FValue.toString( arr.get( i)));
				result[i] = id;
				if (withPost) {
					addPost( quest, id);
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
					int repID = FReputation.fromIdent( FValue.toString( obj.get( IToken.REWARD_REPUTATION)));
					if (repID < 0) {
						Utils.log( LOGGER, Level.WARNING, "missing repID");
					}
					else {
						reward.mRep = ReputationOfID.get( quest.getHqm(), repID);
					}
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
		//
		loadQuestingData( hqm, FArray.to( Medium.redJson( mBase, Medium.DATA_FILE)));
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
				else {
					String str = FValue.toString( json);
					if (str != null) {
						param.add( FItemStack.parse( str));
					}
					else {
						param.add( new FItemStack( null));
					}
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
					int repID = FReputation.fromIdent( FValue.toString( obj.get( IToken.SETTING_REPUTATION)));
					if (repID < 0) {
						Utils.log( LOGGER, Level.WARNING, "missing repID");
					}
					else {
						res.mRep = ReputationOfID.get( task, repID);
					}
					String low = FValue.toString( obj.get( IToken.SETTING_LOWER));
					if (low != null) {
						int lowerID = FMarker.fromIdent( low);
						if (lowerID < 0) {
							Utils.log( LOGGER, Level.WARNING, "missing lowerID");
						}
						else {
							res.mLower = MarkerOfID.get( res.mRep, lowerID);
						}
					}
					String upp = FValue.toString( obj.get( IToken.SETTING_UPPER));
					if (upp != null) {
						int upperID = FMarker.fromIdent( upp);
						if (upperID < 0) {
							Utils.log( LOGGER, Level.WARNING, "missing upperID");
						}
						else {
							res.mUpper = MarkerOfID.get( res.mRep, upperID);
						}
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
			int[] ids = mOptionLinks.get( quest);
			for (int i = 0; i < ids.length; ++i) {
				int id = ids[i];
				FQuest other = QuestOfID.get( hqm, id);
				if (other == null) {
					Utils.log( LOGGER, Level.WARNING, "missing option link [{0}] {1} for {2}", i, id, quest.getName());
				}
				else {
					quest.mOptionLinks.add( other);
				}
			}
		}
	}

	private void updatePosts( FHqm hqm) {
		for (Integer id : mPosts.keySet()) {
			FQuest other = QuestOfID.get( hqm, id);
			if (other == null) {
				Utils.log( LOGGER, Level.WARNING, "missing posts {0}", id);
			}
			else {
				other.mPosts.addAll( mPosts.get( id));
			}
		}
	}

	private void updateRequirements( FHqm hqm) {
		for (FQuest quest : mRequirements.keySet()) {
			int[] ids = mRequirements.get( quest);
			for (int i = 0; i < ids.length; ++i) {
				int id = ids[i];
				FQuest other = QuestOfID.get( hqm, id);
				if (other == null) {
					Utils.log( LOGGER, Level.WARNING, "missing requirement [{0}] {1} for {2}", i, id, quest.getName());
				}
				else {
					quest.mRequirements.add( other);
				}
			}
		}
	}
}
