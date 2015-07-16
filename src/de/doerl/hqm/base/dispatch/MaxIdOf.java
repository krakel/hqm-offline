package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FQuest;

public class MaxIdOf extends AHQMWorker<Object, Object> {
	private int mResult = -1;

	private MaxIdOf() {
	}

	public static int getTasks( FQuest cat) {
		MaxIdOf worker = new MaxIdOf();
		cat.forEachTask( worker, null);
		return worker.mResult;
	}

	@Override
	protected Object doTask( AQuestTask task, Object p) {
		mResult = Math.max( mResult, task.getID());
		return null;
	}
}
