package de.doerl.hqm.questX;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;

class Reputation implements IWriter {
	private static Map<Integer, Reputation> sReputations = new HashMap<Integer, Reputation>();
	int mID;
	String mName;
	ReputationMarker mNeutral;
	ReputationMarker[] mMarker;

	private Reputation( BitInputStream is) {
		mID = is.readData( DataBitHelper.REPUTATION);
		mName = is.readString( DataBitHelper.QUEST_NAME_LENGTH);
		String nn = is.readString( DataBitHelper.QUEST_NAME_LENGTH);
		mNeutral = new ReputationMarker( nn);
		sReputations.put( mID, this);
		mMarker = ReputationMarker.read( is);
		sort();
	}

	public static Reputation getReputation( int id) {
		return sReputations.get( id);
	}

	public static Reputation[] read( BitInputStream is) {
		sReputations.clear();
		if (is.contains( FileVersion.REPUTATION)) {
			int count = is.readData( DataBitHelper.REPUTATION);
			Reputation[] result = new Reputation[count];
			for (int i = 0; i < result.length; ++i) {
				result[i] = new Reputation( is);
			}
			return result;
		}
		return null;
	}

	public ReputationMarker getMarker( int id) {
		if (id == mMarker.length) {
			return mNeutral;
		}
		else {
			return mMarker[id];
		}
	}

	private void sort() {
		Arrays.sort( mMarker);
		for (int i = 0; i < mMarker.length; ++i) {
			mMarker[i].setId( i);
		}
		mNeutral.setId( mMarker.length);
	}

	@Override
	public void writeTo( AWriter out) {
		out.beginObject();
		out.print( "name", mName);
		out.print( "id", mID);
		out.print( "neutral", mNeutral);
		out.print( "reputations", mMarker);
		out.endObject();
	}
}
