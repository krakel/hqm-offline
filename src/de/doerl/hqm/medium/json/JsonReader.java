package de.doerl.hqm.medium.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;

class JsonReader {
	private static final Charset UTF_8 = Charset.forName( "UTF-8");
	private Reader mReader;

	public JsonReader( InputStream in) {
		mReader = new InputStreamReader( in, UTF_8);
	}

	public JsonReader( Reader in) {
		mReader = in;
	}

	public HashMap<String, Object> get() {
		HashMap<String, Object> result = new HashMap<>();
		try {
			char c = (char) mReader.read();
		}
		catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return result;
	}
}
