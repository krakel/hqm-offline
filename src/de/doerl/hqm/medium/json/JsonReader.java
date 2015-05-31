package de.doerl.hqm.medium.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import de.doerl.hqm.base.dispatch.IDispatcher;
import de.doerl.hqm.base.dispatch.IWorker;

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

	public static final class FArray implements IJson {
		ArrayList<IJson> mList = new ArrayList<>();

		public FArray() {
		}

		@Override
		public <T, U> T accept( IJsonWorker<T, U> w, U p) {
			return w.forArray( this, p);
		}

		public void add( IJson json) {
			mList.add( json);
		}
	}

	public static final class FObject implements IJson {
		HashMap<String, IJson> mMap = new HashMap<>();

		public FObject() {
		}

		public <T, U> T accept( IJsonWorker<T, U> w, U p) {
			return w.forObject( this, p);
		}

		public void add( String key, IJson value) {
			mMap.put( key, value);
		}

		public ArrayList<IJson> getArray( String key) {
			IJson json = mMap.get( key);
			if (json instanceof FArray) {
				return ((FArray) json).mList;
			}
			else {
				return null;
			}
		}

		public boolean getBoolean( String key) {
			Object obj = getValue( key);
			if (obj instanceof Boolean) {
				return ((Boolean) obj).booleanValue();
			}
			else {
				return false;
			}
		}

		public Double getDouble( String key) {
			Object obj = getValue( key);
			if (obj instanceof Double) {
				return (Double) obj;
			}
			else {
				return null;
			}
		}

		public int getInt( String key) {
			Integer obj = getInteger( key);
			if (obj != null) {
				return obj.intValue();
			}
			else {
				return 0;
			}
		}

		public Integer getInteger( String key) {
			Object obj = getValue( key);
			if (obj instanceof Integer) {
				return (Integer) obj;
			}
			else {
				return null;
			}
		}

		public FObject getObject( String key) {
			IJson json = mMap.get( key);
			if (json instanceof FObject) {
				return (FObject) json;
			}
			else {
				return null;
			}
		}

		public String getString( String key) {
			Object obj = getValue( key);
			if (obj != null) {
				return String.valueOf( obj);
			}
			else {
				return null;
			}
		}

		public Object getValue( String key) {
			IJson json = mMap.get( key);
			if (json instanceof FValue) {
				return ((FValue) json).mObj;
			}
			else {
				return null;
			}
		}
	}

	public static final class FValue implements IJson {
		Object mObj;

		public FValue( Object obj) {
			mObj = obj;
		}

		public <T, U> T accept( IJsonWorker<T, U> w, U p) {
			return w.forValue( this, p);
		}

		@Override
		public String toString() {
			return String.valueOf( mObj);
		}
	}

	public static interface IJson extends IDispatcher {
		<T, U> T accept( IJsonWorker<T, U> w, U p);
	}

	public static interface IJsonWorker<T, U> extends IWorker {
		T forArray( FArray arr, U p);

		T forObject( FObject obj, U p);

		T forValue( FValue val, U p);
	}

	public static class JSONofArray implements IJsonWorker<FArray, Object> {
		private static final JSONofArray WORKER = new JSONofArray();

		private JSONofArray() {
		}

		public static FArray get( IJson json) {
			return json.accept( WORKER, null);
		}

		@Override
		public FArray forArray( FArray arr, Object p) {
			return arr;
		}

		@Override
		public FArray forObject( FObject obj, Object p) {
			return null;
		}

		@Override
		public FArray forValue( FValue val, Object p) {
			return null;
		}
	}

	public static class JSONofObject implements IJsonWorker<FObject, Object> {
		private static final JSONofObject WORKER = new JSONofObject();

		private JSONofObject() {
		}

		public static FObject get( IJson json) {
			return json.accept( WORKER, null);
		}

		@Override
		public FObject forArray( FArray arr, Object p) {
			return null;
		}

		@Override
		public FObject forObject( FObject obj, Object p) {
			return obj;
		}

		@Override
		public FObject forValue( FValue val, Object p) {
			return null;
		}
	}

	public static class JSONofValue implements IJsonWorker<FValue, Object> {
		private static final JSONofValue WORKER = new JSONofValue();

		private JSONofValue() {
		}

		public static FValue get( IJson json) {
			return json.accept( WORKER, null);
		}

		public static int getInt( IJson json) {
			Integer obj = getInteger( json);
			if (obj != null) {
				return obj.intValue();
			}
			else {
				return 0;
			}
		}

		public static Integer getInteger( IJson json) {
			Object obj = get( json);
			if (obj instanceof Integer) {
				return (Integer) obj;
			}
			else {
				return null;
			}
		}

		@Override
		public FValue forArray( FArray arr, Object p) {
			return null;
		}

		@Override
		public FValue forObject( FObject obj, Object p) {
			return null;
		}

		@Override
		public FValue forValue( FValue val, Object p) {
			return val;
		}
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
