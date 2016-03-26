package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.AMember;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.AQuestTaskReputation;
import de.doerl.hqm.base.ARequirement;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationBar;
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.FSetting;

public class SizeOf extends AHQMWorker<Object, Object> {
	private int mResult = 0;

	private SizeOf() {
	}

	public static int getBars( FQuestSet set) {
		SizeOf worker = new SizeOf();
		set.forEachBar( worker, null);
		return worker.mResult;
	}

	public static int getLocations( FQuestTaskLocation task) {
		SizeOf worker = new SizeOf();
		task.forEachLocation( worker, null);
		return worker.mResult;
	}

	public static int getMarker( FReputation rep) {
		SizeOf worker = new SizeOf();
		rep.forEachMarker( worker, null);
		return worker.mResult;
	}

	public static int getMember( ACategory<?> cat) {
		SizeOf worker = new SizeOf();
		cat.forEachMember( worker, null);
		return worker.mResult;
	}

	public static int getMobs( FQuestTaskMob task) {
		SizeOf worker = new SizeOf();
		task.forEachMob( worker, null);
		return worker.mResult;
	}

	public static int getRequirements( AQuestTaskItems task) {
		SizeOf worker = new SizeOf();
		task.forEachRequirement( worker, null);
		return worker.mResult;
	}

	public static int getReward( FQuest quest) {
		SizeOf worker = new SizeOf();
		quest.forEachReward( worker, null);
		return worker.mResult;
	}

	public static int getSettings( AQuestTaskReputation task) {
		SizeOf worker = new SizeOf();
		task.forEachSetting( worker, null);
		return worker.mResult;
	}

	public static int getTasks( FQuest quest) {
		SizeOf worker = new SizeOf();
		quest.forEachTask( worker, null);
		return worker.mResult;
	}

	@Override
	protected Object doMember( AMember member, Object p) {
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
	public Object forReputationBar( FReputationBar bar, Object p) {
		++mResult;
		return null;
	}

	@Override
	public Object forReputationReward( FReputationReward rr, Object p) {
		++mResult;
		return null;
	}

	@Override
	public Object forSetting( FSetting rs, Object p) {
		++mResult;
		return null;
	}
}
