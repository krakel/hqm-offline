package de.doerl.hqm.base.data;

import de.doerl.hqm.base.dispatch.IDataWorker;
import de.doerl.hqm.quest.DeathType;

public final class FPlayerStats extends AGame {
	public final FPlayer mParentPlayer;
	public final int mDeaths[] = DeathType.newArray();
	public String mName;

	public FPlayerStats( FPlayer parentPlayer) {
		mParentPlayer = parentPlayer;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forPlayerStats( this, p);
	}

	@Override
	public FData getData() {
		return mParentPlayer.getData();
	}

	@Override
	public AGame getParent() {
		return mParentPlayer;
	}
}
