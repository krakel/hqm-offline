package de.doerl.hqm.ui;

public enum LinkType {
	NORM( "hqm.quest.normA", "hqm.quest.bigA", "hqm.quest.normB", "hqm.quest.bigB"),
	DARK( "hqm.dark.normA", "hqm.dark.bigA", "hqm.dark.normB", "hqm.dark.bigB"),
	BASE( "hqm.base.normA", "hqm.base.bigA", "hqm.base.normB", "hqm.base.bigB"),
	LINK( "hqm.link.normA", "hqm.link.bigA", "hqm.link.normB", "hqm.link.bigB"),
	PREF( "hqm.pref.normA", "hqm.pref.bigA", "hqm.pref.normB", "hqm.pref.bigB"),
	POST( "hqm.post.normA", "hqm.post.bigA", "hqm.post.normB", "hqm.post.bigB");
	private String mNormKeyA;
	private String mBigKeyA;
	private String mNormKeyB;
	private String mBigKeyB;

	private LinkType( String normKeyA, String bigKeyA, String normKeyB, String bigKeyB) {
		mNormKeyA = normKeyA;
		mBigKeyA = bigKeyA;
		mNormKeyB = normKeyB;
		mBigKeyB = bigKeyB;
	}

	public String getKey( boolean big, boolean alt) {
		if (alt) {
			return big ? mBigKeyB : mNormKeyB;
		}
		else {
			return big ? mBigKeyA : mNormKeyA;
		}
	}
}
