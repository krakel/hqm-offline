package de.doerl.hqm.utils.nbt;

class Tokenizer {
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
				case '=':
					return Token.EQUAL;
				case ',':
					return Token.COMMA;
				case '(':
					return Token.LEFT_BRACKET;
				case ')':
					return Token.RIGHT_BRACKET;
				case 0:
					return Token.EOF;
				default:
			}
		}
	}

	public String nextValue() {
		return mIn.substring( mStart, mPos).trim();
	}
}
