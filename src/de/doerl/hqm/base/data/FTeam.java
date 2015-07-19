package de.doerl.hqm.base.data;

import java.util.ArrayList;
import java.util.List;

import de.doerl.hqm.base.dispatch.IDataWorker;
import de.doerl.hqm.quest.LifeSetting;
import de.doerl.hqm.quest.RewardSetting;

public final class FTeam extends AGame {
	public final FData mParentData;
	public String mName;
	public FTeamStats mStats = new FTeamStats( this);
	public boolean mReloadedInvites;
	public List<FTeam> mInvites = new ArrayList<>();
	public List<PlayerEntry> mPlayers = new ArrayList<>();
	public List<QuestData> mQuestData = new ArrayList<>();
	public int mClientTeamLives;
	public int mID;
	public LifeSetting mLifeSetting;
	public RewardSetting mRewardSetting;

	public FTeam( FData parent) {
		mParentData = parent;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forTeam( this, p);
	}

	public void createReputation() {
	}

	@Override
	public FData getData() {
		return mParentData;
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
		return false;
	}

	public void setId( int i) {
	}

	public void setReputation( int i, Integer valueOf) {
	}
}
