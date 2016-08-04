/*******************************************************************************
 * (c) Copyright IBM Corporation 2016. All Rights Reserved.
 *
 *******************************************************************************/
package de.doerl.hqm.utils.nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import de.doerl.hqm.utils.Utils;

public class NbtWriter1 {
	private static final Logger LOGGER = Logger.getLogger( NbtWriter.class.getName());

	public NbtWriter1() {
	}

	private static void compress( InputStream in, OutputStream out) {
		GZIPOutputStream os = null;
		try {
			os = new GZIPOutputStream( out);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read( buffer)) != -1) {
				os.write( buffer, 0, len);
			}
		}
		catch (IOException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
		finally {
			Utils.closeIgnore( os);
		}
	}

	public static byte[] write( FCompound main) {
		if (main != null) {
			try {
				byte[] res = write0( main);
				ByteArrayInputStream in = new ByteArrayInputStream( res);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				compress( in, out);
				return out.toByteArray();
			}
			catch (IOException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return null;
	}

	static byte[] write0( FCompound main) throws IOException {
		NbtWriter1 wrt = new NbtWriter1();
		return wrt.doAll( main);
	}

	private byte[] doAll( FCompound main) {
		// TODO Auto-generated method stub
		return null;
	}
}
