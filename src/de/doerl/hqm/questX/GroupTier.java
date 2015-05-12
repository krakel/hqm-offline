package de.doerl.hqm.questX;

import java.util.ArrayList;
import java.util.List;

import de.doerl.hqm.quest.BagTier;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.GuiColor;
import de.doerl.hqm.utils.Helper;

class GroupTier implements IWriter {
	public static List<GroupTier> sTiers = new ArrayList<GroupTier>();
	String mName;
	GuiColor mColor;
	int mWeights[];

	public GroupTier( BitInputStream is) {
		mName = is.readString( DataBitHelper.QUEST_NAME_LENGTH);
		mColor = GuiColor.get( is.readData( DataBitHelper.COLOR));
		mWeights = BagTier.newArray();
		for (int i = 0; i < mWeights.length; i++) {
			mWeights[i] = is.readData( DataBitHelper.WEIGHT);
		}
	}

	public static void readAll( BitInputStream is) {
		sTiers.clear();
		int count = is.readData( DataBitHelper.TIER_COUNT);
		for (int i = 0; i < count; i++) {
			sTiers.add( new GroupTier( is));
		}
	}

	@Override
	public void writeTo( AWriter out) {
		out.beginObject();
		out.print( "name", mName);
		out.print( "color", mColor);
		out.print( "weights", Helper.toString( mWeights));
		out.endObject();
	}
}
