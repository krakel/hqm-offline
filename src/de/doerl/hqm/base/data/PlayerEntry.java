package de.doerl.hqm.base.data;

public class PlayerEntry {
	public String mName;
	public boolean mTeam;
	public boolean mOwner;

	public PlayerEntry( String name, boolean inTeam, boolean owner) {
		mName = name;
		mTeam = inTeam;
		mOwner = owner;
	}
}
