package de.doerl.hqm.medium;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.Utils;

public class MediumUtils {
	private static final Logger LOGGER = Logger.getLogger( MediumUtils.class.getName());
	private static final int MAX_BACKUP = 3;

	public static void createBackup( File src) {
		if (src != null) {
			File olderFile = new File( src.getAbsolutePath() + ".old" + MAX_BACKUP);
			for (int i = MAX_BACKUP - 1; i > 0; --i) {
				if (olderFile.exists()) {
					olderFile.delete();
				}
				File oldFile = new File( src.getAbsolutePath() + ".old" + i);
				if (oldFile.exists() && !oldFile.renameTo( olderFile)) {
					Utils.log( LOGGER, Level.WARNING, "cannot rename older file {0}", src.getName());
				}
				olderFile = oldFile;
			}
			if (src.exists() && !src.renameTo( olderFile)) {
				Utils.log( LOGGER, Level.WARNING, "cannot rename old file {0}", src.getName());
			}
		}
	}

	public static String getFilepackName( File src) {
		if (src != null) {
			String name = src.getName();
			int pos = name.lastIndexOf( '.');
			return pos < 0 ? name : name.substring( 0, pos);
		}
		else {
			return "unknown";
		}
	}

	public static InputStream getSource( File src) throws IOException {
		if (!src.exists()) {
			throw new IOException( "SOURCE does not exist: " + src);
		}
		byte[] buff = new byte[(int) src.length()];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream( src);
			fis.read( buff);
		}
		finally {
			Utils.closeIgnore( fis);
		}
		return new ByteArrayInputStream( buff);
	}

	public static File normalize( File choose, String suffix) {
		String norm = truncName( choose.getName(), suffix);
		return new File( choose.getParent(), norm + suffix);
	}

	public static String truncName( String name, String suffix) {
		if (name.toLowerCase().endsWith( suffix)) {
			return name.substring( 0, name.length() - suffix.length());
		}
		return name;
	}
}
