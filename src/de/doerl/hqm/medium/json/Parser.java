package de.doerl.hqm.medium.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.FFluidRequirement;
import de.doerl.hqm.base.FFluidStack;
import de.doerl.hqm.base.FGroup;
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
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.GroupTierOfID;
import de.doerl.hqm.base.dispatch.MarkerOfID;
import de.doerl.hqm.base.dispatch.QuestSetOfID;
import de.doerl.hqm.base.dispatch.ReindexOfQuests;
import de.doerl.hqm.base.dispatch.ReputationOfID;
import de.doerl.hqm.medium.IHqmReader;
import de.doerl.hqm.quest.BagTier;
import de.doerl.hqm.quest.FileVersion;
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
import de.doerl.hqm.utils.json.JsonReader;

class Parser extends AHQMWorker<Object, FObject> implements IHqmReader, IToken {
	private static final Logger LOGGER = Logger.getLogger( Parser.class.getName());
	private HashMap<FQuest, String[]> mRequirements = new HashMap<>();
	private HashMap<FQuest, String[]> mOptionLinks = new HashMap<>();
	private HashMap<String, Vector<FQuest>> mPosts = new HashMap<>();
	private HashMap<String, FQuest> mQuests = new HashMap<>();
	private JsonReader mSrc;

	public Parser( InputStream is) throws IOException {
		mSrc = new JsonReader( is);
	}

	private void addPost( FQuest quest, String id) {
		Vector<FQuest> p = mPosts.get( id);
		if (p == null) {
			p = new Vector<FQuest>();
			mPosts.put( id, p);
		}
		p.add( quest);
	}

	@Override
	protected Object doTaskItems( AQuestTaskItems task, FObject obj) {
		FArray arr = FArray.to( obj.get( IToken.TASK_REQUIREMENTS));
		if (arr != null) {
			for (IJson json : arr) {
				FObject oo = FObject.to( json);
				if (oo != null) {
					String sequence = FValue.toString( oo.get( IToken.ITEM_OBJECT));
					if (sequence != null) {
						FItemRequirement item = task.createItemRequirement();
						item.mStack = FItemStack.parse( sequence, FValue.toString( oo.get( IToken.ITEM_NBT)));
						item.mRequired = FValue.toInt( oo.get( IToken.REQUIREMENT_REQUIRED));
						item.mPrecision = ItemPrecision.parse( FValue.toString( oo.get( IToken.REQUIREMENT_PRECISION)));
					}
					else {
						FFluidRequirement fluid = task.createFluidRequirement();
						fluid.mStack = FFluidStack.parse( FValue.toString( oo.get( IToken.FLUID_OBJECT)));
					}
				}
			}
		}
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, FObject obj) {
		task.mDeaths = FValue.toInt( obj.get( IToken.TASK_DEATHS));
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, FObject obj) {
		FArray arr = FArray.to( obj.get( IToken.TASK_LOCATIONS));
		if (arr != null) {
			for (IJson json : arr) {
				FObject oo = FObject.to( json);
				if (oo != null) {
					String name = FValue.toString( oo.get( IToken.LOCATION_NAME));
					FLocation loc = task.createLocation( name);
					loc.mIcon = readIcon( oo.get( IToken.LOCATION_ICON));
					loc.mX = FValue.toInt( oo.get( IToken.LOCATION_X));
					loc.mY = FValue.toInt( oo.get( IToken.LOCATION_Y));
					loc.mZ = FValue.toInt( oo.get( IToken.LOCATION_Z));
					loc.mRadius = FValue.toInt( oo.get( IToken.LOCATION_RADIUS));
					loc.mVisibility = Visibility.parse( FValue.toString( oo.get( IToken.LOCATION_VISIBLE)));
					loc.mDim = FValue.toInt( oo.get( IToken.LOCATION_DIM));
				}
			}
		}
		return null;
	}

	@Override
	public Object forTaskMob( FQuestTaskMob task, FObject obj) {
		FArray arr = FArray.to( obj.get( IToken.TASK_MOBS));
		if (arr != null) {
			for (IJson json : arr) {
				FObject oo = FObject.to( json);
				if (oo != null) {
					String name = FValue.toString( oo.get( IToken.MOB_NAME));
					FMob mob = task.createMob( name);
					mob.mIcon = readIcon( oo.get( IToken.MOB_ICON));
					mob.mMob = FValue.toString( oo.get( IToken.MOB_MOB2));
					mob.mKills = FValue.toInt( oo.get( IToken.MOB_COUNT));
					mob.mExact = FValue.toBoolean( oo.get( IToken.MOB_EXACT));
				}
			}
		}
		return null;
	}

	@Override
	public Object forTaskReputationKill( FQuestTaskReputationKill task, FObject obj) {
		task.mKills = FValue.toInt( obj.get( IToken.TASK_KILLS));
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, FObject obj) {
		FArray arr = FArray.to( obj.get( IToken.TASK_SETTINGS));
		if (arr != null) {
			for (IJson json : arr) {
				FObject oo = FObject.to( json);
				if (oo != null) {
					FSetting res = task.createSetting();
					int repID = FReputation.fromIdent( FValue.toString( oo.get( IToken.SETTING_REPUTATION)));
					if (repID < 0) {
						Utils.log( LOGGER, Level.WARNING, "missing repID");
					}
					else {
						res.mRep = ReputationOfID.get( task, repID);
					}
					String low = FValue.toString( oo.get( IToken.SETTING_LOWER));
					if (low != null) {
						int lowerID = FMarker.fromIdent( low);
						if (lowerID < 0) {
							Utils.log( LOGGER, Level.WARNING, "missing lowerID");
						}
						else {
							res.mLower = MarkerOfID.get( res.mRep, lowerID);
						}
					}
					String upp = FValue.toString( oo.get( IToken.SETTING_UPPER));
					if (upp != null) {
						int upperID = FMarker.fromIdent( upp);
						if (upperID < 0) {
							Utils.log( LOGGER, Level.WARNING, "missing upperID");
						}
						else {
							res.mUpper = MarkerOfID.get( res.mRep, upperID);
						}
					}
					res.mInverted = FValue.toBoolean( oo.get( IToken.SETTING_INVERTED));
				}
			}
		}
		return null;
	}

	private void readGroup( FGroupTierCat cat, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String name = FValue.toString( obj.get( IToken.GROUP_NAME));
					FGroupTier tier = null;
					int tierID = FGroupTier.fromIdent( FValue.toString( obj.get( IToken.GROUP_TIER)));
					if (tierID < 0) {
						Utils.log( LOGGER, Level.WARNING, "missing tierID of group {0}", name);
					}
					else {
						tier = GroupTierOfID.get( cat, tierID);
					}
					if (tier == null) {
						Utils.log( LOGGER, Level.WARNING, "missing tierID of group {0}", name);
						tier = cat.createMember( "__Missing__");
					}
					FGroup grp = tier.createGroup( name);
					grp.mLimit = FValue.toIntObj( obj.get( IToken.GROUP_LIMIT));
					readStacks( grp.mStacks, FArray.to( obj.get( IToken.GROUP_STACKS)));
				}
			}
		}
	}

	private void readGroupTiers( FGroupTierCat cat, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FGroupTier tier = cat.createMember( FValue.toString( obj.get( IToken.GROUP_TIER_NAME)));
					Integer idx = FValue.toIntObj( obj.get( IToken.GROUP_TIER_ID));
					if (idx != null) {
						tier.setID( FGroupTier.toIdent( idx.intValue()));
					}
					else {
						tier.setID( FValue.toString( obj.get( IToken.GROUP_TIER_ID)));
					}
					tier.mColorID = FValue.toInt( obj.get( IToken.GROUP_TIER_COLOR));
					FArray weights = FArray.to( obj.get( IToken.GROUP_TIER_WEIGHTS));
					if (weights != null) {
						int[] ww = BagTier.newArray();
						for (int i = 0; i < ww.length; ++i) {
							ww[i] = FValue.toInt( weights.get( i));
						}
						tier.mWeights = ww;
					}
				}
			}
		}
	}

	private FItemStack readIcon( IJson json) {
		FObject arr = FObject.to( json);
		if (arr != null) {
			String seq = FValue.toString( arr.get( IToken.ITEM_OBJECT));
			String nbt = FValue.toString( arr.get( IToken.ITEM_NBT));
			return FItemStack.parse( seq, nbt);
		}
		String str = FValue.toString( json);
		if (str != null) {
			return FItemStack.parse( str);
		}
		return null;
	}

	private void readMarker( FReputation rep, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FMarker marker = rep.createMarker( FValue.toString( obj.get( IToken.MARKER_NAME)));
					marker.setID( FValue.toString( obj.get( IToken.MARKER_ID)));
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
				String id = FQuest.toIdent( FQuest.fromIdent( String.valueOf( FValue.to( arr.get( i)))));
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
				info.mTotal = FValue.toInt( obj.get( IToken.REPEAT_INFO_TOTAL));
			}
		}
	}

	private void readQuests( FHqm hqm, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					if (FValue.toBoolean( obj.get( IToken.QUEST_DELETED))) {
//						mQuests.add( mDelQuest);
					}
					else {
						String name = FValue.toString( obj.get( IToken.QUEST_NAME));
						FQuestSet qs = null;
						int setID = FQuestSet.fromIdent( FValue.toString( obj.get( IToken.QUEST_SET)));
						if (setID < 0) {
							Utils.log( LOGGER, Level.WARNING, "missing setID of quest {0}", name);
						}
						else {
							qs = QuestSetOfID.get( hqm.mQuestSetCat, setID);
						}
						if (qs == null) {
							Utils.log( LOGGER, Level.WARNING, "missing set of quest {0}", name);
							qs = hqm.mQuestSetCat.createMember( "__Missing__");
						}
						FQuest quest = qs.createQuest( name);
						Integer idx = FValue.toIntObj( obj.get( IToken.QUEST_ID));
						if (idx != null) {
							quest.setID( FQuest.toIdent( idx.intValue()));
						}
						else {
							quest.setID( FValue.toString( obj.get( IToken.QUEST_ID)));
						}
						quest.mDescr = FValue.toString( obj.get( IToken.QUEST_DESC));
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
						quest.mCount = FValue.toIntObj( obj.get( IToken.QUEST_PARENT_REQUIREMENT_COUNT));
						readTasks( quest, FArray.to( obj.get( IToken.QUEST_TASKS)));
						readStacks( quest.mRewards, FArray.to( obj.get( IToken.QUEST_REWARD)));
						readStacks( quest.mChoices, FArray.to( obj.get( IToken.QUEST_CHOICE)));
						readRewards( quest, FArray.to( obj.get( IToken.QUEST_REP_REWRDS)));
						mQuests.put( quest.toIdent(), quest);
					}
				}
			}
		}
	}

	private void readQuestSetCat( FQuestSetCat cat, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FQuestSet set = cat.createMember( FValue.toString( obj.get( IToken.QUEST_SET_NAME)));
					set.mDescr = FValue.toString( obj.get( IToken.QUEST_SET_DECR));
					set.setID( FValue.toString( obj.get( IToken.QUEST_SET_ID)));
				}
			}
		}
	}

	private void readReputations( FReputationCat cat, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FReputation rep = cat.createMember( FValue.toString( obj.get( IToken.REPUTATION_NAME)));
					rep.mNeutral = FValue.toString( obj.get( IToken.REPUTATION_NEUTRAL));
					rep.setID( FValue.toString( obj.get( IToken.REPUTATION_ID)));
					readMarker( rep, FArray.to( obj.get( IToken.REPUTATION_MARKERS)));
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

	@Override
	public void readSrc( FHqm hqm) {
		try {
			IJson json = mSrc.doAll();
			FObject obj = FObject.to( json);
			if (obj != null) {
				hqm.setVersion( FileVersion.parse( FValue.toString( obj.get( IToken.HQM_VERSION))));
				hqm.mPassCode = FValue.toString( obj.get( IToken.HQM_PASSCODE));
				hqm.mDescr = FValue.toString( obj.get( IToken.HQM_DECRIPTION));
				if (hqm.mDescr == null) {
					hqm.mDescr = "No description";
				}
				readQuestSetCat( hqm.mQuestSetCat, FArray.to( obj.get( IToken.HQM_QUEST_SET_CAT)));
				readReputations( hqm.mReputationCat, FArray.to( obj.get( IToken.HQM_REPUTATION_CAT)));
				readQuests( hqm, FArray.to( obj.get( IToken.HQM_QUESTS)));
				readGroupTiers( hqm.mGroupTierCat, FArray.to( obj.get( IToken.HQM_GROUP_TIER_CAT)));
				readGroup( hqm.mGroupTierCat, FArray.to( obj.get( IToken.HQM_GROUP_CAT)));
				ReindexOfQuests.get( hqm);
				updateRequirements( hqm);
				updateOptionLinks( hqm);
				updatePosts( hqm);
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	private void readStacks( Vector<FItemStack> param, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String seq = FValue.toString( obj.get( IToken.ITEM_OBJECT));
					String nbt = FValue.toString( obj.get( IToken.ITEM_NBT));
					param.add( FItemStack.parse( seq, nbt));
				}
				else {
					String str = FValue.toString( json);
					if (str != null) {
						param.add( FItemStack.parse( str));
					}
					else {
						param.add( new FItemStack( "unknown"));
					}
				}
			}
		}
	}

	private void readTasks( FQuest quest, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					TaskTyp type = TaskTyp.parse( FValue.toString( obj.get( IToken.TASK_TYPE)));
					AQuestTask task = quest.createQuestTask( type, FValue.toString( obj.get( IToken.TASK_NAME)));
					task.mDescr = FValue.toString( obj.get( IToken.TASK_DESC));
					task.setID( FValue.toString( obj.get( IToken.TASK_ID)));
					task.accept( this, obj);
				}
			}
		}
	}

	private void updateOptionLinks( FHqm hqm) {
		for (Map.Entry<FQuest, String[]> e : mOptionLinks.entrySet()) {
			FQuest quest = e.getKey();
			String[] ids = e.getValue();
			for (int i = 0; i < ids.length; ++i) {
				String id = ids[i];
				FQuest req = mQuests.get( id);
				if (req == null) {
					Utils.log( LOGGER, Level.WARNING, "missing OptionLink [{0}] {1} for {2}", i, id, quest.mName);
				}
				else {
					quest.mOptionLinks.add( req);
				}
			}
		}
	}

	private void updatePosts( FHqm hqm) {
		for (Map.Entry<String, Vector<FQuest>> e : mPosts.entrySet()) {
			String id = e.getKey();
			FQuest quest = mQuests.get( id);
			if (quest == null) {
				Utils.log( LOGGER, Level.WARNING, "missing posts {0}", id);
			}
			else {
				quest.mPosts.addAll( e.getValue());
			}
		}
	}

	private void updateRequirements( FHqm hqm) {
		for (Map.Entry<FQuest, String[]> e : mRequirements.entrySet()) {
			FQuest quest = e.getKey();
			String[] ids = e.getValue();
			for (int i = 0; i < ids.length; ++i) {
				String id = ids[i];
				FQuest req = mQuests.get( id);
				if (req == null) {
					Utils.log( LOGGER, Level.WARNING, "missing Requirement [{0}] {1} for {2}", i, id, quest.mName);
				}
				else {
					quest.mRequirements.add( req);
				}
			}
		}
	}
}
