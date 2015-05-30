package de.doerl.hqm.medium.json;

import java.io.IOException;
import java.io.InputStream;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IHqmReader;

class Parser implements IHqmReader, IToken {
//	private static final Logger LOGGER = Logger.getLogger( Parser.class.getName());
	public Parser( InputStream is) throws IOException {
//		JsonWriter rdr = Json.createReader(is));
	}

	@Override
	public void closeSrc() {
	}

	@Override
	public void readSrc( FHqm hqm, ICallback cb) {
	}
}
