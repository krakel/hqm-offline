package de.doerl.hqm.medium.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IMedium;
import de.doerl.hqm.medium.IMediumWorker;
import de.doerl.hqm.medium.IRefreshListener;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.medium.MediumUtils;
import de.doerl.hqm.utils.Utils;

public class Medium implements IMedium {
	private static final Logger LOGGER = Logger.getLogger( Medium.class.getName());
	public static final FileFilter FILTER = new FileNameExtensionFilter( "JSON file", "json");
	public static final String MEDIUM = "json";
	public static final String JSON_PATH = "json_path";

	private static void findAllLangs( FHqm hqm, File file) {
		String main = toName( file);
		File[] result = file.getParentFile().listFiles( new LangFiles( main + '_'));
		for (int i = 0; i < result.length; ++i) {
			String ll = result[i].getName();
			int begin = ll.indexOf( '_') + 1;
			int end = ll.indexOf( '.');
			if (begin > 0 && begin < end) {
				String lang = ll.substring( begin, end);
				if (!hqm.mLanguages.contains( lang)) {
					hqm.mLanguages.add( lang);
				}
			}
		}
	}

	static FHqm loadHqm( File file) {
		InputStream is = null;
		try {
			String name = toName( file);
			FHqm hqm = new FHqm( name);
			is = new FileInputStream( file);
			readHqm( hqm, is);
			findAllLangs( hqm, file);
			MediaManager.setProperty( hqm, JSON_PATH, file);
			MediaManager.setProperty( hqm, MediaManager.ACTIV_MEDIUM, MEDIUM);
			MediaManager.setProperty( hqm, MediaManager.ACTIV_PATH, file.getParentFile());
			for (String lang : hqm.mLanguages) {
				loadLang( hqm, lang, toLangFile( file, lang));
			}
			return hqm;
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.FINER, ex);
		}
		finally {
			Utils.closeIgnore( is);
		}
		return null;
	}

	static FHqm loadLang( FHqm hqm, String lang, File file) {
		InputStream is = null;
		try {
			is = new FileInputStream( file);
			readLang( hqm, lang, is);
			return hqm;
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.FINER, ex);
		}
		finally {
			Utils.closeIgnore( is);
		}
		return null;
	}

	static File normalize( File choose) {
		return MediumUtils.normalize( choose, ".json");
	}

	private static void readHqm( FHqm hqm, InputStream is) throws IOException {
		Parser parser = new Parser( is);
		parser.readSrc( hqm);
	}

	private static void readLang( FHqm hqm, String lang, InputStream is) throws IOException {
		ParserLang parser = new ParserLang( is, lang);
		parser.readSrc( hqm);
	}

	static boolean saveHQM( FHqm hqm, File file) {
		OutputStream os = null;
		try {
			MediumUtils.createBackup( file);
			os = new FileOutputStream( file);
			writeHQM( hqm, os);
			MediaManager.setProperty( hqm, Medium.JSON_PATH, file);
			for (String lang : hqm.mLanguages) {
				saveLang( hqm, lang, toLangFile( file, lang));
			}
			return true;
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( os);
		}
		return false;
	}

	private static void saveLang( FHqm hqm, String lang, File file) {
		OutputStream os = null;
		try {
			MediumUtils.createBackup( file);
			os = new FileOutputStream( file);
			writeLang( hqm, lang, os);
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( os);
		}
	}

	static File suggest( String name) {
		return name != null ? new File( name + ".json") : null;
	}

	private static File toLangFile( File file, String lang) {
		String name = file.getName();
		int pos = name.indexOf( '.');
		if (pos < 0) {
			return new File( file.getParent(), String.format( "%s_%s", name, lang));
		}
		else {
			return new File( file.getParent(), String.format( "%s_%s%s", name.substring( 0, pos), lang, name.substring( pos)));
		}
	}

	static String toName( File src) {
		if (src == null) {
			return "unknown";
		}
		String name = src.getName();
		int pos = name.lastIndexOf( '.');
		return pos < 0 ? name : name.substring( 0, pos);
	}

	private static void writeHQM( FHqm hqm, OutputStream os) throws IOException {
		Serializer serializer = new Serializer( os);
		serializer.writeDst( hqm);
		serializer.flushDst();
	}

	private static void writeLang( FHqm hqm, String lang, OutputStream os) throws IOException {
		SerializerLang serializer = new SerializerLang( os, lang);
		serializer.writeDst( hqm);
		serializer.flushDst();
	}

	@Override
	public <T, U> T accept( IMediumWorker<T, U> w, U p) {
		return w.forMedium( this, p);
	}

	public String getIcon() {
		return "json.gif";
	}

	@Override
	public String getName() {
		return MEDIUM;
	}

	@Override
	public IRefreshListener getOpen( ICallback cb) {
		return new OpenJSON( cb);
	}

	@Override
	public IRefreshListener getSave( ICallback cb) {
		return new SaveJSON( cb);
	}

	@Override
	public IRefreshListener getSaveAs( ICallback cb) {
		return new SaveAsJSON( cb);
	}

	@Override
	public FHqm openHqm( File file) {
		return loadHqm( file);
	}

	public IMedium parse( String val) {
		if (val != null && val.toLowerCase().endsWith( ".json")) {
			return this;
		}
		return null;
	}

	@Override
	public void testLoad( FHqm hqm, InputStream is) throws IOException {
		readHqm( hqm, is);
	}

	@Override
	public void testSave( FHqm hqm, OutputStream os) throws IOException {
		writeHQM( hqm, os);
	}

	private static final class LangFiles implements FilenameFilter {
		private String mMain;

		private LangFiles( String main) {
			mMain = main;
		}

		@Override
		public boolean accept( File dir, String name) {
			return name.startsWith( mMain) && name.toLowerCase().endsWith( ".json");
		}
	}
}
