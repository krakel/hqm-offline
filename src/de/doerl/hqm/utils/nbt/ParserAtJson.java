package de.doerl.hqm.utils.nbt;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.Utils;

public class ParserAtJson {
	private static final Logger LOGGER = Logger.getLogger( ParserAtJson.class.getName());
	private Tokenizer mParser;

	public ParserAtJson( String src) {
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
		ParserAtJson wrt = new ParserAtJson( src);
		return wrt.doAll();
	}

	private FCompound doAll() throws IOException {
		switch (mParser.nextToken()) {
			case EOF:
				return new FCompound( "");
			case EQUAL:
				FCompound main = (FCompound) doPair();
				doEOF();
				return main;
			default:
				throw new IOException( "wrong nbt source");
		}
	}

	private FByteArray doArrayByte( String name) throws IOException {
		FByteArray arr = new FByteArray( name);
		boolean loop = true;
		while (loop) {
			switch (mParser.nextToken()) {
				case COMMA:
					arr.add( Byte.parseByte( mParser.nextValue()));
					loop = true;
					break;
				case RIGHT_PARENTHESIS:
					String val = mParser.nextValue();
					if (!"".equals( val)) {
						arr.add( Byte.parseByte( val));
					}
					loop = false;
					break;
				default:
					throw new IOException( "wrong byte array");
			}
		}
		return arr;
	}

	private FIntArray doArrayInt( String name) throws IOException {
		FIntArray arr = new FIntArray( name);
		boolean loop = true;
		while (loop) {
			switch (mParser.nextToken()) {
				case COMMA:
					arr.add( Integer.parseInt( mParser.nextValue()));
					loop = true;
					break;
				case RIGHT_PARENTHESIS:
					loop = false;
					String val = mParser.nextValue();
					if (!"".equals( val)) {
						arr.add( Integer.parseInt( val));
					}
					break;
				default:
					throw new IOException( "wrong byte array");
			}
		}
		return arr;
	}

	private FCompound doCompound( String name) throws IOException {
		FCompound comp = new FCompound( name);
		boolean loop = true;
		while (loop) {
			switch (mParser.nextToken()) {
				case EQUAL:
					comp.add( doPair());
					loop = doNext();
					break;
				case RIGHT_PARENTHESIS:
					loop = false;
					break;
				default:
					throw new IOException( "wrong start pair");
			}
		}
		return comp;
	}

	private void doEOF() throws IOException {
		if (mParser.nextToken() != Token.EOF) {
			throw new IOException( "wrong eof");
		}
	}

	private FList doList( String name) throws IOException {
		FList lst = new FList( name);
		boolean loop = true;
		while (loop) {
			switch (mParser.nextToken()) {
				case LEFT_PARENTHESIS:
					int tag = getTag();
					switch (tag) {
						case ANbt.ID_BYTE_ARRAY:
							lst.add( doArrayByte( ""));
							break;
						case ANbt.ID_LIST:
							lst.add( doList( ""));
							break;
						case ANbt.ID_COMPOUND:
							lst.add( doCompound( ""));
							break;
						case ANbt.ID_INT_ARRAY:
							lst.add( doArrayInt( ""));
							break;
						default:
							lst.add( doValueEnd( "", tag));
					}
					loop = doNext(); // COMMA
					break;
				case RIGHT_PARENTHESIS:
					loop = false;
					break;
				default:
					throw new IOException( "wrong byte array");
			}
		}
		return lst;
	}

	private boolean doNext() throws IOException {
		switch (mParser.nextToken()) {
			case COMMA:
				return true;
			case RIGHT_PARENTHESIS:
			case EOF:
				return false;
			default:
				throw new IOException( "wrong end pair");
		}
	}

	private ANbt doPair() throws IOException {
		String name = mParser.nextValue();
		int tag = doValueBegin();
		switch (tag) {
			case ANbt.ID_BYTE_ARRAY:
				return doArrayByte( name);
			case ANbt.ID_LIST:
				return doList( name);
			case ANbt.ID_COMPOUND:
				return doCompound( name);
			case ANbt.ID_INT_ARRAY:
				return doArrayInt( name);
			default:
				return doValueEnd( name, tag);
		}
	}

	private ANbt doValue( String name, int tag, String val) throws IOException {
		switch (tag) {
			case ANbt.ID_BYTE:
				return FLong.createByte( name, Byte.parseByte( val));
			case ANbt.ID_SHORT:
				return FLong.createShort( name, Short.parseShort( val));
			case ANbt.ID_INT:
				return FLong.createInt( name, Integer.parseInt( val));
			case ANbt.ID_LONG:
				return FLong.createLong( name, Long.parseLong( val));
			case ANbt.ID_FLOAT:
				return FDouble.createFloat( name, Float.parseFloat( val));
			case ANbt.ID_DOUBLE:
				return FDouble.createDouble( name, Double.parseDouble( val));
			case ANbt.ID_STRING:
				int len = val.length();
				return FString.create( name, len >= 2 ? val.substring( 1, len - 1) : "");
			default:
				throw new IOException( "wrong value tag");
		}
	}

	private int doValueBegin() throws IOException {
		if (mParser.nextToken() == Token.LEFT_PARENTHESIS) {
			return getTag();
		}
		else {
			throw new IOException( "wrong start value");
		}
	}

	private ANbt doValueEnd( String name, int tag) throws IOException {
		if (mParser.nextToken() == Token.RIGHT_PARENTHESIS) {
			return doValue( name, tag, mParser.nextValue());
		}
		else {
			throw new IOException( "wrong end value");
		}
	}

	private int getTag() throws IOException {
		String key = mParser.nextValue();
		if ("END".equals( key)) {
			return ANbt.ID_END;
		}
		if ("BYTE".equals( key)) {
			return ANbt.ID_BYTE;
		}
		if ("SHORT".equals( key)) {
			return ANbt.ID_SHORT;
		}
		if ("INT".equals( key)) {
			return ANbt.ID_INT;
		}
		if ("LONG".equals( key)) {
			return ANbt.ID_LONG;
		}
		if ("FLOAT".equals( key)) {
			return ANbt.ID_FLOAT;
		}
		if ("DOUBLE".equals( key)) {
			return ANbt.ID_DOUBLE;
		}
		if ("BYTE-ARRAY".equals( key)) {
			return ANbt.ID_BYTE_ARRAY;
		}
		if ("STRING".equals( key)) {
			return ANbt.ID_STRING;
		}
		if ("LIST".equals( key)) {
			return ANbt.ID_LIST;
		}
		if ("COMPOUND".equals( key)) {
			return ANbt.ID_COMPOUND;
		}
		if ("INT-ARRAY".equals( key)) {
			return ANbt.ID_INT_ARRAY;
		}
		throw new IOException( "wrong nbt type");
	}

	private static enum Token {
		EOF,
		COMMA,
		EQUAL,
		LEFT_PARENTHESIS,
		RIGHT_PARENTHESIS;
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
					case '\'':
						readString();
						break;
					case '=':
						return Token.EQUAL;
					case ',':
						return Token.COMMA;
					case '(':
						return Token.LEFT_PARENTHESIS;
					case ')':
						return Token.RIGHT_PARENTHESIS;
					case 0:
						return Token.EOF;
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
			while (c >= 0 && (l == '\\' || c != '\'')) {
				l = c;
				c = getNext();
			}
		}
	}
}
