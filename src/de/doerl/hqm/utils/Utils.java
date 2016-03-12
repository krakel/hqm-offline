package de.doerl.hqm.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Utils {
	private static final boolean INNER = false;
	private static final String SELF = Utils.class.getName();
	private static final Logger SELF_LOGGER = Logger.getLogger( SELF);
	public static String sSessionId;
	private static boolean sTraceStack;

	private static void appentThree( StringBuffer sb, int x) {
		if (x < 100) {
			sb.append( '0');
		}
		if (x < 10) {
			sb.append( '0');
		}
		sb.append( x);
	}

	private static void appentTwo( StringBuffer sb, int x) {
		if (x < 10) {
			sb.append( '0');
		}
		sb.append( x);
	}

	public static void assertEquals( Logger logger, String msg, boolean expected, boolean actual) {
		if (expected != actual) {
			log( logger, Level.WARNING, "Utils.assertEquals {0} {1} {2}", msg, new Boolean( expected), new Boolean( actual));
		}
	}

	public static void assertEquals( Logger logger, String msg, int expected, int actual) {
		if (expected != actual) {
			log( logger, Level.WARNING, "Utils.assertEquals {0} {1} {2}", msg, new Integer( expected), new Integer( actual));
		}
	}

	public static void assertEquals( Logger logger, String msg, long expected, long actual) {
		if (expected != actual) {
			log( logger, Level.WARNING, "Utils.assertEquals {0} {1} {2}", msg, new Long( expected), new Long( actual));
		}
	}

	public static void assertEquals( Logger logger, String msg, Object expected, Object actual) {
		if (different( expected, actual)) {
			log( logger, Level.WARNING, "Utils.assertEquals {0} {1} {2}", msg, expected, actual);
		}
	}

	public static void assertEquals( Logger logger, String msg, short expected, short actual) {
		if (expected != actual) {
			log( logger, Level.WARNING, "Utils.assertEquals {0} {1} {2}", msg, new Short( expected), new Short( actual));
		}
	}

	public static void assertNotNull( Logger logger, String msg, Object actual) {
		if (actual == null) {
			log( logger, Level.WARNING, "Utils.assertNotNull {0}", msg);
		}
	}

	public static void assertNull( Logger logger, String msg, Object actual) {
		if (actual != null) {
			log( logger, Level.WARNING, "Utils.assertNull {0} {1}", msg, actual);
		}
	}

	public static void centerFrame( JFrame frame) {
		try {
			frame.setLocationRelativeTo( null);
		}
		catch (NoSuchMethodError e) {
			setLocationRelativeTo( null, frame);
		}
	}

	public static void checkIntegerProperty( String name, int min, int max) {
		Integer i = Integer.getInteger( name, null);
		if (i != null) {
			if (i.intValue() < min) {
				log( SELF_LOGGER, Level.WARNING, "Utils.integer.min");
				System.setProperty( name, Integer.toString( min));
			}
			else if (i.intValue() > max) {
				log( SELF_LOGGER, Level.WARNING, "Utils.integer.max");
				System.setProperty( name, Integer.toString( max));
			}
		}
	}

	public static void closeIgnore( Closeable stream) {
		try {
			if (stream != null) {
				stream.close();
			}
		}
		catch (IOException ex) {
		}
	}

	public static <T> boolean different( T obj1, T obj2) {
		return !equals( obj1, obj2);
	}

	private static void doLog( Logger l, Level lvl, String msg, Object[] arr) {
		StackTraceElement frame = inferCaller( SELF);
		if (INNER) {
			LogRecord lr = new LogRecord( lvl, msg);
			if (frame != null) {
				lr.setSourceClassName( frame.getClassName());
				lr.setSourceMethodName( frame.getMethodName());
			}
			lr.setParameters( arr);
			doLog( l, lr);
		}
		else {
			if (frame != null) {
				l.logp( lvl, frame.getClassName(), frame.getMethodName(), msg, arr);
			}
			else {
				l.log( lvl, msg, arr);
			}
		}
	}

	private static void doLog( Logger logger, LogRecord lr) {
		lr.setLoggerName( logger.getName());
		String ebName = getEffectiveResourceBundleName( logger);
		if (ebName != null) {
			lr.setResourceBundleName( ebName);
			lr.setResourceBundle( logger.getResourceBundle());
		}
		logger.log( lr);
	}

	private static void doLogThrows( Logger l, Level lvl, String msg, Throwable ex) {
		StackTraceElement frame = inferCaller( SELF);
		if (INNER) {
			LogRecord lr = new LogRecord( lvl, msg);
			if (frame != null) {
				lr.setSourceClassName( frame.getClassName());
				lr.setSourceMethodName( frame.getMethodName());
			}
			lr.setThrown( ex);
			doLog( l, lr);
		}
		else {
			if (frame != null) {
				l.logp( lvl, frame.getClassName(), frame.getMethodName(), msg, ex);
			}
			else {
				l.log( lvl, msg, ex);
			}
		}
	}

	public static <T> boolean equals( T obj1, T obj2) {
		return obj1 == obj2 || obj1 != null && obj1.equals( obj2);
	}

	public static Window findRoot( Component c) {
		if (c == null) {
			return null;
		}
		if (c instanceof Window) {
			return (Window) c;
		}
		Container parent;
		for (parent = c.getParent(); parent != null; parent = parent.getParent()) {
			if (parent instanceof Window) {
				return (Window) parent;
			}
		}
		return null;
	}

	public static String format( String msg, Object... data) {
		if (data == null || data.length == 0) {
			return msg;
		}
		try {
			return MessageFormat.format( msg, data);
		}
		catch (IllegalArgumentException ex) {
			return msg;
		}
	}

	public static String getDateString() {
		return hqmDateString( System.currentTimeMillis());
	}

	private static String getEffectiveResourceBundleName( Logger logger) {
		for (Logger l = logger; l != null; l = l.getParent()) {
			String rbName = l.getResourceBundleName();
			if (rbName != null) {
				return rbName;
			}
		}
		return null;
	}

	/**
	 * returns a more human readable date format with milliseconds
	 */
	public static String getMillisDateString() {
		StringBuffer sb = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		cal.setTime( new Date( System.currentTimeMillis()));
		sb.append( cal.get( Calendar.YEAR));
		appentTwo( sb, cal.get( Calendar.MONTH) + 1);
		appentTwo( sb, cal.get( Calendar.DAY_OF_MONTH));
		appentTwo( sb, cal.get( Calendar.HOUR) + (cal.get( Calendar.AM_PM) == Calendar.PM ? 12 : 0));
		appentTwo( sb, cal.get( Calendar.MINUTE));
		appentTwo( sb, cal.get( Calendar.SECOND));
		appentThree( sb, cal.get( Calendar.MILLISECOND));
		return sb.toString();
	}

	public static String getPackageURL( Class<?> base) {
		String path = base.getName();
		path = path.replace( '.', '/') + ".class";
		try {
			URL url = ClassLoader.getSystemResource( path);
			if (url != null) {
				String result = url.toExternalForm();
				return result.substring( 0, result.indexOf( path));
			}
		}
		catch (Exception ex) {
		}
		log( SELF_LOGGER, Level.WARNING, "Utils.resource {0}", path);
		return ".";
	}

	public static String getRecouceURL( String name) {
		String path = name; //.replace('.', '/');
		try {
			if (path.startsWith( "/")) {
				path = path.substring( 1);
			}
			URL url = ClassLoader.getSystemResource( path);
			if (url != null) {
				return url.toExternalForm();
			}
		}
		catch (Exception ex) {
		}
		log( SELF_LOGGER, Level.WARNING, "Utils.resource {0}", path);
		return ".";
	}

	public static String hqmDateString( long date) {
		StringBuffer sb = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		cal.setTime( new Date( date));
		sb.append( cal.get( Calendar.YEAR));
		appentTwo( sb, cal.get( Calendar.MONTH) + 1);
		appentTwo( sb, cal.get( Calendar.DAY_OF_MONTH));
		appentTwo( sb, cal.get( Calendar.HOUR) + (cal.get( Calendar.AM_PM) == Calendar.PM ? 12 : 0));
		appentTwo( sb, cal.get( Calendar.MINUTE));
		appentTwo( sb, cal.get( Calendar.SECOND));
		return sb.toString();
	}

	private static StackTraceElement inferCaller( String self) {
		StackTraceElement[] stack = new Throwable().getStackTrace();
		int ix = 0;
		for (; ix < stack.length; ++ix) {
			if (self.equals( stack[ix].getClassName())) {
				break;
			}
		}
		for (; ix < stack.length; ++ix) {
			StackTraceElement frame = stack[ix];
			if (!self.equals( frame.getClassName())) {
				return frame;
			}
		}
		return null;
	}

	public static void invokeAndWait( Runnable r) {
//		logDispatchThread();
		if (SwingUtilities.isEventDispatchThread()) {
			r.run();
		}
		else {
			try {
				SwingUtilities.invokeAndWait( r);
			}
			catch (Exception ex) {
				Utils.logThrows( SELF_LOGGER, Level.WARNING, ex);
			}
		}
	}

	public static void invokeLater( Runnable r) {
//		logDispatchThread();
		if (EventQueue.isDispatchThread()) {
			r.run();
		}
		else {
			EventQueue.invokeLater( r);
		}
	}

	public static boolean isBooleanString( String value) {
		return "true".equalsIgnoreCase( value) || "false".equalsIgnoreCase( value);
	}

	public static boolean isIntegerString( String value) {
		try {
			Integer.parseInt( value);
			return true;
		}
		catch (NumberFormatException ex) {
		}
		return false;
	}

	public static void log( Logger l, Level lvl, String msg, Object... os) {
		if (l.isLoggable( lvl)) {
			Object[] arr = new Object[os.length];
			for (int i = 0; i < os.length; ++i) {
				arr[i] = String.valueOf( os[i]);
			}
			doLog( l, lvl, msg, arr);
		}
	}

	public static void log( Logger l, LogRecord lr) {
		if (l.isLoggable( lr.getLevel())) {
			l.log( lr);
		}
	}

	public static void logArr( Logger l, Level lvl, String msg, Object[] arr) {
		if (l.isLoggable( lvl)) {
			doLog( l, lvl, msg, new Object[] {
				toString( arr)
			});
		}
	}

	public static void logDispatchThread() {
		Utils.log( SELF_LOGGER, Level.FINEST, "SwingUtilities.isEventDispatchThread()  {0}", SwingUtilities.isEventDispatchThread());
	}

	public static void logProperty( Logger l, Level lvl, String property) {
		if (l.isLoggable( lvl)) {
			doLog( l, lvl, "{0} = {1}", new Object[] {
				property, System.getProperty( property, null)
			});
		}
	}

	public static void logThrows( Logger l, Level lvl, Throwable ex) {
		if (l.isLoggable( lvl)) {
			if (sTraceStack) {
				doLogThrows( l, lvl, "", ex);
//				ex.printStackTrace();
			}
			else {
				doLog( l, lvl, "", new Object[] {
					ex
				});
			}
		}
	}

	public static <T> boolean match( T obj1, T obj2) {
		if (obj1 != null) {
			return obj1.equals( obj2);
		}
		return obj2 == null;
	}

	public static boolean parseBoolean( String value) {
		return parseBoolean( value, false);
	}

	public static boolean parseBoolean( String value, boolean def) {
		if ("true".equalsIgnoreCase( value)) {
			return true;
		}
		if ("false".equalsIgnoreCase( value)) {
			return false;
		}
		return def;
	}

	public static byte parseByte( String value, byte def) {
		if (value != null) {
			try {
				return Byte.parseByte( value);
			}
			catch (NumberFormatException ex) {
			}
		}
		return def;
	}

	public static double parseDouble( String value) {
		return parseDouble( value, 0);
	}

	public static double parseDouble( String value, double def) {
		if (value != null) {
			try {
				return Double.parseDouble( value);
			}
			catch (NumberFormatException ex) {
			}
		}
		return def;
	}

	public static float parseFloat( String value, float def) {
		if (value != null) {
			try {
				return Float.parseFloat( value);
			}
			catch (NumberFormatException ex) {
			}
		}
		return def;
	}

	public static int parseInteger( String value) {
		return parseInteger( value, 0);
	}

	public static int parseInteger( String value, int def) {
		if (value != null) {
			try {
				return Integer.parseInt( value);
			}
			catch (NumberFormatException ex) {
			}
		}
		return def;
	}

	public static long parseLong( String value) {
		return parseLong( value, 0);
	}

	public static long parseLong( String value, long def) {
		if (value != null) {
			try {
				return Long.parseLong( value);
			}
			catch (NumberFormatException ex) {
			}
		}
		return def;
	}

	public static short parseShort( String value, short def) {
		if (value != null) {
			try {
				return Short.parseShort( value);
			}
			catch (NumberFormatException ex) {
			}
		}
		return def;
	}

	public static void printCaller( String self) {
		StackTraceElement frame = inferCaller( self);
		StringBuffer sb = new StringBuffer( "Call from ");
		sb.append( frame.getClassName());
		sb.append( '.');
		sb.append( frame.getMethodName());
		sb.append( "() at line ");
		sb.append( frame.getLineNumber());
		System.out.println( sb.toString());
	}

	public static void printDispatchThread() {
		System.out.println( "SwingUtilities.isEventDispatchThread() " + SwingUtilities.isEventDispatchThread());
	}

	public static void setLocationRelativeTo( Component c, Component dia) {
		Container root = null;
		if (c != null) {
			if (c instanceof Window) {
				root = (Container) c;
			}
			else {
				Container parent;
				for (parent = c.getParent(); parent != null; parent = parent.getParent()) {
					if (parent instanceof Window) {
						root = parent;
						break;
					}
				}
			}
		}
		if (c != null && !c.isShowing() || root == null || !root.isShowing()) {
			Dimension paneSize = dia.getSize();
			Dimension screenSize = dia.getToolkit().getScreenSize();
			dia.setLocation( (screenSize.width - paneSize.width) / 2, (screenSize.height - paneSize.height) / 2);
		}
		else {
			Dimension invokerSize = c.getSize();
			Point invokerScreenLocation = new Point( 0, 0);
			if (root instanceof Window) {
				Component tc = c;
				while (tc != null) {
					Point tcl = tc.getLocation();
					invokerScreenLocation.x += tcl.x;
					invokerScreenLocation.y += tcl.y;
					if (tc == root) {
						break;
					}
					tc = tc.getParent();
				}
			}
			Rectangle dialogBounds = dia.getBounds();
			int dx = invokerScreenLocation.x + (invokerSize.width - dialogBounds.width >> 1);
			int dy = invokerScreenLocation.y + (invokerSize.height - dialogBounds.height >> 1);
			Dimension ss = dia.getToolkit().getScreenSize();
			if (dy + dialogBounds.height > ss.height) {
				dy = ss.height - dialogBounds.height;
				dx = invokerScreenLocation.x < ss.width >> 1 ? invokerScreenLocation.x + invokerSize.width : invokerScreenLocation.x - dialogBounds.width;
			}
			if (dx + dialogBounds.width > ss.width) {
				dx = ss.width - dialogBounds.width;
			}
			if (dx < 0) {
				dx = 0;
			}
			if (dy < 0) {
				dy = 0;
			}
			dia.setLocation( dx, dy);
		}
	}

	public static void setStackTrace( boolean traceStack) {
		sTraceStack = traceStack;
	}

	public static String shortClassName( Object obj) {
		if (obj == null) {
			return "null";
		}
		String name = obj.getClass().getName();
		return name.substring( name.lastIndexOf( '.') + 1);
	}

	public static boolean startsWithOnly( String name, String match) {
		if (name.startsWith( match)) {
			try {
				int off = match.length() + 1;
				int pos = name.indexOf( '_', off);
				if (pos > off) {
					Integer.parseInt( name.substring( off, pos));
					return false;
				}
			}
			catch (Exception ex) {
			}
			return true;
		}
		return false;
	}

	private static String toString( Object[] arr) {
		StringBuffer sb = new StringBuffer();
		if (arr != null) {
			sb.append( '[');
			for (int i = 0; i < arr.length; ++i) {
				if (i > 0) {
					sb.append( ", ");
				}
				sb.append( '"');
				sb.append( arr[i].toString());
				sb.append( '"');
			}
			sb.append( ']');
		}
		else {
			sb.append( "null");
		}
		return sb.toString();
	}

	public static boolean validString( String value) {
		return value != null && !"".equals( value);
	}
}
