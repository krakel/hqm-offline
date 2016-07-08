package de.doerl.hqm.base;

import java.util.ArrayList;

import de.doerl.hqm.base.dispatch.IDispatcher;
import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.ui.LinkType;
import de.doerl.hqm.utils.ToString;

public abstract class ABase implements IDispatcher {
	ABase() {
	}

	static <T extends ABase> boolean isFirst( ArrayList<T> arr, T val) {
		int pos = arr.indexOf( val);
		if (pos < 0) {
			return false;
		}
		while (--pos >= 0) {
			if (arr.get( pos) != null) {
				return false;
			}
		}
		return true;
	}

	static <T extends ABase> boolean isLast( ArrayList<T> arr, T val) {
		int pos = arr.indexOf( val);
		if (pos < 0) {
			return false;
		}
		int size = arr.size();
		while (++pos < size) {
			if (arr.get( pos) != null) {
				return false;
			}
		}
		return true;
	}

	static <T extends ABase> void moveDown( ArrayList<T> arr, T val) {
		int pos = arr.indexOf( val);
		if (pos >= 0) {
			int size = arr.size();
			int next = pos;
			while (++next < size) {
				T old = arr.get( next);
				if (old != null) {
					arr.set( pos, old);
					arr.set( next, val);
					break;
				}
			}
		}
	}

	static <T extends ABase> void moveUp( ArrayList<T> arr, T val) {
		int pos = arr.indexOf( val);
		if (pos >= 0) {
			int prev = pos;
			while (--prev >= 0) {
				T old = arr.get( prev);
				if (old != null) {
					arr.set( pos, old);
					arr.set( prev, val);
					break;
				}
			}
		}
	}

	static <T extends ABase> void remove( ArrayList<T> arr, T val) {
		int pos = arr.indexOf( val);
		if (pos >= 0) {
			arr.set( pos, null);
		}
	}

	public abstract <T, U> T accept( IHQMWorker<T, U> w, U p);

	public abstract ElementTyp getElementTyp();

	public abstract FHqm getHqm();

	public LinkType getInformation() {
		return LinkType.NORM;
	}

	public abstract ABase getParent();

	public void setInformation( LinkType information) {
	}

	@Override
	public String toString() {
		return ToString.clsName( this);
	}
}
