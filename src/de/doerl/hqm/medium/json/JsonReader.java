package de.doerl.hqm.medium.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class JsonReader {
	private static final Charset UTF_8 = Charset.forName( "UTF-8");

	private JsonReader() {
	}

	private static FArray doArray( Tokenizer parser, FArray arr) throws IOException {
		switch (parser.nextToken()) {
			case VALUE:
				arr.add( parser.nextValue());
				return doArrayNext( parser, arr);
			case LEFT_BRACE:
				arr.add( doObject( parser, new FObject()));
				return doArrayNext( parser, arr);
			case LEFT_SQUARE:
				arr.add( doArray( parser, new FArray()));
				return doArrayNext( parser, arr);
			case RIGHT_SQUARE:
				return arr;
			default:
				throw new IOException( "wrong array value");
		}
	}

	private static FArray doArrayNext( Tokenizer parser, FArray arr) throws IOException {
		switch (parser.nextToken()) {
			case COMMA:
				return doArray( parser, arr);
			case RIGHT_SQUARE:
				return arr;
			default:
				throw new IOException( "wrong array close");
		}
	}

	private static IJson doEOF( Tokenizer parser, IJson json) throws IOException {
		switch (parser.nextToken()) {
			case EOF:
				return json;
			default:
				throw new IOException( "wrong eof");
		}
	}

	private static FObject doObject( Tokenizer parser, FObject obj) throws IOException {
		switch (parser.nextToken()) {
			case VALUE:
				return doPairKey( parser, obj, parser.nextKey());
			case RIGHT_BRACE:
				return obj;
			default:
				throw new IOException( "wrong object value");
		}
	}

	private static FObject doObjectNext( Tokenizer parser, FObject obj) throws IOException {
		switch (parser.nextToken()) {
			case COMMA:
				return doObject( parser, obj);
			case RIGHT_BRACE:
				return obj;
			default:
				throw new IOException( "wrong object close");
		}
	}

	private static FObject doPairKey( Tokenizer parser, FObject obj, String key) throws IOException {
		switch (parser.nextToken()) {
			case COLON:
				return doPairValue( parser, obj, key);
			default:
				throw new IOException( "missing colon");
		}
	}

	private static FObject doPairValue( Tokenizer parser, FObject obj, String key) throws IOException {
		switch (parser.nextToken()) {
			case VALUE:
				obj.add( key, parser.nextValue());
				return doObjectNext( parser, obj);
			case LEFT_BRACE:
				obj.add( key, doObject( parser, new FObject()));
				return doObjectNext( parser, obj);
			case LEFT_SQUARE:
				obj.add( key, doArray( parser, new FArray()));
				return doObjectNext( parser, obj);
			default:
				throw new IOException( "wrong pair value");
		}
	}

	private static IJson get( BufferedReader in) throws IOException {
		try {
			Tokenizer parser = new Tokenizer( in);
			switch (parser.nextToken()) {
				case EOF:
					return new FValue( "");
				case VALUE:
					return doEOF( parser, parser.nextValue());
				case LEFT_BRACE:
					return doEOF( parser, doObject( parser, new FObject()));
				case LEFT_SQUARE:
					return doEOF( parser, doArray( parser, new FArray()));
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

	public static IJson get( InputStream in) throws IOException {
		return get( new InputStreamReader( in, UTF_8));
	}

	public static IJson get( Reader in) throws IOException {
		return get( new BufferedReader( in));
	}

	public static final class FArray implements IJson, Iterable<IJson> {
		private ArrayList<IJson> mList = new ArrayList<>();

		FArray() {
		}

		static FArray get( IJson json) {
			if (json instanceof FArray) {
				return (FArray) json;
			}
			else {
				return null;
			}
		}

		public void add( IJson json) {
			mList.add( json);
		}

		public IJson get( int idx) {
			return mList.get( idx);
		}

		@Override
		public Iterator<IJson> iterator() {
			return mList.iterator();
		}

		public int size() {
			return mList.size();
		}
	}

	public static final class FObject implements IJson {
		private HashMap<String, IJson> mMap = new HashMap<>();

		FObject() {
		}

		static FObject get( IJson json) {
			if (json instanceof FObject) {
				return (FObject) json;
			}
			else {
				return null;
			}
		}

		public void add( String key, IJson value) {
			mMap.put( key, value);
		}

		IJson getJson( String key) {
			return mMap.get( key);
		}
	}

	public static final class FValue implements IJson {
		private Object mObj;

		FValue( Object obj) {
			mObj = obj;
		}

		static FValue get( IJson json) {
			if (json instanceof FValue) {
				return (FValue) json;
			}
			else {
				return null;
			}
		}

		static boolean toBoolean( IJson json) {
			Object obj = toValueObj( json);
			if (obj instanceof Boolean) {
				return (Boolean) obj;
			}
			else {
				return false;
			}
		}

		static Double toDouble( IJson json) {
			Object obj = toValueObj( json);
			if (obj instanceof Double) {
				return (Double) obj;
			}
			else {
				return null;
			}
		}

		static int toInt( IJson json) {
			Integer obj = toInteger( json);
			if (obj != null) {
				return obj.intValue();
			}
			else {
				return 0;
			}
		}

		static Integer toInteger( IJson json) {
			Object obj = toValueObj( json);
			if (obj instanceof Integer) {
				return (Integer) obj;
			}
			else {
				return null;
			}
		}

		static String toString( IJson json) {
			Object obj = toValueObj( json);
			if (obj != null) {
				return String.valueOf( obj);
			}
			else {
				return null;
			}
		}

		static Object toValueObj( IJson json) {
			if (json instanceof FValue) {
				return ((FValue) json).mObj;
			}
			else {
				return null;
			}
		}

		@Override
		public String toString() {
			return String.valueOf( mObj);
		}
	}

	public static interface IJson {
	}

	private static enum Token {
		EOF,
		COLON,
		COMMA,
		LEFT_BRACE,
		RIGHT_BRACE,
		LEFT_SQUARE,
		RIGHT_SQUARE,
		VALUE,
		ERROR;
	}

	private static class Tokenizer {
		private static final FValue VAL_NULL = new FValue( null);
		private static final FValue VAL_TRUE = new FValue( Boolean.TRUE);
		private static final FValue VAL_FALSE = new FValue( Boolean.FALSE);
		private static final char[] ARR_NULL = "null".toCharArray();
		private static final char[] ARR_TRUE = "true".toCharArray();
		private static final char[] ARR_FALSE = "false".toCharArray();
		private StringBuffer mBuffer = new StringBuffer();
		private int mBack = -1;
		private Reader mIn;
		private FValue mValue;

		public Tokenizer( Reader in) {
			mIn = in;
		}

		private int getNext() throws IOException {
			if (mBack < 0) {
				return mIn.read();
			}
			else {
				int b = mBack;
				mBack = -1;
				return b;
			}
		}

		public String nextKey() {
			return mBuffer.toString();
		}

		public Token nextToken() throws IOException {
			mBuffer.setLength( 0);
			mValue = null;
			for (;;) {
				int c = getNext();
				switch (c) {
					case ' ':
					case '\b':
					case '\f':
					case '\r':
					case '\n':
					case '\t':
						break;
					case ':':
						return Token.COLON;
					case ',':
						return Token.COMMA;
					case '[':
						return Token.LEFT_SQUARE;
					case ']':
						return Token.RIGHT_SQUARE;
					case '{':
						return Token.LEFT_BRACE;
					case '}':
						return Token.RIGHT_BRACE;
					case '"':
						return readString();
					case 'n':
						return readMatch( ARR_NULL, VAL_NULL);
					case 't':
						return readMatch( ARR_TRUE, VAL_TRUE);
					case 'f':
						return readMatch( ARR_FALSE, VAL_FALSE);
					case '-':
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
						return readNumber( c);
					case -1:
						return Token.EOF;
					default:
						return Token.ERROR;
				}
			}
		}

		public FValue nextValue() {
			return mValue;
		}

		private Token readMatch( char[] arr, FValue val) throws IOException {
			for (int i = 1; i < arr.length; ++i) {
				int c = getNext();
				if (c < 0 || c != arr[i]) {
					return Token.ERROR;
				}
			}
			mValue = val;
			return Token.VALUE;
		}

		private Token readNumber( int c) throws IOException {
			boolean isDouble = false;
			mBuffer.append( (char) c);
			c = getNext();
			while (c >= 0) {
				boolean inNumber = true;
				switch (c) {
					case 'E':
					case 'e':
					case '.':
						isDouble = true;
					case '+':
					case '-':
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
						mBuffer.append( (char) c);
						break;
					default:
						mBack = c;
						inNumber = false;
				}
				if (inNumber) {
					c = getNext();
				}
				else {
					break;
				}
			}
			try {
				if (c < 0) {
					return Token.ERROR;
				}
				else if (isDouble) {
					mValue = new FValue( Double.valueOf( mBuffer.toString()));
					return Token.VALUE;
				}
				else {
					mValue = new FValue( Integer.valueOf( mBuffer.toString()));
					return Token.VALUE;
				}
			}
			catch (NumberFormatException ex) {
				return Token.ERROR;
			}
		}

		private Token readString() throws IOException {
			int l = 0;
			int c = getNext();
			while (c >= 0 && (l == '\\' || c != '"')) {
				l = c;
				mBuffer.append( (char) c);
				c = getNext();
			}
			if (c < 0) {
				return Token.ERROR;
			}
			else {
				mValue = new FValue( unescape( mBuffer.toString()));
				return Token.VALUE;
			}
		}

		private Object unescape( String s) {
			StringBuffer sb = new StringBuffer();
			int len = s.length();
			boolean inEscape = false;
			int inUnicode = 0;
			for (int i = 0; i < len; ++i) {
				char c = s.charAt( i);
				if (inUnicode > 0) {
					--inUnicode;
					if (inUnicode == 0) {
						sb.append( (char) Integer.parseInt( s.substring( i - 3, i + 1), 16));
					}
				}
				else if (inEscape) {
					switch (c) {
						case 'b':
							sb.append( '\b');
							break;
						case 'f':
							sb.append( '\f');
							break;
						case 'n':
							sb.append( '\n');
							break;
						case 'r':
							sb.append( '\r');
							break;
						case 't':
							sb.append( '\t');
							break;
						case 'u':
							inUnicode = 4;
							break;
						default:
							sb.append( c);
							break;
					}
					inEscape = false;
				}
				else if (c == '\\') {
					inEscape = true;
				}
				else {
					sb.append( c);
				}
			}
			return sb.toString();
		}
	}
}
