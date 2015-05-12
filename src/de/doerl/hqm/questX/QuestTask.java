package de.doerl.hqm.questX;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.quest.Visibility;
import de.doerl.hqm.questX.Requirement.ARequirement;
import de.doerl.hqm.questX.minecraft.ItemStack;

public class QuestTask {
	public static Vector<AQuestTask> read( BitInputStream is) {
		int count = is.readData( DataBitHelper.TASKS);
		Vector<AQuestTask> result = new Vector<AQuestTask>(); // count
		for (int i = 0; i < count; ++i) {
			int type = is.readData( DataBitHelper.TASK_TYPE);
			if (is.contains( FileVersion.NO_ITEM_IDS)) {
				is.readBoolean(); // ?????
			}
			AQuestTask task = null;
			switch (type) {
				case 0:
					task = new QuestTaskItemsConsume( is);
					break;
				case 1:
					task = new QuestTaskItemsCrafting( is);
					break;
				case 2:
					task = new QuestTaskLocation( is);
					break;
				case 3:
					task = new QuestTaskItemsConsumeQDS( is);
					break;
				case 4:
					task = new QuestTaskItemsDetect( is);
					break;
				case 5:
					task = new QuestTaskMob( is);
					break;
				case 6:
					task = new QuestTaskDeath( is);
					break;
				case 7:
					task = new QuestTaskReputation( is);
					break;
			}
			if (result.size() > 0) {
				task.addRequirement( result.get( result.size() - 1));
			}
			result.add( task);
		}
		return result;
	}

	static abstract class AQuestTask implements IWriter {
		String mName;
		String mDesc;
		List<AQuestTask> mRequirements = new ArrayList<AQuestTask>();

		public AQuestTask( BitInputStream is) {
			mName = is.readString( DataBitHelper.QUEST_NAME_LENGTH);
			mDesc = is.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
		}

		void addRequirement( AQuestTask aQuestTask) {
			mRequirements.add( aQuestTask);
		}

		public boolean isCompleted( String playerName) {
			return false;
		}

		@Override
		public void writeTo( AWriter out) {
			out.print( "name", mName);
			out.print( "description", mDesc);
		}
	}

	static abstract class AQuestTaskItems extends AQuestTask {
		ARequirement[] mItems;

		public AQuestTaskItems( BitInputStream is) {
			super( is);
			mItems = Requirement.read( is);
		}

		public ARequirement[] getItems() {
			return mItems;
		}

		@Override
		public void writeTo( AWriter out) {
			super.writeTo( out);
			out.print( "items", mItems);
		}
	}

	static class Location implements IWriter {
		String mName;
		int mX, mY, mZ;
		int mRadius;
		ItemStack mIcon;
		Visibility mVisible;
		int mDim;

		public Location( BitInputStream is) {
			if (is.readBoolean()) {
//				mIcon = is.readIcon( false);
			}
			mName = is.readString( DataBitHelper.NAME_LENGTH);
			mX = is.readData( DataBitHelper.WORLD_COORDINATE);
			mY = is.readData( DataBitHelper.WORLD_COORDINATE);
			mZ = is.readData( DataBitHelper.WORLD_COORDINATE);
			mRadius = is.readData( DataBitHelper.WORLD_COORDINATE);
			mVisible = Visibility.get( is.readData( DataBitHelper.LOCATION_VISIBILITY));
			mDim = is.readData( DataBitHelper.WORLD_COORDINATE);
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			out.print( "name", mName);
			if (mIcon != null) {
				out.print( "icon", mIcon);
			}
			out.print( "x", mX);
			out.print( "y", mY);
			out.print( "z", mZ);
			out.print( "radius", mRadius);
			out.print( "visible", mVisible);
			out.print( "dim", mDim);
			out.endObject();
		}
	}

	static class Mob implements IWriter {
		String mName;
		String mMob;
		ItemStack mIcon;
		int mCount;
		boolean mExact;

		public Mob( BitInputStream is) {
			if (is.readBoolean()) {
//				mIcon = is.readIcon( false);
			}
			mName = is.readString( DataBitHelper.NAME_LENGTH);
			mMob = is.readString( DataBitHelper.MOB_ID_LENGTH);
			mCount = is.readData( DataBitHelper.KILL_COUNT);
			mExact = is.readBoolean();
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			out.print( "name", mName);
			if (mIcon != null) {
				out.print( "icon", mIcon);
			}
			out.print( "mob", mMob);
			out.print( "count", mCount);
			out.print( "exact", mExact);
			out.endObject();
		}
	}

	static class QuestTaskDeath extends AQuestTask {
		int mDeaths;

		public QuestTaskDeath( BitInputStream is) {
			super( is);
			mDeaths = is.readData( DataBitHelper.DEATHS);
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			out.print( "type", "death");
			super.writeTo( out);
			out.print( "deaths", "mDeaths");
			out.endObject();
		}
	}

	static class QuestTaskItemsConsume extends AQuestTaskItems {
		public QuestTaskItemsConsume( BitInputStream is) {
			super( is);
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			out.print( "type", "itemsConsume");
			super.writeTo( out);
			out.endObject();
		}
	}

	static class QuestTaskItemsConsumeQDS extends QuestTaskItemsConsume {
		public QuestTaskItemsConsumeQDS( BitInputStream is) {
			super( is);
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			out.print( "type", "itemsConsumeQDS");
			super.writeTo( out);
			out.endObject();
		}
	}

	static class QuestTaskItemsCrafting extends AQuestTaskItems {
		public QuestTaskItemsCrafting( BitInputStream is) {
			super( is);
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			out.print( "type", "itemsCrafting");
			super.writeTo( out);
			out.endObject();
		}
	}

	static class QuestTaskItemsDetect extends AQuestTaskItems {
		public QuestTaskItemsDetect( BitInputStream is) {
			super( is);
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			out.print( "type", "itemsDetect");
			super.writeTo( out);
			out.endObject();
		}
	}

	static class QuestTaskLocation extends AQuestTask {
		Location[] mLocations;

		public QuestTaskLocation( BitInputStream is) {
			super( is);
			int count = is.readData( DataBitHelper.TASK_LOCATION_COUNT);
			mLocations = new Location[count];
			for (int i = 0; i < mLocations.length; i++) {
				mLocations[i] = new Location( is);
			}
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			super.writeTo( out);
			out.print( "locations", mLocations);
			out.endObject();
		}
	}

	static class QuestTaskMob extends AQuestTask {
		Mob[] mMobs;

		public QuestTaskMob( BitInputStream is) {
			super( is);
			int count = is.readData( DataBitHelper.TASK_MOB_COUNT);
			mMobs = new Mob[count];
			for (int i = 0; i < mMobs.length; i++) {
				mMobs[i] = new Mob( is);
			}
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			out.print( "type", "mob");
			super.writeTo( out);
			out.print( "mobs", mMobs);
			out.endObject();
		}
	}

	static class QuestTaskReputation extends AQuestTask {
		ReputationSetting[] mSettings;

		public QuestTaskReputation( BitInputStream is) {
			super( is);
			int count = is.readData( DataBitHelper.REPUTATION_SETTING);
			mSettings = new ReputationSetting[count];
			for (int i = 0; i < count; i++) {
				mSettings[i] = new ReputationSetting( is);
			}
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			out.print( "type", "Reputation");
			super.writeTo( out);
			out.print( "settings", mSettings);
			out.endObject();
		}
	}

	static class ReputationSetting implements IWriter {
		int mRepID;
		Integer mLowerID;
		Integer mUpperID;
		boolean mInverted;

		public ReputationSetting( BitInputStream is) {
			mRepID = is.readData( DataBitHelper.REPUTATION);
			mLowerID = is.readBoolean() ? is.readData( DataBitHelper.REPUTATION_MARKER) : null;
			mUpperID = is.readBoolean() ? is.readData( DataBitHelper.REPUTATION_MARKER) : null;
			mInverted = is.readBoolean();
		}

		public void writeBinary( AWriter out) {
			out.println( "# ReputationSetting");
			Reputation reputation = Reputation.getReputation( mRepID);
			out.println( reputation);
			if (mLowerID != null) {
				out.print( reputation.getMarker( mLowerID));
			}
			if (mUpperID != null) {
				out.print( reputation.getMarker( mUpperID));
			}
			out.println( mInverted);
		}

		@Override
		public void writeTo( AWriter out) {
			out.beginObject();
			out.print( "reputationID", mRepID);
			if (mLowerID != null) {
				out.print( "lowerID", mLowerID.intValue());
			}
			if (mUpperID != null) {
				out.print( "upperID", mUpperID.intValue());
			}
			out.print( "inverted", mInverted);
			out.endObject();
		}
	}
}
