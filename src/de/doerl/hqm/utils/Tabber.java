package de.doerl.hqm.utils;

public class Tabber {
	private boolean mNL;
	private boolean mComma;
	private int[] mTabs = {
		0
	};
	private String mText = "";

	public void dec() {
		if (mTabs[0] > 0) {
			mTabs[0]--;
			set();
		}
		setNLBlock();
	}

	public void inc() {
		mTabs[0]++;
		set();
		setNLBlock();
	}

	public boolean isComma() {
		return mComma;
	}

	public boolean isNL() {
		return mNL;
	}

	public void pop() {
		if (mTabs.length > 1) {
			int[] old = mTabs;
			mTabs = new int[old.length - 1];
			System.arraycopy( old, 1, mTabs, 0, mTabs.length);
			set();
		}
	}

	public void push( int tabs) {
		int[] old = mTabs;
		mTabs = new int[old.length + 1];
		System.arraycopy( old, 0, mTabs, 1, old.length);
		mTabs[0] = tabs;
		set();
	}

	private void set() {
		StringBuffer sb = new StringBuffer();
		for (int i = mTabs[0]; i > 0; --i) {
//			sb.append( "\t");
			sb.append( "   ");
		}
		mText = sb.toString();
	}

	public void setNL() {
		mNL = true;
	}

	private void setNLBlock() {
		mNL = true;
		mComma = false;
	}

	@Override
	public String toString() {
		mComma = true;
		mNL = false;
		return mText;
	}
}
