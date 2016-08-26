package de.doerl.hqm.medium.gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.json.FObject;
import de.doerl.hqm.utils.json.IJson;
import de.doerl.hqm.utils.json.JsonReader;

public class Medium implements IMedium {
	private static final Logger LOGGER = Logger.getLogger( Medium.class.getName());
	public static final FileFilter FILTER = new FileNameExtensionFilter( "JSON file", "json");
	public static final String MEDIUM = "gson";
	public static final String GSON_PATH = "gson_path";
	static final String MAIN_PATH = "";
	static final String DESCRIPTION_FILE = "description.txt";
	static final String REPUTATION_FILE = "reputations";
	static final String BAGS_FILE = "bags";
	static final String TEAMS_FILE = "teams";
	static final String STATE_FILE = "state";
	static final String DATA_FILE = "data";
	static final String SETS_FILE = "sets";
	static final String DEATHS_FILE = "deaths";

	static File getFile( String name) {
		if (name.endsWith( ".txt")) {
			return new File( MAIN_PATH, name);
		}
		else {
			return new File( MAIN_PATH, name + ".json");
		}
	}

	static File[] getFiles( String name) {
		File dir = new File( name);
		return dir.listFiles();
	}

	static boolean isQuestSet( File file) {
		if (!file.isFile()) {
			return false;
		}
		String name = file.getName();
		if (!name.endsWith( ".json")) {
			return false;
		}
		String base = name.substring( 0, name.indexOf( '.'));
		if (REPUTATION_FILE.equalsIgnoreCase( base)) {
			return false;
		}
		if (BAGS_FILE.equalsIgnoreCase( base)) {
			return false;
		}
		if (TEAMS_FILE.equalsIgnoreCase( base)) {
			return false;
		}
		if (STATE_FILE.equalsIgnoreCase( base)) {
			return false;
		}
		if (DATA_FILE.equalsIgnoreCase( base)) {
			return false;
		}
		if (SETS_FILE.equalsIgnoreCase( base)) {
			return false;
		}
		if (DEATHS_FILE.equalsIgnoreCase( base)) {
			return false;
		}
		return true;
	}

	static FHqm loadHqm( File file) {
		return null;
	}

	private static FHqm loadHqm( FObject obj, String text, File file, boolean override) {
		String name = toName( file);
		FHqm hqm = new FHqm( name);
		hqm.setMain( text);
		readHqm( hqm, obj);
		MediaManager.setProperty( hqm, GSON_PATH, file);
		MediaManager.setProperty( hqm, MediaManager.ACTIV_MEDIUM, MEDIUM);
		MediaManager.setProperty( hqm, MediaManager.ACTIV_PATH, file.getParentFile());
		return null;
	}

	static String loadTxt( File file) throws IOException {
		return new String( Files.readAllBytes( file.toPath()), StandardCharsets.UTF_8);
	}

	static String loadTxt( String src) {
		try {
			File file = getFile( src);
			return loadTxt( file);
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.FINER, ex);
		}
		return null;
	}

	public static File normalize( File choose) {
		return null;
	}

	static void readHqm( FHqm hqm, FObject obj) {
		if (obj != null) {
//			Parser parser = new Parser( lang, withMain, withDocu);
//			parser.readSrc( hqm, obj);
		}
	}

	static FObject redJson( File file) {
		IJson json = null;
		if (file.exists()) {
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
		}
		return FObject.to( json);
	}

	static FObject redJson( String src) {
		File file = getFile( src);
		return redJson( file);
	}

	public static boolean saveHQM( FHqm hqm, File src) {
		return false;
	}

	public static File suggest( String name) {
		return null;
	}

	static String toName( File src) {
		if (src == null) {
			return "unknown";
		}
		String name = src.getName();
		int pos = name.lastIndexOf( '.');
		return pos < 0 ? name : name.substring( 0, pos);
	}

	@Override
	public <T, U> T accept( IMediumWorker<T, U> w, U p) {
		return w.forMedium( this, p);
	}

	@Override
	public String getIcon() {
		return "gson.gif";
	}

	@Override
	public String getName() {
		return MEDIUM;
	}

	@Override
	public IRefreshListener getOpen( ICallback cb) {
		return new OpenGSON( cb);
	}

	@Override
	public IRefreshListener getSave( ICallback cb) {
		return new SaveGSON( cb);
	}

	@Override
	public IRefreshListener getSaveAs( ICallback cb) {
		return new SaveAsGSON( cb);
	}

	@Override
	public FHqm openHqm( File file) {
		return loadHqm( file);
	}

	@Override
	public IMedium parse( String val) {
		if (val != null && val.toLowerCase().endsWith( ".json")) {
			return this;
		}
		return null;
	}

	@Override
	public void testLoad( FHqm hqm, InputStream is) throws IOException {
	}

	@Override
	public void testSave( FHqm hqm, OutputStream os) throws IOException {
	}
}
