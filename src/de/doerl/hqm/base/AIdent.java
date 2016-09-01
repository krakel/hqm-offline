package de.doerl.hqm.base;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.Utils;

public abstract class AIdent extends ANamed {
	private static final Logger LOGGER = Logger.getLogger( AIdent.class.getName());
	private String mBase;
	private int mID;
	private String mUUID = UUID.randomUUID().toString();

	AIdent( String base, int id) {
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
				Utils.log( LOGGER, Level.WARNING, "wrong ident {0} of {1}", ident, base);
				return -1;
			}
		}
	}

	static String toIdent( String base, int idx) {
		return String.format( "%s%03d", base, idx);
	}

	public int getID() {
		return mID;
	}

	public String getUUID() {
		return mUUID;
	}

	public void setID( String ident) {
		if (ident != null) {
			int id = fromIdent( mBase, ident);
			if (id >= 0) {
				mID = id;
			}
		}
	}

	public void setUUID( String uuid) {
		mUUID = uuid;
	}

	public String toIdent() {
		return String.format( "%s%03d", mBase, mID);
	}
}
