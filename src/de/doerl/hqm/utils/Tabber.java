package de.doerl.hqm.utils;

public class Tabber {
	private StringBuffer mBuffer = new StringBuffer();
	private int mTabs;
	private boolean mNL;
	private boolean mComma;

	public void dec() {
		if (mTabs > 0) {
			mTabs--;
			space();
		}
		nlBlock();
	}

	public void inc() {
		mTabs++;
		space();
		nlBlock();
	}

	public boolean isComma() {
		return mComma;
	}

	public boolean isNL() {
		return mNL;
	}

	public void nl() {
		mNL = true;
	}

	private void nlBlock() {
		mNL = true;
		mComma = false;
	}

	private void space() {
		mBuffer.setLength( 0);
		for (int i = mTabs; i > 0; --i) {
			mBuffer.append( "   ");
		}
	}

	@Override
	public String toString() {
		mComma = true;
		mNL = false;
		return mBuffer.toString();
	}
}
