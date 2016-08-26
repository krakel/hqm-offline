package de.doerl.hqm.medium.gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FLanguage;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.FRepeatInfo;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationBar;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.QuestOfID;
import de.doerl.hqm.base.dispatch.ReputationOfID;
import de.doerl.hqm.quest.RepeatType;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.json.FArray;
import de.doerl.hqm.utils.json.FObject;
import de.doerl.hqm.utils.json.FValue;
import de.doerl.hqm.utils.json.IJson;

class Parser extends AHQMWorker<Object, FObject> implements IToken {
	private static final Logger LOGGER = Logger.getLogger( Parser.class.getName());
	private HashMap<FQuest, int[]> mRequirements = new HashMap<>();
	private HashMap<FQuest, int[]> mOptionLinks = new HashMap<>();
	private HashMap<Integer, ArrayList<FQuest>> mPosts = new HashMap<>();
	private FLanguage mLang;

	public Parser( FLanguage lang) {
		mLang = lang;
	}

	private void addPost( FQuest quest, Integer id) {
		ArrayList<FQuest> p = mPosts.get( id);
		if (p == null) {
			p = new ArrayList<>();
			mPosts.put( id, p);
		}
		p.add( quest);
	}

	private void loadDeaths( FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
//					SaveHandler.loadDeaths( file);
				}
			}
		}
	}

	private void loadDescription( FHqm hqm) {
		String descr = Medium.loadTxt( Medium.DESCRIPTION_FILE);
		hqm.setDescr( mLang, descr != null ? descr : "No description");
	}

	private void loadGroupTier( FGroupTierCat cat, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					FGroupTier tier = cat.createMember();
					tier.setID( FValue.toString( obj.get( IToken.GROUP_TIER_ID)));
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
			for (File file : files) {
				if (Medium.isQuestSet( file)) {
					loadQuestSet( cat, Medium.redJson( file));
				}
			}
		}
	}

	private void loadQuestSet( FQuestSetCat cat, FObject obj) {
		if (obj != null) {
			FQuestSet set = cat.createMember();
			set.setID( FValue.toString( obj.get( IToken.QUEST_SET_ID)));
			set.setName( mLang, FValue.toString( obj.get( IToken.QUEST_SET_NAME)));
			set.setDescr( mLang, FValue.toString( obj.get( IToken.QUEST_SET_DECR)));
			readBars( set, FArray.to( obj.get( IToken.QUEST_SET_BARS)));
			readQuests( set, FArray.to( obj.get( IToken.QUEST_SET_QUESTS)));
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

	private void loadState( FHqm hqm) {
		FObject obj = Medium.redJson( Medium.STATE_FILE);
		if (obj != null) {
			hqm.mQuesting = FValue.toBoolean( obj.get( IToken.HQM_QUESTING));
			hqm.mHadrcore = FValue.toBoolean( obj.get( IToken.HQM_HARDCORE));
		}
	}

	private void loadTeam( FHqm hqm) {
//		Team.loadAll(isClient);
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
					FMarker marker = rep.createMarker();
					marker.setID( FValue.toString( obj.get( IToken.MARKER_ID)));
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
					FQuest quest = set.createQuest();
					quest.setID( ident);
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
					quest.mCount = FValue.toIntObj( obj.get( IToken.QUEST_PARENT_REQUIREMENT_COUNT));
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

	void readSrc( FHqm hqm, FObject obj) {
		loadState( hqm);
		loadDescription( hqm);
		loadDeaths( FArray.to( Medium.redJson( Medium.DEATHS_FILE)));
		loadTeam( hqm);
		loadReputation( hqm.mReputationCat, FArray.to( Medium.redJson( Medium.REPUTATION_FILE)));
		loadQuestSet( hqm.mQuestSetCat, Medium.getFiles( Medium.MAIN_PATH));
		loadQuestingData( hqm, FArray.to( Medium.redJson( Medium.DATA_FILE)));
		loadGroupTier( hqm.mGroupTierCat, FArray.to( Medium.redJson( Medium.BAGS_FILE)));
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
						param.add( new FItemStack( null));
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
					TaskTyp type = TaskTyp.parse( FValue.toString( obj.get( IToken.TASK_TYPE)));
					task = quest.createQuestTask( type);
					task.setID( ident);
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
