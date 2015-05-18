package de.doerl.hqm.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

public class PreferenceManager {
	private static final Logger LOGGER = Logger.getLogger( PreferenceManager.class.getName());
	private static boolean sFireEvents = true;
	private static boolean sIsNotInit = true;
	private static EventListenerList sListener = new EventListenerList();
	private static final File SOURCE = new File( SystemInfo.HQM_DIR, "hqm.pref");
	private static PreferenceHash sPrefs = new PreferenceHash();

	private PreferenceManager() {
	}

	public static void addArrayString( String key, int pos, String value) {
		sPrefs.addArrayString( key, pos, value);
		firePreferenceChanged( key);
	}

	public static void addArrayString( String key, String value) {
		sPrefs.addArrayString( key, value);
		firePreferenceChanged( key);
	}

	static void clear() {
		sPrefs.clear();
	}

	public static boolean containsArrayString( String key, String value) {
		return sPrefs.containsArrayString( key, value);
	}

	public static void decInteger( String key) {
		sPrefs.decInteger( key);
		firePreferenceChanged( key);
	}

	public static void deleteArrayString( String key, int nr) {
		sPrefs.deleteArrayString( key, nr);
		firePreferenceChanged( key);
	}

	public static void deleteArrayString( String key, String value) {
		sPrefs.deleteArrayString( key, value);
		firePreferenceChanged( key);
	}

	public static void firePreferenceChanged( String key) {
		if (sFireEvents) {
			Object[] listeners = sListener.getListenerList();
			PreferenceEvent event = new PreferenceEvent( PreferenceManager.class, key);
			for (int i = 0; i < listeners.length; i += 2) {
				if (listeners[i] == IPreferenceChangeListener.class) {
					try {
						((IPreferenceChangeListener) listeners[i + 1]).preferenceChanged( event);
					}
					catch (ClassCastException ex) {
						Utils.logThrows( LOGGER, Level.WARNING, ex);
					}
				}
			}
		}
	}

	public static String[] getArray( String key) {
		return sPrefs.getArray( key);
	}

	public static String[] getArray( String key, String[] def) {
		return sPrefs.getArray( key, def);
	}

	public static int getArrayCount( String key) {
		return sPrefs.getArrayCount( key);
	}

	public static String getArrayString( String key, int nr) {
		return sPrefs.getArrayString( key, nr);
	}

	public static boolean getBool( String key) {
		return sPrefs.getBool( key, false);
	}

	public static boolean getBool( String key, boolean def) {
		return sPrefs.getBool( key, def);
	}

	public static Boolean getBoolean( String key) {
		return sPrefs.getBoolean( key, Boolean.FALSE);
	}

	public static Boolean getBoolean( String key, Boolean def) {
		return sPrefs.getBoolean( key, def);
	}

	public static Color getColor( String key) {
		return sPrefs.getColor( key, null);
	}

	public static Color getColor( String key, Color def) {
		return sPrefs.getColor( key, def);
	}

	public static String[] getDefaultArray( String key) {
//		try {
//			for (int i = 0; i < sHandler.size(); ++i) {
//				APreferenceHandler ape = sHandler.get( i);
//				Object obj = ape.getDefault( key);
//				if (obj != null) {
//					return (String[]) obj;
//				}
//			}
//		}
//		catch (RuntimeException ex) {
//		}
		return new String[0];
	}

	public static boolean getDefaultBoolean( String key) {
//		try {
//			for (int i = 0; i < sHandler.size(); ++i) {
//				APreferenceHandler ape = sHandler.get( i);
//				Object obj = ape.getDefault( key);
//				if (obj != null) {
//					return ((Boolean) obj).booleanValue();
//				}
//			}
//		}
//		catch (RuntimeException ex) {
//		}
		return false;
	}

	public static Color getDefaultColor( String key) {
//		try {
//			for (int i = 0; i < sHandler.size(); ++i) {
//				APreferenceHandler ape = sHandler.get( i);
//				Object obj = ape.getDefault( key);
//				if (obj != null) {
//					return (Color) obj;
//				}
//			}
//		}
//		catch (RuntimeException ex) {
//		}
		return Color.BLACK;
	}

	public static int getDefaultInteger( String key) {
//		try {
//			for (int i = 0; i < sHandler.size(); ++i) {
//				APreferenceHandler ape = sHandler.get( i);
//				Object obj = ape.getDefault( key);
//				if (obj != null) {
//					return ((Integer) obj).intValue();
//				}
//			}
//		}
//		catch (RuntimeException ex) {
//		}
		return 0;
	}

	public static Level getDefaultLevel( String key) {
//		try {
//			for (int i = 0; i < sHandler.size(); ++i) {
//				APreferenceHandler ape = sHandler.get( i);
//				Object obj = ape.getDefault( key);
//				if (obj != null) {
//					return (Level) obj;
//				}
//			}
//		}
//		catch (RuntimeException ex) {
//		}
		return Level.OFF;
	}

	public static String getDefaultString( String key) {
//		try {
//			for (int i = 0; i < sHandler.size(); ++i) {
//				APreferenceHandler ape = sHandler.get( i);
//				Object obj = ape.getDefault( key);
//				if (obj != null) {
//					return obj.toString();
//				}
//			}
//		}
//		catch (RuntimeException ex) {
//		}
		return null;
	}

	public static int getInt( String key) {
		return sPrefs.getInt( key, 0);
	}

	public static int getInt( String key, int def) {
		return sPrefs.getInt( key, def);
	}

	public static Integer getInteger( String key) {
		return sPrefs.getInteger( key, null);
	}

	public static Integer getInteger( String key, Integer def) {
		return sPrefs.getInteger( key, def);
	}

	public static Object getObject( String key) {
		return sPrefs.getObject( key);
	}

	public static String getString( String key) {
		return sPrefs.getString( key, null);
	}

	public static String getString( String key, String def) {
		return sPrefs.getString( key, def);
	}

	static PreferenceObject getValue( String key) {
		return sPrefs.getValue( key);
	}

	public static void incInteger( String key) {
		sPrefs.incInteger( key);
		firePreferenceChanged( key);
	}

	public static void init() {
		synchronized (PreferenceManager.class) {
			if (sIsNotInit) {
				sIsNotInit = false;
				if (SOURCE.exists()) {
					try {
						loadAll();
						initAll( false);
					}
					catch (IOException ex) {
						Utils.logThrows( LOGGER, Level.WARNING, ex);
						initAll( true);
					}
				}
				else {
					initAll( true);
//					PreferenceSaver.event();
				}
				Runtime.getRuntime().addShutdownHook( new Thread() {
					@Override
					public void run() {
						savePreferences();
					}
				});
//				LoggingChangeListener.init();
//				PreferenceSaver.init();
				Utils.setStackTrace( getBool( BaseDefaults.STACKTRC));
			}
		}
	}

	private static void initAll( boolean load) {
		synchronized (sPrefs) {
			BaseDefaults handler = new BaseDefaults();
			if (load) {
				handler.load( sPrefs);
			}
			handler.check( sPrefs);
			handler.keys( sPrefs);
		}
	}

	public static Set<String> keySet() {
		return sPrefs.keySet();
	}

	private static void loadAll() throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream( SOURCE);
			sPrefs.load( in);
		}
		finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public static void registerChangeListener( IPreferenceChangeListener l) {
		sListener.add( IPreferenceChangeListener.class, new WeakChangeListener( l));
	}

	private static void saveAll() {
		File oldFile = null;
		if (SOURCE.exists()) {
			oldFile = new File( SOURCE.getAbsolutePath() + ".old");
			if (!SOURCE.renameTo( oldFile)) {
				Utils.log( LOGGER, Level.SEVERE, "error.preference.rename {0}", SOURCE.getName());
				return;
			}
		}
		try {
			FileOutputStream out = null;
			try {
				out = new FileOutputStream( SOURCE);
				BaseDefaults.save( sPrefs, out);
			}
			finally {
				if (out != null) {
					out.close();
				}
			}
			if (oldFile != null) {
				oldFile.delete();
			}
//			System.out.println("save Properties for " + class.getName());
		}
		catch (Exception ex) {
			if (oldFile != null) {
				oldFile.renameTo( SOURCE);
			}
			Utils.log( LOGGER, Level.WARNING, "error.preference.save {0} {1}", SOURCE.getName(), ex.getMessage());
		}
	}

	static void savePreferences() {
		synchronized (sPrefs) {
			saveAll();
		}
	}

	public static void setArray( String key, String[] val) {
		sPrefs.setArray( key, val);
		firePreferenceChanged( key);
	}

	public static void setArrayString( String key, int nr, String val) {
		sPrefs.setArrayString( key, nr, val);
		firePreferenceChanged( key);
	}

	public static void setBool( String key, boolean val) {
		if (sPrefs.setBool( key, val)) {
			firePreferenceChanged( key);
		}
	}

	public static void setBoolean( String key, Boolean val) {
		if (sPrefs.setBoolean( key, val)) {
			firePreferenceChanged( key);
		}
	}

	public static void setColor( String key, Color val) {
		if (sPrefs.setColor( key, val)) {
			firePreferenceChanged( key);
		}
	}

	public static void setFireEvents( boolean enable) {
		sFireEvents = enable;
	}

	public static void setInt( String key, int val) {
		if (sPrefs.setInt( key, val)) {
			firePreferenceChanged( key);
		}
	}

	public static void setInteger( String key, Integer val) {
		if (sPrefs.setInteger( key, val)) {
			firePreferenceChanged( key);
		}
	}

	public static void setObject( String key, Object val) {
		sPrefs.setObject( key, val);
	}

	public static void setString( String key, String val) {
		if (sPrefs.setString( key, val)) {
			firePreferenceChanged( key);
		}
	}

	private static class WeakChangeListener extends WeakReference<IPreferenceChangeListener> implements IPreferenceChangeListener {
		public WeakChangeListener( IPreferenceChangeListener l) {
			super( l);
		}

		public WeakChangeListener( IPreferenceChangeListener l, ReferenceQueue<IPreferenceChangeListener> q) {
			super( l, q);
		}

		public void preferenceChanged( PreferenceEvent event) {
			IPreferenceChangeListener obj = get();
			if (obj != null) {
				obj.preferenceChanged( event);
			}
			else {
				sListener.remove( IPreferenceChangeListener.class, this);
			}
		}
	}
}
