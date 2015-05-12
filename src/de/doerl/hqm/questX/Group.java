package de.doerl.hqm.questX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.questX.minecraft.ItemStack;

class Group implements IWriter {
	public static Map<Integer, Group> sGroups = new HashMap<Integer, Group>();
	public static List<Group> sGroupList = new ArrayList<Group>();
	String mName;
	int mTierID;
	List<ItemStack> mItems;
	int mLimit;
	int mID;

	public Group( BitInputStream is, int id) {
		if (is.contains( FileVersion.BAG_LIMITS)) {
			mID = is.readData( DataBitHelper.GROUP_COUNT);
		}
		else {
			mID = id;
		}
		mName = is.readString( DataBitHelper.QUEST_NAME_LENGTH);
		mTierID = is.readData( DataBitHelper.TIER_COUNT);
		mItems = new ArrayList<ItemStack>();
		int count = is.readData( DataBitHelper.GROUP_ITEMS);
		for (int j = 0; j < count; j++) {
//			ItemStack itemStack = is.readAndFixItemStack( true);
//			if (itemStack != null) {
//				mItems.add( itemStack);
//			}
		}
		if (is.contains( FileVersion.BAG_LIMITS) && is.readBoolean()) {
			mLimit = is.readData( DataBitHelper.LIMIT);
		}
	}

	public static void add( Group group) {
		sGroups.put( Integer.valueOf( group.mID), group);
		sGroupList.add( group);
	}

	public static void readAll( BitInputStream is) {
		sGroups.clear();
		sGroupList.clear();
//		QuestLine.getActiveQuestLine().groupCount = 0;
		int count = is.readData( DataBitHelper.GROUP_COUNT);
		for (int i = 0; i < count; i++) {
			add( new Group( is, i));
		}
	}

	public void writeBinary( AWriter out) {
		out.println( "# Group");
		out.println( mName);
		out.println( GroupTier.sTiers.get( mTierID));
		out.println( mItems);
		out.println( mLimit);
		out.println( mID);
	}

	@Override
	public void writeTo( AWriter out) {
		out.beginObject();
		out.print( "id", mID);
		out.print( "name", mName);
		out.print( "tierID", mTierID);
		out.print( "items", mItems);
		out.endObject();
	}
}
