package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.AMember;
import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.ARequirement;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReward;
import de.doerl.hqm.base.FSetting;

public class SizeOf extends AHQMWorker<Object, Object> {
	private int mResult = 0;

	private SizeOf() {
	}

	public static int getCategories( ACategory<?> cat) {
		SizeOf worker = new SizeOf();
		cat.forEachMember( worker, null);
		return worker.mResult;
	}

	public static int getLocations( FQuestTaskLocation task) {
		SizeOf size = new SizeOf();
		task.forEachLocation( size, null);
		return size.mResult;
	}

	public static int getMarker( FReputation rep) {
		SizeOf size = new SizeOf();
		rep.forEachMarker( size, null);
		return size.mResult;
	}

	public static int getMobs( FQuestTaskMob task) {
		SizeOf size = new SizeOf();
		task.forEachMob( size, null);
		return size.mResult;
	}

	public static int getQuests( FHqm hqm) {
		SizeOf size = new SizeOf();
		hqm.forEachQuest( size, null);
		return size.mResult;
	}

	public static int getRequirements( AQuestTaskItems task) {
		SizeOf size = new SizeOf();
		task.forEachRequirement( size, null);
		return size.mResult;
	}

	public static int getReward( FQuest quest) {
		SizeOf size = new SizeOf();
		quest.forEachReward( size, null);
		return size.mResult;
	}

	public static int getSettings( FQuestTaskReputationTarget task) {
		SizeOf size = new SizeOf();
		task.forEachSetting( size, null);
		return size.mResult;
	}

	public static int getTasks( FQuest quest) {
		SizeOf size = new SizeOf();
		quest.forEachTask( size, null);
		return size.mResult;
	}

	@Override
	protected Object doMember( AMember<? extends ANamed> member, Object p) {
		++mResult;
		return null;
	}

	@Override
	protected Object doRequirement( ARequirement req, Object p) {
		++mResult;
		return null;
	}

	@Override
	protected Object doTask( AQuestTask task, Object p) {
		++mResult;
		return null;
	}

	@Override
	public Object forLocation( FLocation loc, Object p) {
		++mResult;
		return null;
	}

	@Override
	public Object forMarker( FMarker mark, Object p) {
		++mResult;
		return null;
	}

	@Override
	public Object forMob( FMob mob, Object p) {
		++mResult;
		return null;
	}

	@Override
	public Object forQuest( FQuest quest, Object p) {
		++mResult;
		return null;
	}

	@Override
	public Object forReward( FReward rr, Object p) {
		++mResult;
		return null;
	}

	@Override
	public Object forSetting( FSetting rs, Object p) {
		++mResult;
		return null;
	}
}
