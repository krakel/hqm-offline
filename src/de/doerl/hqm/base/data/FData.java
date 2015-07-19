package de.doerl.hqm.base.data;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.doerl.hqm.base.dispatch.IDataWorker;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.Utils;

public final class FData extends AGame {
	private static final Logger LOGGER = Logger.getLogger( FData.class.getName());
	private FileVersion mVersion;
	private final Vector<FPlayer> mPlayers = new Vector<>();
	private final Vector<FTeam> mTeams = new Vector<>();
	public boolean mHardcore;
	public boolean mQuest;
	public boolean mServer;
	public int mTicks;
	public int mHours;

	public FData() {
	}

	@Override
	public <T, U> T accept( IDataWorker<T, U> w, U p) {
		return w.forData( this, p);
	}

	public FPlayer createPlayer() {
		FPlayer player = new FPlayer( this);
		mPlayers.add( player);
		return player;
	}

	public FTeam createTeam() {
		FTeam team = new FTeam( this);
		mTeams.add( team);
		return team;
	}

	public <T, U> T forEachPlayer( IDataWorker<T, U> worker, U p) {
		for (FPlayer disp : mPlayers) {
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

	public <T, U> T forEachTeam( IDataWorker<T, U> worker, U p) {
		for (FTeam disp : mTeams) {
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
		return this;
	}

	@Override
	public AGame getParent() {
		return null;
	}

	public FileVersion getVersion() {
		return mVersion;
	}

	public void setVersion( FileVersion version) {
		mVersion = version;
	}
}
