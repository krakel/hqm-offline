package de.doerl.hqm.questX;

import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;

class ReputationReward implements IWriter {
	Reputation mReputation;
	int mRepValue;
	int mValue;

	private ReputationReward( BitInputStream is) {
		mRepValue = is.readData( DataBitHelper.REPUTATION);
		mValue = is.readData( DataBitHelper.REPUTATION_VALUE);
		mReputation = Reputation.getReputation( mRepValue);
	}

	public static ReputationReward[] read( BitInputStream is) {
		if (is.contains( FileVersion.REPUTATION)) {
			int count = is.readData( DataBitHelper.REPUTATION_REWARD);
			if (count > 0) {
				ReputationReward[] result = new ReputationReward[count];
				for (int i = 0; i < result.length; i++) {
					result[i] = new ReputationReward( is);
				}
				return result;
			}
		}
		return null;
	}

	@Override
	public void writeTo( AWriter out) {
		out.beginObject();
		out.print( "reputation", mRepValue);
		out.print( "value", mValue);
		out.endObject();
	}
}
