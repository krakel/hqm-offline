package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationCat;

public class MaxIdOf extends AHQMWorker<Object, Object> {
	private int mResult = -1;

	private MaxIdOf() {
	}

	public static int getMarker( FReputation rep) {
		MaxIdOf worker = new MaxIdOf();
		rep.forEachMarker( worker, null);
		return worker.mResult;
	}

	public static int getQuestSet( FQuestSetCat cat) {
		MaxIdOf worker = new MaxIdOf();
		cat.forEachMember( worker, null);
		return worker.mResult;
	}

	public static int getReputation( FReputationCat cat) {
		MaxIdOf worker = new MaxIdOf();
		cat.forEachMember( worker, null);
		return worker.mResult;
	}

	public static int getTasks( FQuest quest) {
		MaxIdOf worker = new MaxIdOf();
		quest.forEachTask( worker, null);
		return worker.mResult;
	}

	public static int getTier( FGroupTierCat cat) {
		MaxIdOf worker = new MaxIdOf();
		cat.forEachMember( worker, null);
		return worker.mResult;
	}

	@Override
	protected Object doTask( AQuestTask task, Object p) {
		mResult = Math.max( mResult, task.getID());
		return null;
	}

	@Override
	public Object forGroupTier( FGroupTier tier, Object p) {
		mResult = Math.max( mResult, tier.getID());
		return null;
	}

	@Override
	public Object forMarker( FMarker mark, Object p) {
		mResult = Math.max( mResult, mark.getID());
		return null;
	}

	@Override
	public Object forQuestSet( FQuestSet set, Object p) {
		mResult = Math.max( mResult, set.getID());
		return null;
	}

	@Override
	public Object forReputation( FReputation rep, Object p) {
		mResult = Math.max( mResult, rep.getID());
		return null;
	}
}
