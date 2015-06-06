package de.doerl.hqm.medium.bits;

import java.io.File;
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
import de.doerl.hqm.medium.MediumUtils;
import de.doerl.hqm.utils.Utils;

public class Medium implements IMedium {
	private static final Logger LOGGER = Logger.getLogger( Medium.class.getName());
	public static final FileFilter FILTER = new FileNameExtensionFilter( "Hardcore Questing Mode", "hqm");
	public static final String MEDIUM = "bit";
	public static final String HQM_PATH = "hqm_path";

	static FHqm loadHqm( File file) {
		InputStream is = null;
		try {
			String name = toName( file);
			FHqm hqm = new FHqm( name);
			is = MediumUtils.getSource( file);
			readHqm( hqm, is);
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

	static String nameOfOriginal( File src) {
		String name = src.getName();
		if (!"quests.hqm".equals( name)) {
			return null;
		}
		File p1 = src.getParentFile();
		if (p1 == null || !"hqm".equals( p1.getName())) {
			return null;
		}
		File p2 = p1.getParentFile();
		if (p2 == null || !"config".equals( p2.getName())) {
			return null;
		}
		File p3 = p2.getParentFile();
		if (p3 == null) {
			return null;
		}
		return p3.getName();
	}

	static File normalize( File choose) {
		return MediumUtils.normalize( choose, ".hqm");
	}

	static File protectOriginal( File src) {
		if ("quests.hqm".equals( src.getName()) && src.exists()) {
			File protect = new File( src.getParentFile(), "quests.hqm.orig");
			if (protect.exists()) {
				return null;
			}
			if (src.renameTo( protect)) {
				return protect;
			}
			Utils.log( LOGGER, Level.SEVERE, "cannot rename original hqm file {0}", src);
		}
		return null;
	}

	static void readHqm( FHqm hqm, InputStream is) throws IOException {
		Parser parser = new Parser( is);
		parser.readSrc( hqm);
	}

	static void restoreOriginal( File old) {
		if (old != null && old.exists()) {
			File orig = new File( old.getParentFile(), "quests.hqm");
			if (orig.exists()) {
				orig.delete();
			}
			if (!old.renameTo( orig)) {
				Utils.log( LOGGER, Level.SEVERE, "cannot restore original hqm file {0}", old);
			}
		}
	}

	static File suggest( String name) {
		return name != null ? new File( name + ".hqm") : null;
	}

	static String toName( File src) {
		if (src == null) {
			return "unknown";
		}
		String orig = nameOfOriginal( src);
		if (orig != null) {
			return orig;
		}
		String name = src.getName();
		int pos = name.lastIndexOf( '.');
		return pos < 0 ? name : name.substring( 0, pos);
	}

	static void writeHQM( FHqm hqm, OutputStream os) throws IOException {
		Serializer serializer = new Serializer( os);
		serializer.writeDst( hqm);
		serializer.flush();
	}

	@Override
	public <T, U> T accept( IMediumWorker<T, U> w, U p) {
		return w.forMedium( this, p);
	}

	@Override
	public String getIcon() {
		return "hqm.gif";
	}

	@Override
	public String getName() {
		return MEDIUM;
	}

	@Override
	public IRefreshListener getOpen( ICallback cb) {
		return new OpenBit( cb);
	}

	@Override
	public IRefreshListener getSave( ICallback cb) {
		return new SaveBit( cb);
	}

	@Override
	public IRefreshListener getSaveAs( ICallback cb) {
		return new SaveAsBit( cb);
	}

	@Override
	public FHqm openHqm( File file) {
		return loadHqm( file);
	}

	@Override
	public IMedium parse( String val) {
		if (val != null && val.toLowerCase().endsWith( ".hqm")) {
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
}
