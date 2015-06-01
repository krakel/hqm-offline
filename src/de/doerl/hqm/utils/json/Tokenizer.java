package de.doerl.hqm.utils.json;

import java.io.IOException;
import java.io.Reader;

class Tokenizer {
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
		while (true) {
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
			if (c != arr[i]) {
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
			mValue = new FValue( Escape.unescape( mBuffer.toString()));
			return Token.VALUE;
		}
	}
}
