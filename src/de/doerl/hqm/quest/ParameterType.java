package de.doerl.hqm.quest;

public enum ParameterType {
	PARAMETER_SETS,
	PARAMETER_BOOLEAN,
	PARAMETER_INTEGER,
	PARAMETER_INT,
	PARAMETER_INT_ARR,
	PARAMETER_STACK,
	PARAMETER_STRING;
	public static ParameterType get( int idx) {
		ParameterType[] values = values();
		if (idx < 0) {
			return values[0];
		}
		if (idx >= values.length) {
			return values[values.length - 1];
		}
		return values[idx];
	}
}
