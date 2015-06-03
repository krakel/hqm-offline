package de.doerl.hqm.medium.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IMedium;
import de.doerl.hqm.medium.IMediumWorker;
import de.doerl.hqm.medium.IRefreshListener;
import de.doerl.hqm.medium.MediumUtils;

public class Medium implements IMedium {
//	private static final Logger LOGGER = Logger.getLogger( Medium.class.getName());
	public static final FileFilter FILTER = new FileNameExtensionFilter( "JSON file", "json");
	public static final String MEDIUM = "json";
	public static final String JSON_PATH = "json_path";

	static File normalize( File choose) {
		return MediumUtils.normalize( choose, ".json");
	}

	static void readHqm( FHqm hqm, InputStream is) throws IOException {
		Parser parser = new Parser( is);
		parser.readSrc( hqm);
	}

	static File suggest( String name) {
		return name != null ? new File( name + ".json") : null;
	}

	static String toName( File src) {
		if (src == null) {
			return "unknown";
		}
		String name = src.getName();
		int pos = name.lastIndexOf( '.');
		return pos < 0 ? name : name.substring( 0, pos);
	}

	static void writeHQM( FHqm hqm, OutputStream os) throws IOException {
		Serializer serializer = new Serializer( os);
		serializer.writeDst( hqm);
		serializer.flushDst();
	}

	@Override
	public <T, U> T accept( IMediumWorker<T, U> w, U p) {
		return w.forMedium( this, p);
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
	public void testLoad( FHqm hqm, InputStream is) throws IOException {
		readHqm( hqm, is);
	}

	@Override
	public void testSave( FHqm hqm, OutputStream os) throws IOException {
		writeHQM( hqm, os);
	}
}
