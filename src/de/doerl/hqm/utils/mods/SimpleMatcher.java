package de.doerl.hqm.utils.mods;

import java.util.ArrayList;

import de.doerl.hqm.utils.Utils;

public class SimpleMatcher implements IMatcher {
	private String mStk;
	private String mNbt;
	private String mFile;

	public SimpleMatcher( String stk) {
		mStk = stk;
	}

	public SimpleMatcher( String stk, String nbt, String file) {
		mStk = stk;
		mNbt = nbt;
		mFile = file;
	}

	@Override
	public void addNBT( String nbt, String file) {
		mNbt = nbt;
		mFile = file;
	}

	@Override
	public String findFile( String nbt) {
		return Utils.equals( mNbt, nbt) ? mFile : null;
	}

	@Override
	public void findMatch( ArrayList<SimpleMatcher> arr, String value) {
		if (mFile.contains( value)) {
			arr.add( this);
		}
	}

	public String getNbt() {
		return mNbt;
	}

	@Override
	public String getStk() {
		return mStk;
	}
}
