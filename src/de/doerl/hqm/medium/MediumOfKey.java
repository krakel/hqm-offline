package de.doerl.hqm.medium;

import de.doerl.hqm.utils.Utils;

public class MediumOfKey implements IMediumWorker<IMedium, String> {
	private static final MediumOfKey WORKER = new MediumOfKey();

	private MediumOfKey() {
	}

	public static IMedium get( String key) {
		return MediaManager.forEachMedium( WORKER, key);
	}

	public IMedium forMedium( IMedium m, String key) {
		return Utils.equals( m.getName(), key) ? m : null;
	}
}
