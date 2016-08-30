package de.doerl.hqm.medium.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import de.doerl.hqm.base.dispatch.GroupOfID;
import de.doerl.hqm.base.dispatch.GroupTierOfID;
import de.doerl.hqm.base.dispatch.LocationOfID;
import de.doerl.hqm.base.dispatch.MarkerOfID;
import de.doerl.hqm.base.dispatch.MobOfID;
import de.doerl.hqm.base.dispatch.QuestOfID;
import de.doerl.hqm.base.dispatch.QuestSetOfID;
import de.doerl.hqm.base.dispatch.ReputationOfID;
import de.doerl.hqm.base.dispatch.TaskOfID;
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
import de.doerl.hqm.utils.nbt.NbtParser;

public class Parser extends AHQMWorker<Object, FObject> implements IToken {
	private static final Logger LOGGER = Logger.getLogger( Parser.class.getName());
	private static final Pattern PATTERN_FLUID = Pattern.compile( "(.*?) amount\\((\\d*)\\)");
	private static final Pattern PATTERN_ITEM = Pattern.compile( "(.*?) size\\((\\d*)\\) dmg\\((\\d*)\\)");
	private HashMap<FQuest, int[]> mRequirements = new HashMap<>();
	private HashMap<FQuest, int[]> mOptionLinks = new HashMap<>();
	private HashMap<Integer, ArrayList<FQuest>> mPosts = new HashMap<>();
	private FLanguage mLang;
	private boolean mMain;
	private boolean mDocu;

	public Parser( FLanguage lang, boolean withMain, boolean withDocu) {
		mLang = lang;
		mMain = withMain;
		mDocu = withDocu;
	}

	public static FItemStack parseItemStack( String itemSeq, String nbt) {
		Matcher mm = PATTERN_ITEM.matcher( itemSeq);
		if (mm.find()) {
			try {
				String name = mm.group( 1);
				int size = Utils.parseInteger( mm.group( 2));
				int dmg = Utils.parseInteger( mm.group( 3));
				return new FItemStack( name, dmg, size, NbtParser.parse( nbt));
			}
			catch (RuntimeException ex) {
				Utils.log( LOGGER, Level.WARNING, "illagle pattern: {0}", itemSeq);
			}
			return new FItemStack( "item:unknown", 0, 1, null);
		}
		else {
			return new FItemStack( itemSeq, 0, 1, NbtParser.parse( nbt));
		}
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
		if (mMain) {
			readTaskRequirements( task, FArray.to( obj.get( IToken.TASK_REQUIREMENTS)));
		}
		return null;
	}

	@Override
	protected Object doTaskReputation( AQuestTaskReputation task, FObject obj) {
		if (mMain) {
			readTaskReputations( task, FArray.to( obj.get( IToken.TASK_SETTINGS)));
		}
		return null;
	}

	@Override
	public Object forTaskDeath( FQuestTaskDeath task, FObject obj) {
		if (mMain) {
			task.mDeaths = FValue.toInt( obj.get( IToken.TASK_DEATHS));
		}
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
		if (mMain) {
			task.mKills = FValue.toInt( obj.get( IToken.TASK_KILLS));
		}
		return null;
	}

	@Override
	public Object forTaskReputationTarget( FQuestTaskReputationTarget task, FObject obj) {
		doTaskReputation( task, obj);
		return null;
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
					String ident = FValue.toString( obj.get( IToken.GROUP_ID));
					FGroup grp = null;
					if (!mMain) {
						grp = GroupOfID.get( tier, ident);
						if (grp == null) {
							Utils.log( LOGGER, Level.WARNING, "missing group for ident {0}", ident);
						}
					}
					if (grp == null) {
						grp = tier.createGroup();
						grp.setID( ident);
					}
					if (mDocu) {
						grp.setName( mLang, FValue.toString( obj.get( IToken.GROUP_NAME)));
					}
					if (mMain) {
						grp.mLimit = FValue.toIntObj( obj.get( IToken.GROUP_LIMIT));
						readStacks( grp.mStacks, FArray.to( obj.get( IToken.GROUP_STACKS)));
					}
				}
			}
		}
	}

	private void readGroupTiers( FGroupTierCat cat, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String ident = FValue.toString( obj.get( IToken.GROUP_TIER_ID));
					FGroupTier tier = null;
					if (!mMain) {
						tier = GroupTierOfID.get( cat, ident);
						if (tier == null) {
							Utils.log( LOGGER, Level.WARNING, "missing group tier for ident {0}", ident);
						}
					}
					if (tier == null) {
						tier = cat.createMember();
						tier.setID( ident);
					}
					if (mDocu) {
						tier.setName( mLang, FValue.toString( obj.get( IToken.GROUP_TIER_NAME)));
					}
					if (mMain) {
						tier.mColorID = FValue.toInt( obj.get( IToken.GROUP_TIER_COLOR));
						FArray ww = FArray.to( obj.get( IToken.GROUP_TIER_WEIGHTS));
						if (ww != null) {
							int size = Math.min( ww.size(), tier.mWeights.length);
							for (int i = 0; i < size; ++i) {
								tier.mWeights[i] = FValue.toInt( ww.get( i));
							}
						}
					}
					readGroups( tier, FArray.to( obj.get( IToken.GROUP_TIER_GROUPS)));
				}
			}
		}
	}

	private FItemStack readItem( IJson json) {
		FObject arr = FObject.to( json);
		if (arr != null) {
			String seq = FValue.toString( arr.get( IToken.ITEM_OBJECT));
			if (seq != null) {
				return parseItemStack( seq, FValue.toString( arr.get( IToken.ITEM_NBT)));
			}
		}
		else {
			String seq = FValue.toString( json);
			if (seq != null) {
				return parseItemStack( seq, null);
			}
		}
		return null;
	}

	private void readLanguages( FHqm hqm, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				String lang = FValue.toString( json);
				if (lang != null && hqm.getLanguage( lang) == null) {
					hqm.createLanguage( lang);
				}
			}
		}
		if (hqm.mLanguages.size() == 0) {
			hqm.createLanguage( FHqm.LANG_EN_US);
		}
	}

	private void readMarker( FReputation rep, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String ident = FValue.toString( obj.get( IToken.MARKER_ID));
					FMarker marker = null;
					if (!mMain) {
						marker = MarkerOfID.get( rep, ident);
						if (marker == null) {
							Utils.log( LOGGER, Level.WARNING, "missing marker for ident {0}", ident);
						}
					}
					if (marker == null) {
						marker = rep.createMarker();
						marker.setID( ident);
					}
					if (mDocu) {
						marker.setName( mLang, FValue.toString( obj.get( IToken.MARKER_NAME)));
					}
					if (mMain) {
						marker.mMark = FValue.toInt( obj.get( IToken.MARKER_VALUE));
					}
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
				info.mTotal = FValue.toInt( obj.get( IToken.REPEAT_INFO_TOTAL));
			}
		}
	}

	private void readQuests( FQuestSet set, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String ident = FValue.toString( obj.get( IToken.QUEST_ID));
					FQuest quest = null;
					if (!mMain) {
						quest = QuestOfID.get( set, ident);
						if (quest == null) {
							Utils.log( LOGGER, Level.WARNING, "missing quest for ident {0}", ident);
						}
					}
					if (quest == null) {
						quest = set.createQuest();
						quest.setID( ident);
					}
					if (mDocu) {
						quest.setName( mLang, FValue.toString( obj.get( IToken.QUEST_NAME)));
						quest.setDescr( mLang, FValue.toString( obj.get( IToken.QUEST_DESC)));
					}
					if (mMain) {
						quest.mUUID = FValue.toString( obj.get( IToken.QUEST_UUID));
						quest.mX = FValue.toInt( obj.get( IToken.QUEST_X));
						quest.mY = FValue.toInt( obj.get( IToken.QUEST_Y));
						quest.mBig = FValue.toBoolean( obj.get( IToken.QUEST_BIG));
						quest.mIcon = readItem( obj.get( IToken.QUEST_ICON));
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
					}
					readTasks( quest, FArray.to( obj.get( IToken.QUEST_TASKS)));
				}
			}
		}
	}

	private void readQuestSetCat( FQuestSetCat cat, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String ident = FValue.toString( obj.get( IToken.QUEST_SET_ID));
					FQuestSet set = null;
					if (!mMain) {
						set = QuestSetOfID.get( cat, ident);
						if (set == null) {
							Utils.log( LOGGER, Level.WARNING, "missing quest set for ident {0}", ident);
						}
					}
					if (set == null) {
						set = cat.createMember();
						set.setID( ident);
					}
					if (mDocu) {
						set.setName( mLang, FValue.toString( obj.get( IToken.QUEST_SET_NAME)));
						set.setDescr( mLang, FValue.toString( obj.get( IToken.QUEST_SET_DECR)));
					}
					readBars( set, FArray.to( obj.get( IToken.QUEST_SET_BARS)));
					readQuests( set, FArray.to( obj.get( IToken.QUEST_SET_QUESTS)));
				}
			}
		}
	}

	private void readReputations( FReputationCat cat, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String ident = FValue.toString( obj.get( IToken.REPUTATION_ID));
					FReputation rep = null;
					if (!mMain) {
						rep = ReputationOfID.get( cat, ident);
						if (rep == null) {
							Utils.log( LOGGER, Level.WARNING, "missing reputation for ident {0}", ident);
						}
					}
					if (rep == null) {
						rep = cat.createMember();
						rep.setID( ident);
					}
					if (mDocu) {
						rep.setName( mLang, FValue.toString( obj.get( IToken.REPUTATION_NAME)));
						rep.setDescr( mLang, FValue.toString( obj.get( IToken.REPUTATION_NEUTRAL)));
					}
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

	void readSrc( FHqm hqm, FObject obj) {
		if (mMain) {
			hqm.setVersion( FileVersion.parse( FValue.toString( obj.get( IToken.HQM_VERSION))));
			hqm.mPassCode = FValue.toString( obj.get( IToken.HQM_PASSCODE));
			readLanguages( hqm, FArray.to( obj.get( IToken.HQM_LANGUAGES)));
			hqm.setMain( FValue.toString( obj.get( IToken.HQM_MAIN), mLang.mLocale));
		}
		if (mDocu) {
			String descr = FValue.toString( obj.get( IToken.HQM_DESCRIPTION));
			hqm.setDescr( mLang, descr != null ? descr : "No description");
		}
		readReputations( hqm.mReputationCat, FArray.to( obj.get( IToken.HQM_REPUTATION_CAT)));
		readQuestSetCat( hqm.mQuestSetCat, FArray.to( obj.get( IToken.HQM_QUEST_SET_CAT)));
		readGroupTiers( hqm.mGroupTierCat, FArray.to( obj.get( IToken.HQM_GROUP_TIER_CAT)));
		if (mMain) {
			ReindexOfQuests.get( hqm);
			updateRequirements( hqm);
			updateOptionLinks( hqm);
			updatePosts( hqm);
		}
	}

	private void readStacks( ArrayList<FItemStack> param, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FItemStack item = readItem( json);
				if (item != null) {
					param.add( item);
				}
			}
		}
	}

	private void readTaskLocations( FQuestTaskLocation task, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String ident = FValue.toString( obj.get( IToken.LOCATION_ID));
					FLocation loc = null;
					if (!mMain) {
						loc = LocationOfID.get( task, ident);
						if (loc == null) {
							Utils.log( LOGGER, Level.WARNING, "missing location for ident {0}", ident);
						}
					}
					if (loc == null) {
						loc = task.createLocation();
						loc.setID( ident);
					}
					if (mDocu) {
						loc.setName( mLang, FValue.toString( obj.get( IToken.LOCATION_NAME)));
					}
					if (mMain) {
						loc.mIcon = readItem( obj.get( IToken.LOCATION_ICON));
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
	}

	private void readTaskMobs( FQuestTaskMob task, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String ident = FValue.toString( obj.get( IToken.MOB_ID));
					FMob mob = null;
					if (!mMain) {
						mob = MobOfID.get( task, ident);
						if (mob == null) {
							Utils.log( LOGGER, Level.WARNING, "missing mob for ident {0}", ident);
						}
					}
					if (mob == null) {
						mob = task.createMob();
						mob.setID( ident);
					}
					if (mDocu) {
						mob.setName( mLang, FValue.toString( obj.get( IToken.MOB_NAME)));
					}
					if (mMain) {
						mob.mIcon = readItem( obj.get( IToken.MOB_ICON));
						mob.mMob = FValue.toString( obj.get( IToken.MOB_OBJECT));
						mob.mKills = FValue.toInt( obj.get( IToken.MOB_COUNT));
						mob.mExact = FValue.toBoolean( obj.get( IToken.MOB_EXACT));
					}
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

	private void readTaskRequirements( AQuestTaskItems task, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String itemSeq = FValue.toString( obj.get( IToken.ITEM_OBJECT));
					if (itemSeq != null) {
						FItemRequirement itemReq = task.createItemRequirement();
						itemReq.setStack( parseItemStack( itemSeq, FValue.toString( obj.get( IToken.ITEM_NBT))));
						itemReq.mAmount = FValue.toInt( obj.get( IToken.REQUIREMENT_REQUIRED));
						itemReq.mPrecision = ItemPrecision.parse( FValue.toString( obj.get( IToken.REQUIREMENT_PRECISION)));
					}
					else {
						FFluidRequirement fluidReq = task.createFluidRequirement();
						String fluidSeq = FValue.toString( obj.get( IToken.FLUID_OBJECT), "fluid:unknown");
						Matcher mm = PATTERN_FLUID.matcher( fluidSeq);
						if (mm.find()) {
							try {
								fluidReq.setStack( new FFluidStack( mm.group( 1)));
								fluidReq.mAmount = Utils.parseInteger( mm.group( 2));
							}
							catch (RuntimeException ex) {
								Utils.log( LOGGER, Level.WARNING, "illagle pattern: {0}", fluidSeq);
								fluidReq.setStack( new FFluidStack( "fluid:unknown"));
								fluidReq.mAmount = 1;
							}
						}
						else {
							fluidReq.setStack( new FFluidStack( fluidSeq));
							fluidReq.mAmount = FValue.toInt( obj.get( IToken.REQUIREMENT_REQUIRED));
						}
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
					String ident = FValue.toString( obj.get( IToken.TASK_ID));
					AQuestTask task = null;
					if (mMain) {
						TaskTyp type = TaskTyp.parse( FValue.toString( obj.get( IToken.TASK_TYPE)));
						task = quest.createQuestTask( type);
						task.setID( ident);
					}
					else {
						task = TaskOfID.get( quest, ident);
						if (task == null) {
							Utils.log( LOGGER, Level.WARNING, "missing quest task for ident {0}", ident);
							if (obj.get( IToken.TASK_LOCATIONS) != null) {
								task = quest.createQuestTask( TaskTyp.LOCATION);
							}
							else if (obj.get( IToken.TASK_MOBS) != null) {
								task = quest.createQuestTask( TaskTyp.KILL);
							}
							else {
								task = quest.createQuestTask( TaskTyp.DETECT);
							}
						}
					}
					if (mDocu) {
						task.setName( mLang, FValue.toString( obj.get( IToken.TASK_NAME)));
						task.setDescr( mLang, FValue.toString( obj.get( IToken.TASK_DESC)));
					}
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
