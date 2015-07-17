package de.doerl.hqm.utils.json;

public final class FValue implements IJson {
	private Object mObj;

	FValue( Object obj) {
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

	public static boolean toBoolean( IJson json) {
		Object obj = toObject( json);
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		}
		else {
			return false;
		}
	}

	public static byte toByte( IJson json) {
		return toByte( json, (byte) 0);
	}

	public static byte toByte( IJson json, byte def) {
		Long obj = toLongObj( json);
		if (obj != null) {
			return obj.byteValue();
		}
		else {
			return def;
		}
	}

	public static double toDouble( IJson json) {
		return toDouble( json, 0);
	}

	public static double toDouble( IJson json, double def) {
		Double obj = toDoubleObj( json);
		if (obj != null) {
			return obj.doubleValue();
		}
		else {
			return def;
		}
	}

	public static Double toDoubleObj( IJson json) {
		Object obj = toObject( json);
		if (obj instanceof Double) {
			return (Double) obj;
		}
		else {
			return null;
		}
	}

	public static float toFloat( IJson json) {
		return toFloat( json, 0);
	}

	public static float toFloat( IJson json, float def) {
		Double obj = toDoubleObj( json);
		if (obj != null) {
			return obj.floatValue();
		}
		else {
			return def;
		}
	}

	public static int toInt( IJson json) {
		return toInt( json, 0);
	}

	public static int toInt( IJson json, int def) {
		Long obj = toLongObj( json);
		if (obj != null) {
			return obj.intValue();
		}
		else {
			return def;
		}
	}

	public static Integer toIntObj( IJson json) {
		Long obj = toLongObj( json);
		if (obj != null) {
			return obj.intValue();
		}
		else {
			return null;
		}
	}

	public static long toLong( IJson json) {
		return toLong( json, 0L);
	}

	public static long toLong( IJson json, long def) {
		Long obj = toLongObj( json);
		if (obj != null) {
			return obj.longValue();
		}
		else {
			return def;
		}
	}

	public static Long toLongObj( IJson json) {
		Object obj = toObject( json);
		if (obj instanceof Long) {
			return (Long) obj;
		}
		else {
			return null;
		}
	}

	public static Object toObject( IJson json) {
		if (json instanceof FValue) {
			return ((FValue) json).mObj;
		}
		else {
			return null;
		}
	}

	public static short toShort( IJson json) {
		return toShort( json, (short) 0);
	}

	public static short toShort( IJson json, short def) {
		Long obj = toLongObj( json);
		if (obj != null) {
			return obj.shortValue();
		}
		else {
			return def;
		}
	}

	public static String toString( IJson json) {
		Object obj = toObject( json);
		if (obj != null) {
			return String.valueOf( obj);
		}
		else {
			return null;
		}
	}

	public static String toString( IJson json, String def) {
		Object obj = toObject( json);
		if (obj != null) {
			return String.valueOf( obj);
		}
		else {
			return def;
		}
	}

	@Override
	public String toString() {
		return String.valueOf( mObj);
	}
}
