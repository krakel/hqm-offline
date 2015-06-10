package de.doerl.hqm.utils.nei;

class Tokenizer {
	private int mBegin;
	private int mEnd;
	private int mPos;
	private String mLine;

	public Tokenizer( String line) {
		mLine = line;
	}

	private char getNext() {
		return mLine.charAt( mPos++);
	}

	public Token nextToken() {
		mBegin = mEnd = mPos;
		while (mPos < mLine.length()) {
			int c = getNext();
			switch (c) {
				case '\r':
				case '\n':
					break;
				case '"':
					readString();
					mEnd = mPos;
					break;
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
				case ':':
					return Token.COLON;
				default:
					mEnd = mPos;
			}
		}
		return Token.EOL;
	}

	public String nextValue() {
		String str = mLine.substring( mBegin, mEnd);
		if (str.startsWith( "\"\uFFFD")) {
			byte[] arr = str.getBytes();
			byte b1 = arr[1];
			b1 = (byte) (b1 + 0);
		}
		return str;
	}

	private void readString() {
		if (mPos < mLine.length()) {
			int l = 0;
			int c = getNext();
			while (mPos < mLine.length() && (l == '\\' || c != '"')) {
				l = c;
				c = getNext();
			}
		}
	}
}
