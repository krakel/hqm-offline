package de.doerl.hqm.medium.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FLanguage;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IMedium;
import de.doerl.hqm.medium.IMediumWorker;
import de.doerl.hqm.medium.IRefreshListener;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.medium.MediumUtils;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.json.FObject;
import de.doerl.hqm.utils.json.FValue;
import de.doerl.hqm.utils.json.IJson;
import de.doerl.hqm.utils.json.JsonReader;

public class Medium implements IMedium {
	private static final Logger LOGGER = Logger.getLogger( Medium.class.getName());
	public static final FileFilter FILTER = new FileNameExtensionFilter( "JSON file", "json");
	public static final String MEDIUM = "json";
	public static final String JSON_PATH = "json_path";

	private static File fromLangFile( File file) {
		String name = file.getName();
		int begin = name.indexOf( '_');
		if (begin < 0) {
			int end = name.indexOf( '.');
			if (end < 0) {
				return fromLangFile( file, name);
			}
			else {
				return new File( file.getParent(), String.format( "%s.json", name.substring( 0, end)));
			}
		}
		else {
			return new File( file.getParent(), String.format( "%s.json", name.substring( 0, begin)));
		}
	}

	private static File fromLangFile( File file, String child) {
		return new File( file.getParent(), String.format( "%s.json", child));
	}

	static FHqm loadHqm( File file) {
		String lang = toLang( file.getName());
		if (lang != null) {
			File main = fromLangFile( file);
			if (main.exists()) {
				return loadHqm( redJson( main), lang, main, true);
			}
			else {
				return null;
			}
		}
		else {
			FObject obj = redJson( file);
			if (obj != null && obj.get( IToken.HQM_PARENT) != null) {
				File main = fromLangFile( file, FValue.toString( obj.get( IToken.HQM_PARENT)));
				if (main.exists()) {
					return loadHqm( redJson( main), FHqm.LANG_EN_US, main, false);
				}
				else {
					return null;
				}
			}
			else {
				return loadHqm( obj, FHqm.LANG_EN_US, file, false);
			}
		}
	}

	private static FHqm loadHqm( FObject obj, String lang, File file, boolean override) {
		String name = MediumUtils.getFilepackName( file);
		FHqm hqm = new FHqm( name);
		hqm.setMain( lang);
		readHqm( hqm, obj, hqm.mMain, true, false);
		MediaManager.setProperty( hqm, JSON_PATH, file);
		MediaManager.setProperty( hqm, MediaManager.ACTIV_MEDIUM, MEDIUM);
		MediaManager.setProperty( hqm, MediaManager.ACTIV_PATH, file.getParentFile());
		for (FLanguage ll : hqm.mLanguages) {
			File src = toLangFile( file, ll.mLocale);
			if (src.exists()) {
				FObject docu = redJson( src);
				readHqm( hqm, docu, ll, false, true);
			}
		}
		if (override) {
			hqm.setMain( lang);
		}
		return hqm;
	}

	static File normalize( File choose) {
		return MediumUtils.normalize( choose, ".json");
	}

	static void readHqm( FHqm hqm, FObject obj, FLanguage lang, boolean withMain, boolean withDocu) {
		if (obj != null) {
			Parser parser = new Parser( lang, withMain, withDocu);
			parser.readSrc( hqm, obj);
		}
	}

	private static FObject redJson( File file) {
		IJson json = null;
		InputStream is = null;
		try {
			is = new FileInputStream( file);
			JsonReader src = new JsonReader( is);
			json = src.doAll();
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.FINER, ex);
		}
		finally {
			Utils.closeIgnore( is);
		}
		return FObject.to( json);
	}

	static boolean saveHQM( FHqm hqm, File file) {
		OutputStream os = null;
		try {
			MediumUtils.createBackup( file);
			os = new FileOutputStream( file);
			boolean withLang = PreferenceManager.getBool( BaseDefaults.LANGUAGE);
			if (withLang) {
				writeHQM( hqm, os, hqm.mMain, true, false);
				MediaManager.setProperty( hqm, Medium.JSON_PATH, file);
				for (FLanguage lang : hqm.mLanguages) {
					saveLang( hqm, lang, toLangFile( file, lang.mLocale));
				}
			}
			else {
				writeHQM( hqm, os, hqm.mMain, true, true);
				MediaManager.setProperty( hqm, Medium.JSON_PATH, file);
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

	private static void saveLang( FHqm hqm, FLanguage lang, File file) {
		OutputStream os = null;
		try {
			MediumUtils.createBackup( file);
			os = new FileOutputStream( file);
			writeHQM( hqm, os, lang, false, true);
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

	private static String toLang( String name) {
		int begin = name.indexOf( '_');
		if (begin < 0) {
			return null;
		}
		int end = name.indexOf( '.');
		if (end < 0) {
			return name.substring( begin + 1);
		}
		else if (begin < end) {
			return name.substring( begin + 1, end);
		}
		else {
			return null;
		}
	}

	private static File toLangFile( File file, String lang) {
		String name = file.getName();
		int pos = name.indexOf( '.');
		if (pos < 0) {
			return new File( file.getParent(), String.format( "%s_%s.json", name, lang));
		}
		else {
			return new File( file.getParent(), String.format( "%s_%s.json", name.substring( 0, pos), lang));
		}
	}

	private static void writeHQM( FHqm hqm, OutputStream os, FLanguage lang, boolean withMain, boolean withDocu) throws IOException {
		Serializer serializer = new Serializer( os, lang, withMain, withDocu);
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
		hqm.setMain( FHqm.LANG_EN_US);
		JsonReader src = new JsonReader( is);
		readHqm( hqm, FObject.to( src.doAll()), hqm.mMain, true, true);
	}

	@Override
	public void testSave( FHqm hqm, OutputStream os) throws IOException {
		writeHQM( hqm, os, hqm.mMain, true, true);
	}
}
