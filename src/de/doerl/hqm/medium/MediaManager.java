package de.doerl.hqm.medium;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.utils.Utils;

public class MediaManager {
	private static final Logger LOGGER = Logger.getLogger( MediaManager.class.getName());
	public static final String DEFAULT_MEDIUM = System.getProperty( "hqm.medium.default", "hqm");
	public static final String ACTIV_MEDIUM = "activ_medium";
	public static final String ACTIV_PATH = "activ_path";
	private static Vector<IMedium> sMedia;
	static {
		sMedia = new Vector<IMedium>();
		registerMedium( new de.doerl.hqm.medium.bits.Medium());
		registerMedium( new de.doerl.hqm.medium.json.Medium());
	}

	private MediaManager() {
	}

	public static <T, U> T forEachMedium( IMediumWorker<T, U> worker, U p) {
		for (IMedium disp : sMedia) {
			try {
				if (disp != null) {
					T obj = disp.accept( worker, p);
					if (obj != null) {
						return obj;
					}
				}
			}
			catch (RuntimeException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return null;
	}

	public static IMedium get( String key) {
		return MediumOfKey.get( key);
	}

	public static IMedium getMaster() {
		return get( DEFAULT_MEDIUM);
	}

	public static Object getProperty( FHqm base, String key) {
		return WeakProperty.get( base).get( key);
	}

	public static Object getProperty( FHqm base, String key, Object def) {
		Object res = WeakProperty.get( base).get( key);
		return res != null ? res : def;
	}

	public static boolean isFALSE( FHqm base, String key) {
		Object prop = getProperty( base, key);
		return prop == null || Boolean.FALSE.equals( prop);
	}

	public static boolean isProperty( FHqm base, String key) {
		return WeakProperty.get( base).get( key) != null;
	}

	public static boolean isTRUE( FHqm base, String key) {
		Object prop = getProperty( base, key);
		return prop != null && Boolean.TRUE.equals( prop);
	}

	public static String parse( String name) {
		return null;
	}

	public static void registerMedium( IMedium medium) {
		if (medium != null && !sMedia.contains( medium)) {
			sMedia.add( medium);
		}
	}

	public static void setProperty( FHqm base, String key, Object value) {
		WeakProperty.get( base).set( key, value);
	}
}
