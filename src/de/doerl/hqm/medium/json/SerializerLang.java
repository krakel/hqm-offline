package de.doerl.hqm.medium.json;

import java.io.IOException;
import java.io.OutputStream;

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
import de.doerl.hqm.base.dispatch.ReindexOfQuests;
import de.doerl.hqm.medium.IHqmWriter;
import de.doerl.hqm.utils.json.JsonWriter;

class SerializerLang extends AHQMWorker<Object, Object> implements IHqmWriter, IToken {
	private JsonWriter mDst;
	private String mLang;

	public SerializerLang( OutputStream os, String lang) throws IOException {
		mDst = new JsonWriter( os);
		mLang = lang;
	}

	private void doTask( AQuestTask task) {
		mDst.print( TASK_ID, task.toIdent());
		mDst.print( TASK_NAME, task.getName( mLang));
		mDst.print( TASK_DESC, task.getDescr( mLang));
	}

	@Override
	protected Object doTask( AQuestTask task, Object p) {
		mDst.beginObject();
		doTask( task);
		mDst.endObject();
		return null;
	}

	public void flushDst() {
		mDst.flush();
	}

	@Override
	public Object forGroup( FGroup grp, Object p) {
		mDst.beginObject();
		mDst.print( GROUP_ID, grp.toIdent());
		mDst.print( GROUP_NAME, grp.getName( mLang));
		mDst.endObject();
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, Object p) {
		mDst.beginObject();
		mDst.print( GROUP_TIER_ID, tier.toIdent());
		mDst.print( GROUP_TIER_NAME, tier.getName( mLang));
		writeGroups( tier);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forLocation( FLocation loc, Object p) {
		mDst.beginObject();
		mDst.print( LOCATION_ID, loc.toIdent());
		mDst.print( LOCATION_NAME, loc.getName( mLang));
		mDst.endObject();
		return null;
	}

	@Override
	public Object forMarker( FMarker mark, Object p) {
		mDst.beginObject();
		mDst.print( MARKER_ID, mark.toIdent());
		mDst.print( MARKER_NAME, mark.getName( mLang));
		mDst.endObject();
		return null;
	}

	@Override
	public Object forMob( FMob mob, Object p) {
		mDst.beginObject();
		mDst.print( MOB_ID, mob.toIdent());
		mDst.print( MOB_NAME, mob.getName( mLang));
		mDst.endObject();
		return null;
	}

	@Override
	public Object forQuest( FQuest quest, Object p) {
		mDst.beginObject();
		mDst.print( QUEST_ID, quest.toIdent());
		mDst.print( QUEST_NAME, quest.getName( mLang));
		mDst.print( QUEST_DESC, quest.getDescr( mLang));
		writeTasks( quest);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		mDst.beginObject();
		mDst.print( QUEST_SET_ID, set.toIdent());
		mDst.print( QUEST_SET_NAME, set.getName( mLang));
		mDst.print( QUEST_SET_DECR, set.getDescr( mLang));
		writeQuests( set);
		mDst.endObject();
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, Object p) {
		mDst.beginObject();
		mDst.print( REPUTATION_ID, rep.toIdent());
		mDst.print( REPUTATION_NAME, rep.getName( mLang));
		mDst.print( REPUTATION_NEUTRAL, rep.getNeutral( mLang));
		writeMarkers( rep);
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
	public void writeDst( FHqm hqm) {
		ReindexOfQuests.get( hqm);
		mDst.beginObject();
		mDst.print( HQM_PARENT, hqm.getName());
		mDst.print( HQM_DECRIPTION, hqm.getDescr( mLang));
		writeQuestSetCat( hqm.mQuestSetCat);
		writeReputations( hqm.mReputationCat);
		writeGroupTiers( hqm.mGroupTierCat);
		mDst.endObject();
	}

	private void writeGroups( FGroupTier tier) {
		mDst.beginArray( GROUP_TIER_GROUPS);
		tier.forEachGroup( this, null);
		mDst.endArray();
	}

	private void writeGroupTiers( FGroupTierCat cat) {
		mDst.beginArray( HQM_GROUP_TIER_CAT);
		cat.forEachMember( this, null);
		mDst.endArray();
	}

	private void writeMarkers( FReputation rep) {
		mDst.beginArray( REPUTATION_MARKERS);
		rep.forEachMarker( this, null);
		mDst.endArray();
	}

	private void writeQuests( FQuestSet set) {
		mDst.beginArray( QUEST_SET_QUESTS);
		set.forEachQuest( this, null);
		mDst.endArray();
	}

	private void writeQuestSetCat( FQuestSetCat set) {
		mDst.beginArray( HQM_QUEST_SET_CAT);
		set.forEachMember( this, null);
		mDst.endArray();
	}

	private void writeReputations( FReputationCat set) {
		mDst.beginArray( HQM_REPUTATION_CAT);
		set.forEachMember( this, null);
		mDst.endArray();
	}

	private void writeTasks( FQuest quest) {
		mDst.beginArray( QUEST_TASKS);
		quest.forEachTask( this, null);
		mDst.endArray();
	}
}
