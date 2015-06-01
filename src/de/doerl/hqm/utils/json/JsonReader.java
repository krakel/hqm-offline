package de.doerl.hqm.utils.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class JsonReader {
	private JsonReader() {
	}

	private static FArray doArray( Tokenizer parser) throws IOException {
		boolean loop = false;
		FArray arr = new FArray();
		do {
			switch (parser.nextToken()) {
				case VALUE:
					arr.add( parser.nextValue());
					loop = doArrayNext( parser);
					break;
				case LEFT_BRACE:
					arr.add( doObject( parser));
					loop = doArrayNext( parser);
					break;
				case LEFT_SQUARE:
					arr.add( doArray( parser));
					loop = doArrayNext( parser);
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

	private static boolean doArrayNext( Tokenizer parser) throws IOException {
		switch (parser.nextToken()) {
			case COMMA:
				return true;
			case RIGHT_SQUARE:
				return false;
			default:
				throw new IOException( "wrong array close");
		}
	}

	private static void doEOF( Tokenizer parser) throws IOException {
		if (parser.nextToken() != Token.EOF) {
			throw new IOException( "wrong eof");
		}
	}

	private static FObject doObject( Tokenizer parser) throws IOException {
		boolean loop = false;
		FObject obj = new FObject();
		do {
			switch (parser.nextToken()) {
				case VALUE:
					String key = parser.nextKey();
					doPairBegin( parser);
					doPairValue( parser, obj, key);
					loop = doObjectNext( parser);
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

	private static boolean doObjectNext( Tokenizer parser) throws IOException {
		switch (parser.nextToken()) {
			case COMMA:
				return true;
			case RIGHT_BRACE:
				return false;
			default:
				throw new IOException( "wrong object close");
		}
	}

	private static void doPairBegin( Tokenizer parser) throws IOException {
		if (parser.nextToken() != Token.COLON) {
			throw new IOException( "missing colon");
		}
	}

	private static void doPairValue( Tokenizer parser, FObject obj, String key) throws IOException {
		switch (parser.nextToken()) {
			case VALUE:
				obj.put( key, parser.nextValue());
				break;
			case LEFT_BRACE:
				obj.put( key, doObject( parser));
				break;
			case LEFT_SQUARE:
				obj.put( key, doArray( parser));
				break;
			default:
				throw new IOException( "wrong pair value");
		}
	}

	private static IJson read( BufferedReader in) throws IOException {
		try {
			Tokenizer parser = new Tokenizer( in);
			switch (parser.nextToken()) {
				case EOF:
					return new FValue( "");
				case VALUE:
					FValue val = parser.nextValue();
					doEOF( parser);
					return val;
				case LEFT_BRACE:
					FObject obj = doObject( parser);
					doEOF( parser);
					return obj;
				case LEFT_SQUARE:
					FArray arr = doArray( parser);
					doEOF( parser);
					return arr;
				default:
					throw new IOException( "wrong json source");
			}
		}
		finally {
			try {
				in.close();
			}
			catch (IOException ex) {
			}
		}
	}

	public static IJson read( InputStream in) throws IOException {
		return read( new InputStreamReader( in, "UTF-8"));
	}

	public static IJson read( Reader in) throws IOException {
		return read( new BufferedReader( in));
	}
}
