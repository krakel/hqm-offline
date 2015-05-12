package de.doerl.hqm.questX;

import de.doerl.hqm.quest.DataBitHelper;

class ReputationMarker implements Comparable<ReputationMarker>, IWriter {
	String mName;
	int mValue;
	int mID;
	boolean mIsNeutral;

	private ReputationMarker( BitInputStream is) {
		mName = is.readString( DataBitHelper.QUEST_NAME_LENGTH);
		mValue = is.readData( DataBitHelper.REPUTATION_VALUE);
		mIsNeutral = false;
	}

	ReputationMarker( String neutral) {
		mName = neutral;
		mIsNeutral = true;
	}

	public static ReputationMarker[] read( BitInputStream is) {
		int count = is.readData( DataBitHelper.REPUTATION_MARKER);
		ReputationMarker[] result = new ReputationMarker[count];
		for (int i = 0; i < count; ++i) {
			result[i] = new ReputationMarker( is);
		}
		return result;
	}

	@Override
	public int compareTo( ReputationMarker other) {
		return Integer.compare( mValue, other.mValue);
	}

	public void setId( int id) {
		mID = id;
	}

	@Override
	public void writeTo( AWriter out) {
		out.beginObject();
		out.print( "name", mName);
		if (!mIsNeutral) {
			out.print( "value", mValue);
		}
		out.endObject();
	}
}
