package de.doerl.hqm.utils;

import java.util.EventListener;

public interface IPreferenceChangeListener extends EventListener {
	void preferenceChanged( PreferenceEvent event);
}
