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
import de.doerl.hqm.medium.IHqmReader;
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
	private HashMap<FQuest, int[]> mRequirements = new HashMap<>();
	private HashMap<FQuest, int[]> mOptionLinks = new HashMap<>();
	private HashMap<Integer, Vector<FQuest>> mPosts = new HashMap<>();
	private Vector<FQuest> mQuests = new Vector<>();
	private JsonReader mSrc;

	public Parser( InputStream is) throws IOException {
		mSrc = new JsonReader( is);
	}

	private static int parseID( String s) {
		int pos = s.indexOf( " - ");
		if (pos > 0) {
			return Utils.parseInteger( s.substring( 0, pos), 0);
		}
		else {
			Utils.log( LOGGER, Level.WARNING, "wrong index {0}", s);
			return 0;
		}
	}

	private void addPost( FQuest quest, Integer id) {
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
					String name = FValue.toString( oo.get( IToken.REQUIREMENT_ITEM));
					if (name != null) {
						FItemRequirement item = task.createItemRequirement();
						item.mStack = FItemStack.parse( name);
						item.mRequired = FValue.toInt( oo.get( IToken.REQUIREMENT_REQUIRED));
						item.mPrecision = ItemPrecision.parse( FValue.toString( oo.get( IToken.REQUIREMENT_PRECISION)));
					}
					else {
						FFluidRequirement fluid = task.createFluidRequirement();
						fluid.mStack = FFluidStack.parse( FValue.toString( oo.get( IToken.REQUIREMENT_FLUID)));
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
					FItemStack icon = FItemStack.parse( FValue.toString( oo.get( IToken.LOCATION_ICON)));
					FLocation loc = task.createLocation( icon, FValue.toString( oo.get( IToken.LOCATION_NAME)));
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
					FItemStack icon = FItemStack.parse( FValue.toString( oo.get( IToken.MOB_ICON)));
					FMob mob = task.createMob( icon, FValue.toString( oo.get( IToken.MOB_NAME)));
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
					res.mRep = ReputationOfIdx.get( task, parseID( FValue.toString( oo.get( IToken.SETTING_REPUTATION))));
					String low = FValue.toString( oo.get( IToken.SETTING_LOWER));
					if (low != null) {
						res.mLower = MarkerOfIdx.get( res.mRep, parseID( low));
					}
					String upp = FValue.toString( oo.get( IToken.SETTING_UPPER));
					if (upp != null) {
						res.mUpper = MarkerOfIdx.get( res.mRep, parseID( upp));
					}
					res.mInverted = FValue.toBoolean( oo.get( IToken.SETTING_INVERTED));
				}
			}
		}
		return null;
	}

	private void readGroup( FGroupCat cat, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FGroup grp = cat.createMember( FValue.toString( obj.get( IToken.GROUP_NAME)));
					grp.mTier = GroupTierOfIdx.get( cat.mParentHQM, parseID( FValue.toString( obj.get( IToken.GROUP_TIER))));
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
					tier.mColorID = FValue.toInt( obj.get( IToken.GROUP_TIER_COLOR));
					FArray weights = FArray.to( obj.get( IToken.GROUP_TIER_WEIGHTS));
					if (weights != null) {
						int[] ww = new int[weights.size()];
						for (int i = 0; i < ww.length; ++i) {
							ww[i] = FValue.toInt( weights.get( i));
						}
						tier.mWeights = ww;
					}
				}
			}
		}
	}

	private void readMarker( FReputation rep, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FMarker marker = rep.createMarker( FValue.toString( obj.get( IToken.MARKER_NAME)));
					marker.mMark = FValue.toInt( obj.get( IToken.MARKER_VALUE));
				}
			}
		}
	}

	private void readQuestArr( FQuest quest, HashMap<FQuest, int[]> cache, FArray arr, boolean withPost) {
		if (arr != null) {
			int size = arr.size();
			int[] result = new int[size];
			for (int i = 0; i < size; ++i) {
				int id = parseID( FValue.to( arr.get( i)).toString());
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
					boolean isDelete = FValue.toBoolean( obj.get( IToken.QUEST_DELETED));
					if (isDelete) {
						mQuests.add( hqm.mQuestSetCat.addDeletedQuest());
					}
					else {
						int setID = parseID( FValue.toString( obj.get( IToken.QUEST_SET)));
						FQuestSet qs = QuestSetOfIdx.get( hqm.mQuestSetCat, setID);
						if (qs == null) {
							qs = hqm.mQuestSetCat.createMember( "__Missing__");
						}
						FQuest quest = qs.createQuest( FValue.toString( obj.get( IToken.QUEST_NAME)));
						quest.mDescr = FValue.toString( obj.get( IToken.QUEST_DESC));
						quest.mX = FValue.toInt( obj.get( IToken.QUEST_X));
						quest.mY = FValue.toInt( obj.get( IToken.QUEST_Y));
						quest.mBig = FValue.toBoolean( obj.get( IToken.QUEST_BIG));
						quest.mIcon = FItemStack.parse( FValue.toString( obj.get( IToken.QUEST_ICON)));
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
						quest.mReqCount = FValue.toIntObj( obj.get( IToken.QUEST_PARENT_REQUIREMENT_COUNT));
						readTasks( quest, FArray.to( obj.get( IToken.QUEST_TASKS)));
						readStacks( quest.mRewards, FArray.to( obj.get( IToken.QUEST_REWARD)));
						readStacks( quest.mChoices, FArray.to( obj.get( IToken.QUEST_CHOICE)));
						readRewards( quest, FArray.to( obj.get( IToken.QUEST_REPUTATIONS)));
						mQuests.add( quest);
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
					FQuestSet member = cat.createMember( FValue.toString( obj.get( IToken.QUEST_SET_NAME)));
					member.mDescr = FValue.toString( obj.get( IToken.QUEST_SET_DECR));
				}
			}
		}
	}

	private void readReputations( FReputationCat cat, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FReputation member = cat.createMember( FValue.toString( obj.get( IToken.REPUTATION_NAME)));
					member.mNeutral = FValue.toString( obj.get( IToken.REPUTATION_NEUTRAL));
					readMarker( member, FArray.to( obj.get( IToken.REPUTATION_MARKERS)));
				}
			}
		}
	}

	private void readRewards( FQuest quest, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FReward reward = quest.createReputationReward();
					reward.mRep = ReputationOfIdx.get( quest.getHqm(), parseID( FValue.toString( obj.get( IToken.REWARD_REPUTATION))));
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
				readQuestSetCat( hqm.mQuestSetCat, FArray.to( obj.get( IToken.HQM_QUEST_SET_CAT)));
				readReputations( hqm.mReputationCat, FArray.to( obj.get( IToken.HQM_REPUTATION_CAT)));
				readQuests( hqm, FArray.to( obj.get( IToken.HQM_QUESTS)));
				readGroupTiers( hqm.mGroupTierCat, FArray.to( obj.get( IToken.HQM_GROUP_TIER_CAT)));
				readGroup( hqm.mGroupCat, FArray.to( obj.get( IToken.HQM_GROUP_CAT)));
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
					String name = FValue.toString( obj.get( IToken.ITEM_NAME));
					String nbt = FValue.toString( obj.get( IToken.ITEM_NBT));
					param.add( FItemStack.parse( name, nbt));
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
					task.accept( this, obj);
				}
			}
		}
	}

	private void updateOptionLinks( FHqm hqm) {
		for (Map.Entry<FQuest, int[]> e : mOptionLinks.entrySet()) {
			FQuest quest = e.getKey();
			int[] ids = e.getValue();
			for (int i = 0; i < ids.length; ++i) {
				int id = ids[i];
				FQuest req = mQuests.get( id);
				if (req == null || req.isDeleted()) {
					Utils.log( LOGGER, Level.WARNING, "missing OptionLink [{0}] {1} for {2}", i, id, quest.mName);
				}
				else {
					quest.mOptionLinks.add( req);
				}
			}
		}
	}

	private void updatePosts( FHqm hqm) {
		for (Map.Entry<Integer, Vector<FQuest>> e : mPosts.entrySet()) {
			int id = e.getKey();
			Vector<FQuest> posts = e.getValue();
			FQuest quest = mQuests.get( id);
			if (quest == null || quest.isDeleted()) {
				Utils.log( LOGGER, Level.WARNING, "missing posts {0}", id);
			}
			else {
				quest.mPosts.addAll( posts);
			}
		}
	}

	private void updateRequirements( FHqm hqm) {
		for (Map.Entry<FQuest, int[]> e : mRequirements.entrySet()) {
			FQuest quest = e.getKey();
			int[] ids = e.getValue();
			for (int i = 0; i < ids.length; ++i) {
				int id = ids[i];
				FQuest req = mQuests.get( id);
				if (req == null || req.isDeleted()) {
					Utils.log( LOGGER, Level.WARNING, "missing Requirement [{0}] {1} for {2}", i, id, quest.mName);
				}
				else {
					quest.mRequirements.add( req);
				}
			}
		}
	}
}
