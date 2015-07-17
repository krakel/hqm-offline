package de.doerl.hqm.base;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.Utils;

public abstract class AIdented extends ANamed {
	private static final Logger LOGGER = Logger.getLogger( AIdented.class.getName());
	private String mBase;
	private int mID;

	public AIdented( String base, int id) {
		mBase = base;
		mID = id;
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

	static String toIdent( String base, int idx) {
		return String.format( "%s%03d", base, idx);
	}

	public int fromIdentX( String ident) {
		if (ident == null) {
			return -1;
		}
		else {
			int pos = ident.indexOf( " - ");
			if (pos > 0) {
				return Utils.parseInteger( ident.substring( 0, pos), -1);
			}
			else if (ident.startsWith( mBase)) {
				return Utils.parseInteger( ident.substring( mBase.length()), -1);
			}
			else {
				Utils.log( LOGGER, Level.WARNING, "wrong ident {0}", ident);
				return -1;
			}
		}
	}

	public int getID() {
		return mID;
	}

	public void setID( String ident) {
		if (ident != null) {
			int id = fromIdent( mBase, ident);
			if (id >= 0) {
				mID = id;
			}
		}
	}

	public void setIDObj( Object obj) {
		if (obj instanceof Number) {
			mID = ((Number) obj).intValue();
		}
		else {
			setID( String.valueOf( obj));
		}
	}

	public String toIdent() {
		return String.format( "%s%03d", mBase, mID);
	}

	public String toIdentX( int idx) {
		return String.format( "%s%03d", mBase, idx);
	}
}
