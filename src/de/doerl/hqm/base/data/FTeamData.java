package de.doerl.hqm.base.data;

import java.util.ArrayList;
import java.util.List;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.dispatch.IDataWorker;
import de.doerl.hqm.quest.LifeSetting;
import de.doerl.hqm.quest.RewardSetting;

public final class FTeamData extends AGame {
	public final FData mParentData;
	public FTeamStats mStats = new FTeamStats( this);
	public boolean mReloadedInvites;
	public List<FTeamData> mInvites = new ArrayList<>();
	private List<PlayerEntry> mPlayers = new ArrayList<>();
	public List<FQuestData> mQuestData = new ArrayList<>();
	public int mClientTeamLives = -1;
	public LifeSetting mLifeSetting = LifeSetting.SHARE;
	public RewardSetting mRewardSetting = RewardSetting.getDefault();
	private String mName;
	private int mID;
	private boolean mSingle;

	FTeamData( FData parent) {
		mParentData = parent;
	}

	FTeamData( FData parent, int id) {
		mParentData = parent;
		mID = id;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forTeam( this, p);
	}

	public void createPlayer( String name, boolean inTeam, boolean owner) {
		mPlayers.add( new PlayerEntry( name, inTeam, owner));
	}

	public FQuestData createQuestData( FQuest quest) {
		FQuestData questData = new FQuestData( this, quest);
		mQuestData.add( questData);
		return questData;
	}

	@Override
	public FData getData() {
		return mParentData;
	}

	public int getID() {
		return mID;
	}

	public String getName() {
		return mName;
	}

	@Override
	public AGame getParent() {
		return mParentData;
	}

	public int getPlayerCount() {
		int count = 0;
		for (PlayerEntry player : mPlayers) {
			if (player.mTeam) {
				++count;
			}
		}
		return count;
	}

	public boolean isSharingLives() {
		return false;
	}

	public boolean isSingle() {
		return mSingle;
	}

	public void setId( int i) {
	}

	public void setName( String name) {
		mName = name;
	}

	public void setReputation( int i, Integer value) {
	}

	public void setSingle( boolean value) {
		mSingle = value;
	}
}
