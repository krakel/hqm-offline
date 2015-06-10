package de.doerl.hqm.utils.nei;

import de.doerl.hqm.utils.ToString;
import de.doerl.hqm.utils.Utils;

public final class FValue implements IJson {
	private String mObj;

	FValue( String obj) {
		mObj = obj;
	}

	public static FValue to( IJson json) {
		if (json instanceof FValue) {
			return (FValue) json;
		}
		else {
			return null;
		}
	}

	@Override
	public boolean equals( Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		try {
			FValue other = (FValue) obj;
			if (Utils.different( mObj, other.mObj)) {
				return false;
			}
		}
		catch (ClassCastException ex) {
			return false;
		}
		return true;
	}

	public int toByte() {
		if (mObj != null && mObj.endsWith( "b")) {
			return Utils.parseInteger( mObj.substring( 0, mObj.length() - 1), 0);
		}
		else {
			return 0;
		}
	}

	public double toDouble() {
		if (mObj != null && mObj.endsWith( "d")) {
			return Utils.parseDouble( mObj.substring( 0, mObj.length() - 1), 0);
		}
		else {
			return 0;
		}
	}

	public float toFloat() {
		if (mObj != null && mObj.endsWith( "f")) {
			return Utils.parseFloat( mObj.substring( 0, mObj.length() - 1), 0);
		}
		else {
			return 0;
		}
	}

	public int toInt() {
		if (mObj != null) {
			return Utils.parseInteger( mObj, 0);
		}
		else {
			return 0;
		}
	}

	public long toLong() {
		if (mObj != null && mObj.endsWith( "L")) {
			return Utils.parseLong( mObj.substring( 0, mObj.length() - 1), 0);
		}
		else {
			return 0;
		}
	}

	public String toObject() {
		if (mObj != null && mObj.endsWith( "\"")) {
			return mObj.substring( 1, mObj.length() - 1);
		}
		else {
			return "";
		}
	}

	public int toShort() {
		if (mObj != null && mObj.endsWith( "s")) {
			return Utils.parseInteger( mObj.substring( 0, mObj.length() - 1), 0);
		}
		else {
			return 0;
		}
	}

	@Override
	public String toString() {
		ToString sb = new ToString( this);
		sb.appendMsg( "obj", mObj);
		return sb.toString();
	}
}
