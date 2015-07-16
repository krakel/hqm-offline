package de.doerl.hqm.base;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IDispatcher;
import de.doerl.hqm.base.dispatch.IHQMWorker;
import de.doerl.hqm.quest.ElementTyp;
import de.doerl.hqm.ui.LinkType;
import de.doerl.hqm.utils.ToString;
import de.doerl.hqm.utils.Utils;

public abstract class ABase implements IDispatcher {
	private static final Logger LOGGER = Logger.getLogger( ABase.class.getName());

	ABase() {
	}

	static int fromIdent( String base, String ident) {
		if (ident == null) {
			return -1;
		}
		else {
			int pos = ident.indexOf( " - ");
			if (pos > 0) {
				return Utils.parseInteger( ident.substring( 0, pos), -1);
			}
			else if (ident.startsWith( base)) {
				return Utils.parseInteger( ident.substring( base.length()), -1);
			}
			else {
				Utils.log( LOGGER, Level.WARNING, "wrong ident {0}", ident);
				return -1;
			}
		}
	}

	static <T extends ABase> boolean isFirst( Vector<T> arr, T val) {
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

	static <T extends ABase> boolean isLast( Vector<T> arr, T val) {
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

	static <T extends ABase> void moveDown( Vector<T> arr, T val) {
		int pos = arr.indexOf( val);
		if (pos >= 0) {
			int size = arr.size();
			int next = pos;
			while (++next < size) {
				T old = arr.get( next);
				if (old != null) {
					arr.setElementAt( old, pos);
					arr.setElementAt( val, next);
					break;
				}
			}
		}
	}

	static <T extends ABase> void moveUp( Vector<T> arr, T val) {
		int pos = arr.indexOf( val);
		if (pos >= 0) {
			int prev = pos;
			while (--prev >= 0) {
				T old = arr.get( prev);
				if (old != null) {
					arr.setElementAt( old, pos);
					arr.setElementAt( val, prev);
					break;
				}
			}
		}
	}

	static <T extends ABase> void remove( Vector<T> arr, T val) {
		int pos = arr.indexOf( val);
		if (pos >= 0) {
			arr.setElementAt( null, pos);
		}
	}

	static String toIdent( String base, int idx) {
		return String.format( "%s%03d", base, idx);
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
