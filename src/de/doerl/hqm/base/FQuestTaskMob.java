package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.utils.Utils;

public final class FQuestTaskMob extends AQuestTask {
	private static final Logger LOGGER = Logger.getLogger( FQuestTaskMob.class.getName());
	private Vector<FMob> mMobs = new Vector<FMob>();

	public FQuestTaskMob( FQuest parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forTaskMob( this, p);
	}

	public FMob createMob( FItemStack icon, String name) {
		FMob res = new FMob( this, icon, name);
		mMobs.add( res);
		return res;
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
				Utils.logThrows( LOGGER, Level.WARNING, "Common.error", ex);
			}
		}
		return null;
	}

	@Override
	public ElementTyp getElementTyp() {
		return ElementTyp.QUEST_TASK_MOB;
	}
}
