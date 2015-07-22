package de.doerl.hqm.base.dispatch;

import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FQuestTaskMob;

public class MobOfID extends AHQMWorker<FMob, Integer> {
	private static final MobOfID WORKER = new MobOfID();

	private MobOfID() {
	}

	public static FMob get( FQuestTaskMob task, int id) {
		return task.forEachMob( WORKER, id);
	}

	public static FMob get( FQuestTaskMob task, String ident) {
		return task.forEachMob( WORKER, FMob.fromIdent( ident));
	}

	@Override
	public FMob forMob( FMob mob, Integer id) {
		return mob.getID() == id.intValue() ? mob : null;
	}
}
