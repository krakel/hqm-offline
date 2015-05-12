package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.utils.Utils;

public abstract class ASet<E extends ANamed> extends ABase {
	private static final Logger LOGGER = Logger.getLogger( ASet.class.getName());
	public final FHqm mParentHQM;
	private Vector<E> mMember = new Vector<E>();

	ASet( FHqm parent) {
		mParentHQM = parent;
	}

	void addMember( E member) {
		mMember.add( member);
	}

	public abstract E createMember( String name);

	public <T, U> T forEachMember( IHQMWorker<T, U> worker, U p) {
		for (E disp : mMember) {
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
	public FHqm getParent() {
		return mParentHQM;
	}
}
