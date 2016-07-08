package de.doerl.hqm.base.data;

import java.util.ArrayList;

import de.doerl.hqm.base.dispatch.IDataWorker;
import de.doerl.hqm.base.dispatch.IDispatcher;
import de.doerl.hqm.utils.ToString;

public abstract class AGame implements IDispatcher {
	AGame() {
	}

	static <T extends AGame> void remove( ArrayList<T> arr, T val) {
		int pos = arr.indexOf( val);
		if (pos >= 0) {
			arr.set( pos, null);
		}
	}

	public abstract <T, U> T accept( IDataWorker<T, U> w, U p);

	public abstract FData getData();

	public abstract AGame getParent();

	@Override
	public String toString() {
		return ToString.clsName( this);
	}
}
