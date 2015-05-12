package de.doerl.hqm.medium;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MediumUtils {
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
			if (fis != null) {
				fis.close();
			}
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
