package de.doerl.hqm.utils.nei;

import java.io.IOException;

import de.doerl.hqm.utils.Utils;

public class NEIReader {
	private Tokenizer mParser;

	public NEIReader( String line) {
		mParser = new Tokenizer( line);
	}

	private FArray doArray() throws IOException {
		boolean loop = false;
		FArray arr = new FArray();
		do {
			switch (mParser.nextToken()) {
				case COLON:
					mParser.nextValue();
					loop = doArrayValue( arr);
					break;
				case COMMA:
					String val = mParser.nextValue();
					if (Utils.validString( val)) {
						arr.add( new FValue( val));
					}
					loop = true;
					break;
				case RIGHT_BRACKET:
					arr.setByteArr( mParser.nextValue());
					loop = false;
					break;
				default:
					throw new IOException( "wrong array pair");
			}
		}
		while (loop);
		return arr;
	}

	private boolean doArrayNext() throws IOException {
		switch (mParser.nextToken()) {
			case COMMA:
				return true;
			case RIGHT_BRACKET:
				return false;
			default:
				throw new IOException( "wrong array close");
		}
	}

	private boolean doArrayValue( FArray arr) throws IOException {
		switch (mParser.nextToken()) {
			case COMMA:
				arr.add( new FValue( mParser.nextValue()));
				return true;
			case RIGHT_BRACKET:
				arr.add( new FValue( mParser.nextValue()));
				return false;
			case LEFT_CURLY:
				arr.add( doObject());
				return doArrayNext();
			case LEFT_BRACKET:
				arr.add( doArray());
				return doArrayNext();
			default:
				throw new IOException( "wrong pair value");
		}
	}

	public IJson doLine() throws IOException {
		switch (mParser.nextToken()) {
			case EOL:
				return new FValue( mParser.nextValue());
			case LEFT_CURLY:
				return doObject();
			case LEFT_BRACKET:
				return doArray();
			default:
				throw new IOException( "wrong json source");
		}
	}

	private FObject doObject() throws IOException {
		boolean loop = false;
		FObject obj = new FObject();
		do {
			switch (mParser.nextToken()) {
				case COLON:
					String key = mParser.nextValue();
					loop = doObjectValue( obj, key);
					break;
				case RIGHT_CURLY:
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
			case RIGHT_CURLY:
				return false;
			default:
				throw new IOException( "wrong object close");
		}
	}

	private boolean doObjectValue( FObject obj, String key) throws IOException {
		switch (mParser.nextToken()) {
			case COMMA:
				FValue vvv = new FValue( mParser.nextValue());
				obj.put( key, vvv);
				return true;
			case RIGHT_CURLY:
				FValue www = new FValue( mParser.nextValue());
				obj.put( key, www);
				return false;
			case LEFT_CURLY:
				obj.put( key, doObject());
				return doObjectNext();
			case LEFT_BRACKET:
				obj.put( key, doArray());
				return doObjectNext();
			default:
				throw new IOException( "wrong pair value");
		}
	}
}
