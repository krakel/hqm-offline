package de.doerl.hqm.utils.mods;

import java.util.ArrayList;

class NBTMatcher implements IMatcher {
	private String mStk;
	private ArrayList<SimpleMatcher> mMap = new ArrayList<>();

	public NBTMatcher( String stk) {
		mStk = stk;
	}

	@Override
	public void addNBT( String nbt, String file) {
		mMap.add( new SimpleMatcher( mStk, nbt, file));
	}

	@Override
	public String findFile( String nbt) {
		for (SimpleMatcher s : mMap) {
			String res = s.findFile( nbt);
			if (res != null) {
				return res;
			}
		}
		return null;
	}

	@Override
	public void findMatch( ArrayList<SimpleMatcher> arr, String value) {
		for (SimpleMatcher s : mMap) {
			s.findMatch( arr, value);
		}
	}

	public String getStk() {
		return mStk;
	}
}
