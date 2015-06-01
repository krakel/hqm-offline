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
		Integer obj = toIntObj( json);
		if (obj != null) {
			return obj.byteValue();
		}
		else {
			return 0;
		}
	}

	public static double toDouble( IJson json) {
		Double obj = toDoubleObj( json);
		if (obj != null) {
			return obj.doubleValue();
		}
		else {
			return 0;
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
		Double obj = toDoubleObj( json);
		if (obj != null) {
			return obj.floatValue();
		}
		else {
			return 0;
		}
	}

	public static int toInt( IJson json) {
		Integer obj = toIntObj( json);
		if (obj != null) {
			return obj.intValue();
		}
		else {
			return 0;
		}
	}

	public static Integer toIntObj( IJson json) {
		Object obj = toObject( json);
		if (obj instanceof Integer) {
			return (Integer) obj;
		}
		else {
			return null;
		}
	}

	public static long toLong( IJson json) {
		Long obj = toLongObj( json);
		if (obj != null) {
			return obj.longValue();
		}
		else {
			return 0;
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
		Integer obj = toIntObj( json);
		if (obj != null) {
			return obj.shortValue();
		}
		else {
			return 0;
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

	@Override
	public String toString() {
		return String.valueOf( mObj);
	}
}
