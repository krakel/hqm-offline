package de.doerl.hqm.utils;

public final class ToString {
	private static final char[] CLOSE_GROUPS = { ']', '}', '>', ')'
	};
	private static final char[] OPEN_GROUPS = { '[', '{', '<', '('
	};
	private int mFirst;
	private int mGroup;
	private StringBuilder mSB = new StringBuilder();

	public ToString( Object obj) {
		addOpenGroup();
		if (obj != null) {
			String name = obj.getClass().getName();
			mSB.append( name.substring( name.lastIndexOf( '.') + 1));
			mFirst = 1;
		}
		else {
//			mSB.append( "null"); //$NON-NLS-1$
		}
	}

	public static String clsName( Object obj) {
		ToString sb = new ToString( obj);
		return sb.toString();
	}

	private void addCloseGroup() {
		if (mGroup > 0) {
			--mGroup;
		}
		if (mGroup < CLOSE_GROUPS.length) {
			mSB.append( CLOSE_GROUPS[mGroup]);
		}
		else {
			mSB.append( CLOSE_GROUPS[CLOSE_GROUPS.length - 1]);
		}
	}

	private void addFirst() {
		switch (mFirst) {
			case 0:
				break;
			case 1:
				mSB.append( ':');
				break;
			default:
				mSB.append( ';');
		}
		mFirst = 2;
	}

	private void addOpenGroup() {
		if (mGroup < OPEN_GROUPS.length) {
			mSB.append( OPEN_GROUPS[mGroup]);
		}
		else {
			mSB.append( OPEN_GROUPS[OPEN_GROUPS.length - 1]);
		}
		++mGroup;
	}

	public synchronized ToString append( boolean b) {
		addFirst();
		mSB.append( b);
		return this;
	}

	public synchronized ToString append( char c) {
		addFirst();
		mSB.append( c);
		return this;
	}

	public synchronized ToString append( char str[]) {
		addFirst();
		mSB.append( str);
		return this;
	}

	public synchronized ToString append( char str[], int offset, int len) {
		addFirst();
		mSB.append( str, offset, len);
		return this;
	}

	public synchronized ToString append( double d) {
		addFirst();
		mSB.append( d);
		return this;
	}

	public synchronized ToString append( double d, int size) {
		return append( Double.toString( d), size);
	}

	public synchronized ToString append( float f) {
		addFirst();
		mSB.append( f);
		return this;
	}

	public synchronized ToString append( float f, int size) {
		return append( Float.toString( f), size);
	}

	public synchronized ToString append( int i) {
		addFirst();
		mSB.append( i);
		return this;
	}

	public synchronized ToString append( int i, int size) {
		return append( Integer.toString( i), size);
	}

	public synchronized ToString append( long l) {
		addFirst();
		mSB.append( l);
		return this;
	}

	public synchronized ToString append( long l, int size) {
		return append( Long.toString( l), size);
	}

	public synchronized ToString append( Object obj) {
		addFirst();
		mSB.append( String.valueOf( obj));
		return this;
	}

	public synchronized ToString append( String str) {
		addFirst();
		mSB.append( str);
		return this;
	}

	public synchronized ToString append( String s, int size) {
		addFirst();
		int len = s.length();
		while (len++ < size) {
			mSB.append( ' ');
		}
		mSB.append( s);
		return this;
	}

	public synchronized ToString append( StringBuilder sb) {
		addFirst();
		mSB.append( sb);
		return this;
	}

	public synchronized ToString appendMsg( Object obj) {
		mSB.append( obj);
		return this;
	}

	public synchronized ToString appendMsg( String msg) {
		mSB.append( msg);
		return this;
	}

	public synchronized ToString appendMsg( String msg, boolean b) {
		addFirst();
		mSB.append( msg);
		mSB.append( '=');
		mSB.append( b);
		return this;
	}

	public synchronized ToString appendMsg( String msg, char c) {
		addFirst();
		mSB.append( msg);
		mSB.append( '=');
		mSB.append( c);
		return this;
	}

	public synchronized ToString appendMsg( String msg, char str[]) {
		addFirst();
		mSB.append( msg);
		mSB.append( '=');
		mSB.append( str);
		return this;
	}

	public synchronized ToString appendMsg( String msg, char str[], int offset, int len) {
		addFirst();
		mSB.append( msg);
		mSB.append( '=');
		mSB.append( str, offset, len);
		return this;
	}

	public synchronized ToString appendMsg( String msg, double d) {
		addFirst();
		mSB.append( msg);
		mSB.append( '=');
		mSB.append( d);
		return this;
	}

	public synchronized ToString appendMsg( String msg, float f) {
		addFirst();
		mSB.append( msg);
		mSB.append( '=');
		mSB.append( f);
		return this;
	}

	public synchronized ToString appendMsg( String msg, int i) {
		addFirst();
		mSB.append( msg);
		mSB.append( '=');
		mSB.append( i);
		return this;
	}

	public synchronized ToString appendMsg( String msg, long l) {
		addFirst();
		mSB.append( msg);
		mSB.append( '=');
		mSB.append( l);
		return this;
	}

	public synchronized ToString appendMsg( String msg, Object obj) {
		addFirst();
		mSB.append( msg);
		mSB.append( '=');
		mSB.append( String.valueOf( obj));
		return this;
	}

	public synchronized ToString appendMsg( String msg, String str) {
		addFirst();
		mSB.append( msg);
		mSB.append( '=');
		mSB.append( str);
		return this;
	}

	public synchronized ToString appendMsg( String msg, StringBuilder sb) {
		addFirst();
		mSB.append( msg);
		mSB.append( '=');
		mSB.append( sb);
		return this;
	}

	public void closeGroup() {
		addCloseGroup();
		mFirst = 2;
	}

	public void openGroup() {
		addFirst();
		addOpenGroup();
		mFirst = 1;
	}

	@Override
	public String toString() {
		while (mGroup > 0) {
			addCloseGroup();
		}
		return mSB.toString();
	}
}
