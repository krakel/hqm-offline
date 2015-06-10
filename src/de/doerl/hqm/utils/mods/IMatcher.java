package de.doerl.hqm.utils.mods;

import java.util.ArrayList;

public interface IMatcher {
	void addNBT( String nbt, String file);

	String findFile( String nbt);

	void findMatch( ArrayList<SimpleMatcher> arr, String value);

	String getStk();
}
