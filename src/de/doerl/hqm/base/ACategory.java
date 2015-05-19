package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.utils.Utils;

public abstract class ACategory<E extends ANamed> extends ABase {
	private static final Logger LOGGER = Logger.getLogger( ACategory.class.getName());
	public final FHqm mParentHQM;
	private Vector<E> mArr = new Vector<E>();

	ACategory( FHqm parent) {
		mParentHQM = parent;
	}

	void addMember( E member) {
		mArr.add( member);
	}

	public abstract E createMember( String name);

	public <T, U> T forEachMember( IHQMWorker<T, U> worker, U p) {
		for (E disp : mArr) {
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
	public FHqm getParent() {
		return mParentHQM;
	}

	void removeMember( AMember<?> member) {
		int pos = mArr.indexOf( member);
		if (pos >= 0) {
			mArr.setElementAt( null, pos);
		}
	}
}
