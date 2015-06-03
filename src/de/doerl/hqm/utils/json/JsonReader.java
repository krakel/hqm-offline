package de.doerl.hqm.utils.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class JsonReader {
	private Tokenizer mParser;

	private JsonReader( BufferedReader in) {
		mParser = new Tokenizer( in);
	}

	public JsonReader( InputStream in) throws IOException {
		this( new BufferedReader( new InputStreamReader( in, "UTF-8")));
	}

	public JsonReader( Reader in) throws IOException {
		this( new BufferedReader( in));
	}

	public IJson doAll() throws IOException {
		switch (mParser.nextToken()) {
			case EOF:
				return new FValue( "");
			case VALUE:
				FValue val = mParser.nextValue();
				doEOF();
				return val;
			case LEFT_BRACE:
				FObject obj = doObject();
				doEOF();
				return obj;
			case LEFT_SQUARE:
				FArray arr = doArray();
				doEOF();
				return arr;
			default:
				throw new IOException( "wrong json source");
		}
	}

	private FArray doArray() throws IOException {
		boolean loop = false;
		FArray arr = new FArray();
		do {
			switch (mParser.nextToken()) {
				case VALUE:
					arr.add( mParser.nextValue());
					loop = doArrayNext();
					break;
				case LEFT_BRACE:
					arr.add( doObject());
					loop = doArrayNext();
					break;
				case LEFT_SQUARE:
					arr.add( doArray());
					loop = doArrayNext();
					break;
				case RIGHT_SQUARE:
					loop = false;
					break;
				default:
					throw new IOException( "wrong array value");
			}
		}
		while (loop);
		return arr;
	}

	private boolean doArrayNext() throws IOException {
		switch (mParser.nextToken()) {
			case COMMA:
				return true;
			case RIGHT_SQUARE:
				return false;
			default:
				throw new IOException( "wrong array close");
		}
	}

	private void doEOF() throws IOException {
		if (mParser.nextToken() != Token.EOF) {
			throw new IOException( "wrong eof");
		}
	}

	private FObject doObject() throws IOException {
		boolean loop = false;
		FObject obj = new FObject();
		do {
			switch (mParser.nextToken()) {
				case VALUE:
					String key = mParser.nextKey();
					doPairBegin();
					doPairValue( obj, key);
					loop = doObjectNext();
					break;
				case RIGHT_BRACE:
					loop = false;
					break;
				default:
					throw new IOException( "wrong object pair");
			}
		}
		while (loop);
		return obj;
	}

	private boolean doObjectNext() throws IOException {
		switch (mParser.nextToken()) {
			case COMMA:
				return true;
			case RIGHT_BRACE:
				return false;
			default:
				throw new IOException( "wrong object close");
		}
	}

	private void doPairBegin() throws IOException {
		if (mParser.nextToken() != Token.COLON) {
			throw new IOException( "missing colon");
		}
	}

	private void doPairValue( FObject obj, String key) throws IOException {
		switch (mParser.nextToken()) {
			case VALUE:
				obj.put( key, mParser.nextValue());
				break;
			case LEFT_BRACE:
				obj.put( key, doObject());
				break;
			case LEFT_SQUARE:
				obj.put( key, doArray());
				break;
			default:
				throw new IOException( "wrong pair value");
		}
	}
}
