package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.utils.Utils;

public final class FQuestTaskMob extends AQuestTask {
	private static final Logger LOGGER = Logger.getLogger( FQuestTaskMob.class.getName());
	public final Vector<FMob> mMobs = new Vector<>();

	FQuestTaskMob( FQuest parent) {
		super( parent);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskMob( this, p);
	}

	public FMob createMob() {
		FMob mob = new FMob( this);
		mMobs.add( mob);
		return mob;
	}

	public FMob createMob( int id) {
		FMob mob = new FMob( this, id);
		mMobs.add( mob);
		return mob;
	}

	public <T, U> T forEachMob( IHQMWorker<T, U> worker, U p) {
		for (FMob disp : mMobs) {
			try {
				if (disp != null) {
					T obj = disp.accept( worker, p);
					if (obj != null) {
						return obj;
					}
				}
			}
			catch (RuntimeException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return null;
	}

	@Override
	public TaskTyp getTaskTyp() {
		return TaskTyp.TASK_MOB;
	}
}
