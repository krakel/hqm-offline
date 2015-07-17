package de.doerl.hqm.medium.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.GroupOfID;
import de.doerl.hqm.base.dispatch.GroupTierOfID;
import de.doerl.hqm.base.dispatch.LocationOfId;
import de.doerl.hqm.base.dispatch.MarkerOfID;
import de.doerl.hqm.base.dispatch.MobOfId;
import de.doerl.hqm.base.dispatch.QuestOfID;
import de.doerl.hqm.base.dispatch.QuestSetOfID;
import de.doerl.hqm.base.dispatch.ReputationOfID;
import de.doerl.hqm.base.dispatch.TaskOfId;
import de.doerl.hqm.medium.IHqmReader;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.json.FArray;
import de.doerl.hqm.utils.json.FObject;
import de.doerl.hqm.utils.json.FValue;
import de.doerl.hqm.utils.json.IJson;
import de.doerl.hqm.utils.json.JsonReader;

class ParserLang extends AHQMWorker<Object, FObject> implements IHqmReader, IToken {
	private static final Logger LOGGER = Logger.getLogger( ParserLang.class.getName());
	private JsonReader mSrc;
	private String mLang;

	public ParserLang( InputStream is, String lang) throws IOException {
		mSrc = new JsonReader( is);
		mLang = lang;
	}

	@Override
	protected Object doTask( AQuestTask task, FObject p) {
		return null;
	}

	@Override
	public Object forTaskLocation( FQuestTaskLocation task, FObject obj) {
		FArray arr = FArray.to( obj.get( IToken.TASK_LOCATIONS));
		if (arr != null) {
			for (IJson json : arr) {
				FObject oo = FObject.to( json);
				if (oo != null) {
					readLocation( task, oo);
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
					readMob( task, oo);
				}
			}
		}
		return null;
	}

	private void readGroups( FGroupTier tier, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String ident = FValue.toString( obj.get( IToken.GROUP_ID));
					FGroup grp = GroupOfID.get( tier, FGroup.fromIdent( ident));
					if (grp == null) {
						Utils.log( LOGGER, Level.WARNING, "missing FGroup for ident {0}", ident);
					}
					else {
						grp.setName( mLang, FValue.toString( obj.get( IToken.GROUP_NAME)));
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
					FGroupTier tier = GroupTierOfID.get( cat, FGroupTier.fromIdent( ident));
					if (tier == null) {
						Utils.log( LOGGER, Level.WARNING, "missing FGroupTier for ident {0}", ident);
					}
					else {
						tier.setName( mLang, FValue.toString( obj.get( IToken.GROUP_TIER_NAME)));
						readGroups( tier, FArray.to( obj.get( IToken.GROUP_TIER_GROUPS)));
					}
				}
			}
		}
	}

	private void readLocation( FQuestTaskLocation task, FObject obj) {
		String ident = FValue.toString( obj.get( IToken.LOCATION_ID));
		FLocation loc = LocationOfId.get( task, FLocation.fromIdent( ident));
		if (loc == null) {
			Utils.log( LOGGER, Level.WARNING, "missing FLocation for ident {0}", ident);
		}
		else {
			loc.setName( mLang, FValue.toString( obj.get( IToken.LOCATION_NAME)));
		}
	}

	private void readMarker( FReputation rep, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String ident = FValue.toString( obj.get( IToken.MARKER_ID));
					FMarker marker = MarkerOfID.get( rep, FMarker.fromIdent( ident));
					if (marker == null) {
						Utils.log( LOGGER, Level.WARNING, "missing FMarker for ident {0}", ident);
					}
					else {
						marker.setName( mLang, FValue.toString( obj.get( IToken.MARKER_NAME)));
					}
				}
			}
		}
	}

	private void readMob( FQuestTaskMob task, FObject obj) {
		String ident = FValue.toString( obj.get( IToken.MOB_ID));
		FMob mob = MobOfId.get( task, FMob.fromIdent( ident));
		if (mob == null) {
			Utils.log( LOGGER, Level.WARNING, "missing FMob for ident {0}", ident);
		}
		else {
			mob.setName( mLang, FValue.toString( obj.get( IToken.MOB_NAME)));
		}
	}

	private void readQuests( FQuestSet set, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String ident = FValue.toString( obj.get( IToken.QUEST_ID));
					FQuest quest = QuestOfID.get( set, FQuest.fromIdent( ident));
					if (quest == null) {
						Utils.log( LOGGER, Level.WARNING, "missing FQuest for ident {0}", ident);
					}
					else {
						quest.setName( mLang, FValue.toString( obj.get( IToken.QUEST_NAME)));
						quest.setDescr( mLang, FValue.toString( obj.get( IToken.QUEST_DESC)));
						readTasks( quest, FArray.to( obj.get( IToken.QUEST_TASKS)));
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
					String ident = FValue.toString( obj.get( IToken.QUEST_SET_ID));
					FQuestSet set = QuestSetOfID.get( cat, FQuestSet.fromIdent( ident));
					if (set == null) {
						Utils.log( LOGGER, Level.WARNING, "missing FQuestSet for ident {0}", ident);
					}
					else {
						set.setName( mLang, FValue.toString( obj.get( IToken.QUEST_SET_NAME)));
						set.setDescr( mLang, FValue.toString( obj.get( IToken.QUEST_SET_DECR)));
						readQuests( set, FArray.to( obj.get( IToken.QUEST_SET_QUESTS)));
					}
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
					FReputation rep = ReputationOfID.get( cat, FReputation.fromIdent( ident));
					if (rep == null) {
						Utils.log( LOGGER, Level.WARNING, "missing FReputation for ident {0}", ident);
					}
					else {
						rep.setName( mLang, FValue.toString( obj.get( IToken.REPUTATION_NAME)));
						rep.setNeutral( mLang, FValue.toString( obj.get( IToken.REPUTATION_NEUTRAL)));
						readMarker( rep, FArray.to( obj.get( IToken.REPUTATION_MARKERS)));
					}
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
				String descr = FValue.toString( obj.get( IToken.HQM_DECRIPTION));
				hqm.setDescr( mLang, descr != null ? descr : "No description");
				readReputations( hqm.mReputationCat, FArray.to( obj.get( IToken.HQM_REPUTATION_CAT)));
				readQuestSetCat( hqm.mQuestSetCat, FArray.to( obj.get( IToken.HQM_QUEST_SET_CAT)));
				readGroupTiers( hqm.mGroupTierCat, FArray.to( obj.get( IToken.HQM_GROUP_TIER_CAT)));
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	private void readTasks( FQuest quest, FArray arr) {
		if (arr != null) {
			for (IJson json : arr) {
				FObject obj = FObject.to( json);
				if (obj != null) {
					String ident = FValue.toString( obj.get( IToken.TASK_ID));
					AQuestTask task = TaskOfId.get( quest, AQuestTask.fromIdent( ident));
					if (task == null) {
						Utils.log( LOGGER, Level.WARNING, "missing QuestTask for ident {0}", ident);
					}
					else {
						task.setName( mLang, FValue.toString( obj.get( IToken.TASK_NAME)));
						task.setDescr( mLang, FValue.toString( obj.get( IToken.TASK_DESC)));
						task.accept( this, obj);
					}
				}
			}
		}
	}
}
