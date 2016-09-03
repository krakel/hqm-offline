package de.doerl.hqm.medium.gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
import de.doerl.hqm.medium.MediumUtils;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.json.IJson;
import de.doerl.hqm.utils.json.JsonReader;

public class Medium implements IMedium {
	private static final Logger LOGGER = Logger.getLogger( Medium.class.getName());
	public static final FileFilter FILTER = new FileNameExtensionFilter( "JSON file", "json");
	public static final String MEDIUM = "gson";
	public static final String GSON_PATH = "gson_path";
	static final String DESCRIPTION_FILE = "description.txt";
	static final String BAG_FILE = "bags";
	static final String DEATH_FILE = "deaths";
	static final String REPUTATION_FILE = "reputations";
	static final String TEAM_FILE = "teams";
	static final String STATE_FILE = "state";
	static final String DATA_FILE = "data";
	static final String SET_FILE = "sets";

	static File getFile( String name, File base) {
		if (name.endsWith( ".txt")) {
			return new File( base, name);
		}
		else {
			return new File( base, name + ".json");
		}
	}

//	private static File getModpackBase( File src) {
//		if (src != null) {
//			try {
//				String name = src.getCanonicalPath();
//				if (name.contains( "hqm")) {
//					File path = src;
//					while (path != null && !"default".equals( path.getName())) {
//						path = path.getParentFile();
//					}
//					if (path != null) {
//						path = path.getParentFile();
//					}
//					if (path != null) {
//						return path;
//					}
//				}
//			}
//			catch (IOException ex) {
//				Utils.logThrows( LOGGER, Level.WARNING, ex);
//			}
//		}
//		return null;
//	}
//
	private static String getModpackName( File src) {
		if (src != null) {
			try {
				String name = src.getCanonicalPath();
				if (name.contains( "hqm")) {
					File path = src;
					while (path != null && !"default".equals( path.getName())) {
						path = path.getParentFile();
					}
					if (path != null) {
						path = path.getParentFile();
					}
					if (path != null && "hqm".equals( path.getName())) {
						path = path.getParentFile();
					}
					if (path != null && "config".equals( path.getName())) {
						path = path.getParentFile();
					}
					if (path != null && "minecraft".equals( path.getName())) {
						path = path.getParentFile();
					}
					if (path != null) {
						return path.getName();
					}
				}
			}
			catch (IOException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return MediumUtils.getFilepackName( src);
	}

	static boolean isQuestSet( File file) {
		if (!file.isFile()) {
			return false;
		}
		String name = file.getName();
		if (!name.endsWith( ".json")) {
			return false;
		}
		String base = name.substring( 0, name.lastIndexOf( '.')).toLowerCase();
		if (BAG_FILE.equals( base)) {
			return false;
		}
		if (DEATH_FILE.equals( base)) {
			return false;
		}
		if (REPUTATION_FILE.equals( base)) {
			return false;
		}
		if (TEAM_FILE.equals( base)) {
			return false;
		}
		if (STATE_FILE.equals( base)) {
			return false;
		}
		if (DATA_FILE.equals( base)) {
			return false;
		}
		if (SET_FILE.equals( base)) {
			return false;
		}
		return true;
	}

	static FHqm loadHqm( File file) {
		File base = file.isDirectory() ? file : file.getParentFile();
		String name = getModpackName( base);
		FHqm hqm = new FHqm( name);
		hqm.setMain( FHqm.LANG_EN_US);
		if (base != null) {
			readHqm( hqm, base);
			MediaManager.setProperty( hqm, GSON_PATH, base);
			MediaManager.setProperty( hqm, MediaManager.ACTIV_MEDIUM, MEDIUM);
			MediaManager.setProperty( hqm, MediaManager.ACTIV_PATH, base.getParentFile());
			return hqm;
		}
		return null;
	}

	static String loadTxt( File file) {
		if (file.exists()) {
			try {
				byte[] src = Files.readAllBytes( file.toPath());
				return new String( src, StandardCharsets.UTF_8);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.FINER, ex);
			}
		}
		return null;
	}

	public static void main( String[] args) {
		File file = new File( "D:\\Games\\Minecraft\\hqm\\hqm-Brave\\default");
		String name = getModpackName( file);
		FHqm hqm = new FHqm( name);
		hqm.setMain( FHqm.LANG_EN_US);
		readHqm( hqm, file);
	}

	static void readHqm( FHqm hqm, File base) {
		Parser parser = new Parser( hqm.mMain, base);
		parser.readSrc( hqm);
	}

	static IJson redJson( File file) {
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
		return json;
	}

	public static boolean saveHQM( FHqm hqm, File base) {
		try {
//			MediumUtils.createBackup( base);
			Serializer serializer = new Serializer( base, hqm.mMain);
			serializer.writeDst( hqm);
			MediaManager.setProperty( hqm, GSON_PATH, base);
			return true;
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		return false;
	}

	static void saveTxt( File file, String txt) {
		FileWriter writer = null;
		try {
			writer = new FileWriter( file);
			writer.write( txt);
		}
		catch (Exception ex) {
			Utils.logThrows( LOGGER, Level.FINER, ex);
		}
		finally {
			Utils.closeIgnore( writer);
		}
	}

	public static File suggest( String name) {
		return name != null ? new File( name + ".json") : null;
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
		if (val != null && val.toLowerCase().endsWith( "default")) {
			return this;
		}
		return null;
	}
}
