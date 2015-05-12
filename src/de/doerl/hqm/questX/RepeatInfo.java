package de.doerl.hqm.questX;

import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.quest.RepeatType;

class RepeatInfo implements IWriter {
	RepeatType mType;
	int mTotal;
	int mDays;
	int mHours;

	private RepeatInfo( BitInputStream is) {
		if (is.contains( FileVersion.REPEATABLE_QUESTS)) {
			mType = RepeatType.get( is.readData( DataBitHelper.REPEAT_TYPE));
			if (mType.isUseTime()) {
				mTotal = is.readData( DataBitHelper.HOURS);
				mDays = mTotal / 24;
				mHours = mTotal % 24;
			}
		}
		else {
			mType = RepeatType.NONE;
		}
	}

	public static RepeatInfo read( BitInputStream is) {
		return new RepeatInfo( is);
	}

	@Override
	public void writeTo( AWriter out) {
		out.beginObject();
		out.print( "type", mType);
		if (mType.isUseTime()) {
			out.print( "total", mTotal);
		}
		out.endObject();
	}
}
