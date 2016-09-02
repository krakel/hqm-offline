package de.doerl.hqm.utils.nbt;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.Utils;

public class ParserAtNEI {
	private static final Logger LOGGER = Logger.getLogger( ParserAtNEI.class.getName());
	private Tokenizer mParser;

	public ParserAtNEI( String src) {
		mParser = new Tokenizer( src);
	}

	public static FCompound parse( String src) {
		if (Utils.validString( src)) {
			try {
				return parse0( src);
			}
			catch (IOException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return null;
	}

	static FCompound parse0( String src) throws IOException {
		ParserAtNEI wrt = new ParserAtNEI( src);
		return wrt.doAll();
	}

	private FCompound doAll() throws IOException {
		switch (mParser.nextToken()) {
			case EOL:
				return new FCompound( "");
			case LEFT_CURLY:
				return doCompound( "");
			default:
				throw new IOException( "wrong nbt source");
		}
	}

	private ANbt doArray( String name) throws IOException {
		switch (mParser.nextToken()) {
			case COLON:
				mParser.nextValue(); // ignore index
				return doArrayList( name);
			case COMMA:
				String first = mParser.nextValue();
				if (first.endsWith( "b") || first.endsWith( "B")) {
					return doArrayByte( name, first);
				}
				else {
					return doArrayInteger( name, first);
				}
			case RIGHT_BRACKET:
				String bytes = mParser.nextValue();
				if (bytes.endsWith( "bytes")) {
					return new FByteArray( name);
				}
				else {
					return new FIntArray( name);
				}
			default:
				throw new IOException( "wrong array pair");
		}
	}

	private FByteArray doArrayByte( String name, String first) throws IOException {
		FByteArray arr = new FByteArray( name);
		arr.add( Byte.parseByte( first.substring( 0, first.length() - 1)));
		boolean loop = false;
		do {
			switch (mParser.nextToken()) {
				case COLON:
					arr.add( Byte.parseByte( mParser.nextValue().substring( 0, first.length() - 1)));
					loop = true;
					break;
				case RIGHT_BRACKET:
					arr.add( Byte.parseByte( mParser.nextValue().substring( 0, first.length() - 1)));
					loop = false;
					break;
				default:
					throw new IOException( "wrong object pair");
			}
		}
		while (loop);
		return arr;
	}

	private FIntArray doArrayInteger( String name, String first) throws IOException {
		FIntArray arr = new FIntArray( name);
		arr.add( Integer.parseInt( first));
		boolean loop = false;
		do {
			switch (mParser.nextToken()) {
				case COLON:
					arr.add( Integer.parseInt( mParser.nextValue()));
					loop = true;
					break;
				case RIGHT_BRACKET:
					arr.add( Integer.parseInt( mParser.nextValue()));
					loop = false;
					break;
				default:
					throw new IOException( "wrong object pair");
			}
		}
		while (loop);
		return arr;
	}

	private ANbt doArrayList( String name) throws IOException {
		FList lst = new FList( name);
		boolean loop = false;
		do {
			switch (mParser.nextToken()) {
				case COMMA:
					lst.add( doValue( "", mParser.nextValue()));
					doArrayListNext( lst);
					loop = true;
					break;
				case LEFT_BRACKET:
					lst.add( doArray( ""));
					loop = true;
					break;
				case RIGHT_BRACKET:
					lst.add( doValue( "", mParser.nextValue()));
					loop = false;
					break;
				case LEFT_CURLY:
					lst.add( doCompound( ""));
					loop = true;
					break;
				default:
					throw new IOException( "wrong array close");
			}
		}
		while (loop);
		return lst;
	}

	private void doArrayListNext( FList lst) throws IOException {
		switch (mParser.nextToken()) {
			case COLON:
				mParser.nextValue(); // ignore index
			default:
				throw new IOException( "wrong array close");
		}
	}

	private FCompound doCompound( String name) throws IOException {
		FCompound obj = new FCompound( name);
		boolean loop = false;
		do {
			switch (mParser.nextToken()) {
				case COLON:
					loop = doCompoundValue( obj, mParser.nextValue());
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

	private boolean doCompoundNext() throws IOException {
		switch (mParser.nextToken()) {
			case COMMA:
				return true;
			case RIGHT_CURLY:
				return false;
			default:
				throw new IOException( "wrong object close");
		}
	}

	private boolean doCompoundValue( FCompound obj, String name) throws IOException {
		switch (mParser.nextToken()) {
			case COMMA:
				obj.add( doValue( name, mParser.nextValue()));
				return true;
			case LEFT_BRACKET:
				obj.add( doArray( name));
				return doCompoundNext();
			case LEFT_CURLY:
				obj.add( doCompound( name));
				return doCompoundNext();
			case RIGHT_CURLY:
				obj.add( doValue( name, mParser.nextValue()));
				return false;
			default:
				throw new IOException( "wrong pair value");
		}
	}

	private ANbt doValue( String name, String val) {
		int len = val.length() - 1;
		char last = len < 0 ? ' ' : val.charAt( len);
		switch (last) {
			case '"':
				return FString.create( name, len >= 1 ? val.substring( 1, len) : ""); // TODO remove \\\\"
			case 'f':
			case 'F':
				return FDouble.createFloat( name, Float.parseFloat( val.substring( 0, len)));
			case 'd':
			case 'D':
				return FDouble.createDouble( name, Double.parseDouble( val.substring( 0, len)));
			case 'b':
			case 'B':
				return FLong.createByte( name, Byte.parseByte( val.substring( 0, len)));
			case 's':
			case 'S':
				return FLong.createShort( name, Short.parseShort( val.substring( 0, len)));
			case 'l':
			case 'L':
				return FLong.createLong( name, Long.parseLong( val.substring( 0, len)));
			default:
				if (val.indexOf( '.') >= 0) {
					return FDouble.createDouble( name, Double.parseDouble( val));
				}
				else {
					return FLong.createInt( name, Integer.parseInt( val));
				}
		}
	}

	private static enum Token {
		EOL,
		COMMA,
		COLON,
		LEFT_BRACKET,
		RIGHT_BRACKET,
		LEFT_CURLY,
		RIGHT_CURLY;
	}

	private static class Tokenizer {
		private String mIn;
		private int mStart, mPos;

		public Tokenizer( String in) {
			mIn = in;
		}

		private char getNext() {
			if (mPos < mIn.length()) {
				return mIn.charAt( mPos++);
			}
			else {
				return 0;
			}
		}

		public Token nextToken() {
			mStart = mPos;
			while (true) {
				char c = getNext();
				switch (c) {
					case '"':
						readString();
						break;
					case ':':
						return Token.COLON;
					case ',':
						return Token.COMMA;
					case '[':
						return Token.LEFT_BRACKET;
					case ']':
						return Token.RIGHT_BRACKET;
					case '{':
						return Token.LEFT_CURLY;
					case '}':
						return Token.RIGHT_CURLY;
					case 0:
						return Token.EOL;
					default:
				}
			}
		}

		public String nextValue() {
			return mIn.substring( mStart, mPos - 1).trim();
		}

		private void readString() {
			int l = 0;
			int c = getNext();
			while (c >= 0 && (l == '\\' || c != '"')) {
				l = c;
				c = getNext();
			}
		}
	}
}
