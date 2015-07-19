package de.doerl.hqm.base.data;

import de.doerl.hqm.base.dispatch.IDataWorker;
import de.doerl.hqm.quest.DeathType;

public final class FTeamStats extends AGame {
	public final FTeam mParentTeam;
	public final int mDeaths[] = DeathType.newArray();
	public String mName;
	public int mPlayers;
	public int mLives;
	public int mProgress;

	public FTeamStats( FTeam parentTeam) {
		mParentTeam = parentTeam;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forTeamStats( this, p);
	}

	@Override
	public FData getData() {
		return mParentTeam.getData();
	}

	@Override
	public AGame getParent() {
		return mParentTeam;
	}
}
