package de.doerl.hqm.medium;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.Utils;

public class MediumUtils {
	private static final Logger LOGGER = Logger.getLogger( MediumUtils.class.getName());
	private static final int MAX_BACKUP = 3;

	public static void backupDir( File src) {
		if (src != null && src.isDirectory()) {
			File backupDir = new File( src.getParentFile(), "backup");
			if (!backupDir.exists()) {
				backupDir.mkdir();
			}
			File olderDir = new File( backupDir, createBackupName( src.getName(), MAX_BACKUP));
			for (int i = MAX_BACKUP - 1; i > 0; --i) {
				if (olderDir.exists()) {
					deleteDir( olderDir);
				}
				File oldDir = new File( backupDir, createBackupName( src.getName(), i));
				if (oldDir.exists() && !oldDir.renameTo( olderDir)) {
					Utils.log( LOGGER, Level.WARNING, "cannot rename older folder {0}", oldDir);
				}
				olderDir = oldDir;
			}
			File[] arr = src.listFiles();
			if (arr != null) {
				olderDir.mkdir();
				for (File file : arr) {
					if (file.isFile()) {
						if (!file.renameTo( new File( olderDir, file.getName()))) {
							Utils.log( LOGGER, Level.WARNING, "cannot rename old file {0}", file);
						}
					}
					else {
						Utils.log( LOGGER, Level.WARNING, "nested folder on backup {0}", file);
					}
				}
			}
		}
	}

	public static void backupFile( File src) {
		if (src != null && src.isFile()) {
			File backupDir = new File( src.getParentFile(), "backup");
			if (!backupDir.exists()) {
				backupDir.mkdir();
			}
			File olderFile = new File( backupDir, createBackupName( src.getName(), MAX_BACKUP));
			for (int i = MAX_BACKUP - 1; i > 0; --i) {
				if (olderFile.exists()) {
					olderFile.delete();
				}
				File oldFile = new File( backupDir, createBackupName( src.getName(), i));
				if (oldFile.exists() && !oldFile.renameTo( olderFile)) {
					Utils.log( LOGGER, Level.WARNING, "cannot rename older file {0}", oldFile);
				}
				olderFile = oldFile;
			}
			if (!src.renameTo( olderFile)) {
				Utils.log( LOGGER, Level.WARNING, "cannot rename old file {0}", src);
			}
		}
	}

	private static String createBackupName( String name, int idx) {
		int pos = name.lastIndexOf( '.');
		if (pos < 0) {
			return name + "_old" + idx;
		}
		else {
			return name.substring( 0, pos) + "_old" + idx + name.substring( pos);
		}
	}

	public static void deleteDir( File dir) {
		if (dir != null && dir.isDirectory()) {
			File[] arr = dir.listFiles();
			if (arr != null) {
				for (File file : arr) {
					if (file.isDirectory()) {
						deleteDir( file);
					}
					else {
						file.delete();
					}
				}
			}
			dir.delete();
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

	public void block( Path path, Path watchedFile) {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			WatchKey key = path.register( watcher, ENTRY_DELETE, OVERFLOW);
			while (key.reset()) {
				try {
					key = watcher.take(); // this will actually block
					List<WatchEvent<?>> watchEvents = key.pollEvents();
					if (watchEvents.stream().filter( event -> ENTRY_DELETE.equals( event.kind())).anyMatch( event -> watchedFile.getFileName().equals( event.context()))) {
						key.cancel();
					}
				}
				catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
					key.cancel();
				}
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}
}
