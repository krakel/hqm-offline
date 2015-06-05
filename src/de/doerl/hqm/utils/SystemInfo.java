package de.doerl.hqm.utils;

import java.io.File;
import java.util.logging.Level;

public final class SystemInfo {
	public static final File HQM_DIR;
	static {
		initPath();
		HQM_DIR = new File( System.getProperty( "hqm.pref.dir"));
		if (!HQM_DIR.exists() && !HQM_DIR.mkdir()) {
			System.err.println( "Panic!!! Can not create preference dir!!!");
		}
	}

	public static void init() {
	}

	public static void initPath() {
		setProperty( "hqm.file.logLevel", Level.INFO.getName());
		setProperty( "hqm.console.logLevel", Level.FINEST.getName());
		String sep = System.getProperty( "file.separator");
		setProperty( "hqm.pref.dir", System.getProperty( "user.home") + sep + ".hqm");
	}

	public static void setProperty( String name, String src) {
		if (System.getProperty( name) == null) {
			System.setProperty( name, src);
		}
	}
}
