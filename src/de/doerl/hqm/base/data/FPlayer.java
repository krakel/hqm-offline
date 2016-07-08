package de.doerl.hqm.base.data;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.dispatch.GroupOfID;
import de.doerl.hqm.base.dispatch.IDataWorker;
import de.doerl.hqm.base.dispatch.SizeOfGroups;
import de.doerl.hqm.utils.Utils;

public final class FPlayer extends AGame {
	private static final Logger LOGGER = Logger.getLogger( FPlayer.class.getName());
	public final FData mParentData;
	public ArrayList<GroupData> mGroupData = new ArrayList<>();
	private final ArrayList<FPlayerStats> mStats = new ArrayList<>();
	public String mName;
	public int mLives;
	public int mSelectedQuest;
	public int mSelectedTask;
	public boolean mPlayedLore;
	public boolean mReceivedBook;
	public FTeamData mTeam;

	public FPlayer( FData parent) {
		mParentData = parent;
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forPlayer( this, p);
	}

	private void createGroupData( int id) {
		int start = id < mGroupData.size() ? id : mGroupData.size();
		for (int i = start; i <= id; i++) {
			if (GroupOfID.get( null, i) != null) {
				mGroupData.add( i, new GroupData());
			}
			else {
				mGroupData.add( i, null);
			}
		}
	}

	public FPlayerStats createStats() {
		FPlayerStats team = new FPlayerStats( this);
		mStats.add( team);
		return team;
	}

	public <T, U> T forEachPlayer( IDataWorker<T, U> worker, U p) {
		for (FPlayerStats disp : mStats) {
			try {
				if (disp != null) {
					T obj = disp.accept( worker, p);
					if (obj != null) {
						return obj;
					}
				}
			}
			catch (RuntimeException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
		return null;
	}

	@Override
	public FData getData() {
		return mParentData;
	}

	public GroupData getGroupData( int id) {
		if (id < 0 || id >= SizeOfGroups.get( (FGroupTierCat) null)) {
			return null;
		}
		if (id >= mGroupData.size()) {
			createGroupData( id);
		}
		return mGroupData.get( id);
	}

	@Override
	public AGame getParent() {
		return mParentData;
	}

	public void setName( String name) {
		mName = name;
	}
}
