package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.utils.Utils;

public final class FQuestSet extends AMember<FQuestSet> {
	private static final Logger LOGGER = Logger.getLogger( FQuestSet.class.getName());
	public final FParameterString mDesc = new FParameterString( this, "Description");
	private Vector<FQuest> mMember = new Vector<FQuest>();

	public FQuestSet( ASet<FQuestSet> parent, String name) {
		super( parent, name);
	}

	@Override
	public <T, U> T accept( IHQMWorker<T, U> w, U p) {
		return w.forQuestSet( this, p);
	}

	void addQuest( FQuest member) {
		mMember.add( member);
	}

	public FQuest createQuest( String name) {
		FQuest reward = new FQuest( this, name);
		addQuest( reward);
		return reward;
	}

	public <T, U> T forEachQuest( IHQMWorker<T, U> worker, U p) {
		for (FQuest disp : mMember) {
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
		return ElementTyp.QUEST_SET;
	}
}
