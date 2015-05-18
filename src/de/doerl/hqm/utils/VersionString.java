package de.doerl.hqm.utils;

public class VersionString implements Comparable<VersionString> {
	private String[] mList;
	private String mVersion;

	public VersionString( String version) {
		if (version.length() == 0) {
			throw new IllegalArgumentException( "version may not be zero length");
		}
		mList = toList( version);
		mVersion = version;
	}

	public static int compare( String s1, String s2) {
		if (s1 == s2) {
			return 0;
		}
		if (s1 == null) {
			return 1;
		}
		if (s2 == null) {
			return 1;
		}
		String[] list1 = s1.split( "\\.");
		String[] list2 = s2.split( "\\.");
		return compareList( list1, list2);
	}

	private static int compareList( String[] list1, String[] list2) {
		int min = Math.min( list1.length, list2.length);
		for (int i = 0; i < min; ++i) {
			int thisNum = Integer.parseInt( list1[i]);
			int otherNum = Integer.parseInt( list2[i]);
			if (thisNum != otherNum) {
				return thisNum < otherNum ? -1 : 1;
			}
		}
		if (list1.length == list2.length) {
			return 0;
		}
		return list1.length < list2.length ? -1 : 1;
	}

	private static String[] toList( String version) {
		String[] list = version.split( "\\.");
		for (String s : list) {
			if (s == null) {
				throw new IllegalArgumentException( "version contains a null version sub-string");
			}
			if (s.length() == 0) {
				throw new IllegalArgumentException( "version contains a zero lenght version sub-string");
			}
			Integer.parseInt( s);
		}
		return list;
	}

	@Override
	public int compareTo( VersionString other) {
		if (other == this) {
			return 0;
		}
		if (other.mVersion.equals( mVersion)) {
			return 0;
		}
		return compareList( mList, other.mList);
	}

	@Override
	public String toString() {
		return mVersion;
	}
}
