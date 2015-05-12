package de.doerl.hqm.utils;

import java.io.File;

@SuppressWarnings( "nls")
public final class SystemInfo {
	public static final File HQM_DIR;
	private static boolean sDebug = Boolean.getBoolean( "hqm.systeminfo.debug");
	static {
		initPath();
		HQM_DIR = new File( System.getProperty( "hqm.preferences"));
		if (!HQM_DIR.exists() && !HQM_DIR.mkdir()) {
			System.err.println( "Panic!!! Can not create preference dir!!!");
		}
	}

	private static String clearPath( String home) {
		if (home.endsWith( "/")) {
			home = home.substring( 0, home.length() - 1);
		}
		if (home.endsWith( "/bin")) {
			home = home.substring( 0, home.length() - 4);
		}
		if (home.endsWith( "/classes")) {
			home = home.substring( 0, home.length() - 8);
		}
		return home.substring( home.indexOf( ':') + 1, home.length());
	}

	public static void init() {
	}

	public static void initPath() {
		String home = System.getProperty( "hqm.home.path");
		if (home == null) {
			home = Utils.getPackageURL( SystemInfo.class);
			home = removePkg( home);
			home = clearPath( home);
			System.setProperty( "hqm.home.path", home);
		}
		String dir = System.getProperty( "hqm.dir");
		if (dir == null) {
			dir = System.getProperty( "user.dir");
			dir = clearPath( dir);
			System.setProperty( "hqm.dir", dir);
		}
		String sep = System.getProperty( "file.separator");
		setProperty( "hqm.preferences", System.getProperty( "user.home") + sep + ".hqm");
		if (sDebug) {
			System.out.println( "hqm.preferences: " + System.getProperty( "hqm.preferences"));
			System.out.println( "hqm.home.path: " + System.getProperty( "hqm.home.path"));
			System.out.println( "hqm.dir: " + System.getProperty( "hqm.dir"));
		}
	}

	private static String removePkg( String url) {
		if (url.startsWith( "jar:") && url.endsWith( "!/")) {
			int end = url.lastIndexOf( '/', url.length() - 3);
			return url.substring( 4, end + 1);
		}
		return url;
	}

	public static void setProperty( String name, String src) {
		if (System.getProperty( name) == null) {
			System.setProperty( name, src);
		}
	}

	public static void setPropResource( String name, String src) {
		if (System.getProperty( name) == null) {
			System.setProperty( name, Utils.getRecouceURL( src));
		}
	}
}
