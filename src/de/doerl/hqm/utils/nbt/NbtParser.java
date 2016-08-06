package de.doerl.hqm.utils.nbt;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.utils.Utils;

public class NbtParser {
	private static final Logger LOGGER = Logger.getLogger( NbtParser.class.getName());
	private Tokenizer mParser;

	public NbtParser( String src) {
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
		NbtParser wrt = new NbtParser( src);
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
		boolean loop = false;
		do {
			switch (mParser.nextToken()) {
				case COMMA:
					arr.add( Byte.parseByte( mParser.nextValue()));
					loop = true;
					break;
				case RIGHT_BRACKET:
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
		while (loop);
		return arr;
	}

	private FIntArray doArrayInt( String name) throws IOException {
		FIntArray arr = new FIntArray( name);
		boolean loop = false;
		do {
			switch (mParser.nextToken()) {
				case COMMA:
					arr.add( Integer.parseInt( mParser.nextValue()));
					loop = true;
					break;
				case RIGHT_BRACKET:
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
		while (loop);
		return arr;
	}

	private FCompound doCompound( String name) throws IOException {
		FCompound comp = new FCompound( name);
		boolean loop = false;
		do {
			switch (mParser.nextToken()) {
				case EQUAL:
					comp.add( doPair());
					loop = doNext();
					break;
				case RIGHT_BRACKET:
					loop = false;
					break;
				default:
					throw new IOException( "wrong start pair");
			}
		}
		while (loop);
		return comp;
	}

	private void doEOF() throws IOException {
		if (mParser.nextToken() != Token.EOF) {
			throw new IOException( "wrong eof");
		}
	}

	private FList doList( String name) throws IOException {
		FList lst = new FList( name);
		boolean loop = false;
		do {
			switch (mParser.nextToken()) {
				case LEFT_BRACKET:
					int tag = getTag();
					switch (tag) {
						case 7: // Byte-Array
							lst.add( doArrayByte( ""));
							break;
						case 9: // List
							lst.add( doList( ""));
							break;
						case 10: // Compound
							lst.add( doCompound( ""));
							break;
						case 11: // Int-Array
							lst.add( doArrayInt( ""));
							break;
						default:
							lst.add( doValueEnd( "", tag));
					}
					loop = doNext(); // COMMA
					break;
				case RIGHT_BRACKET:
					loop = false;
					break;
				default:
					throw new IOException( "wrong byte array");
			}
		}
		while (loop);
		return lst;
	}

	private boolean doNext() throws IOException {
		switch (mParser.nextToken()) {
			case COMMA:
				return true;
			case RIGHT_BRACKET:
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
			case 7: // Byte-Array
				return doArrayByte( name);
			case 9: // List
				return doList( name);
			case 10: // Compound
				return doCompound( name);
			case 11: // Int-Array
				return doArrayInt( name);
			default:
				return doValueEnd( name, tag);
		}
	}

	private ANbt doValue( String name, int tag, String val) throws IOException {
		switch (tag) {
			case 1: // Byte
				return FLong.createByte( name, Byte.parseByte( val));
			case 2: // Short
				return FLong.createShort( name, Short.parseShort( val));
			case 3: // Int
				return FLong.createInt( name, Integer.parseInt( val));
			case 4: // Long
				return FLong.createLong( name, Long.parseLong( val));
			case 5: // Float
				return FDouble.createFloat( name, Float.parseFloat( val));
			case 6: // Double
				return FDouble.createDouble( name, Double.parseDouble( val));
			case 8: // String
				int len = val.length();
				return FString.create( name, len >= 2 ? val.substring( 1, len - 1) : "");
			default:
				throw new IOException( "wrong value tag");
		}
	}

	private int doValueBegin() throws IOException {
		if (mParser.nextToken() == Token.LEFT_BRACKET) {
			return getTag();
		}
		else {
			throw new IOException( "wrong start value");
		}
	}

	private ANbt doValueEnd( String name, int tag) throws IOException {
		if (mParser.nextToken() == Token.RIGHT_BRACKET) {
			return doValue( name, tag, mParser.nextValue());
		}
		else {
			throw new IOException( "wrong end value");
		}
	}

	private int getTag() throws IOException {
		String key = mParser.nextValue();
		if ("END".equals( key)) {
			return 0;
		}
		if ("BYTE".equals( key)) {
			return 1;
		}
		if ("SHORT".equals( key)) {
			return 2;
		}
		if ("INT".equals( key)) {
			return 3;
		}
		if ("LONG".equals( key)) {
			return 4;
		}
		if ("FLOAT".equals( key)) {
			return 5;
		}
		if ("DOUBLE".equals( key)) {
			return 6;
		}
		if ("BYTE-ARRAY".equals( key)) {
			return 7;
		}
		if ("STRING".equals( key)) {
			return 8;
		}
		if ("LIST".equals( key)) {
			return 9;
		}
		if ("COMPOUND".equals( key)) {
			return 10;
		}
		if ("INT-ARRAY".equals( key)) {
			return 11;
		}
		throw new IOException( "wrong nbt type");
	}
}
