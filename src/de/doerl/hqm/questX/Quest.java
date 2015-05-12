package de.doerl.hqm.questX;

import java.util.Map;
import java.util.Vector;

import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.questX.QuestTask.AQuestTask;
import de.doerl.hqm.questX.minecraft.ItemStack;
import de.doerl.hqm.utils.Helper;

class Quest implements IWriter {
	String mName;
	String mDesc;
	int mX, mY;
	boolean mBig;
	int mSetId;
//	QuestSet mQuestSet;
	ItemStack mIcon;
	int[] mRequirements;
	int[] mOptionLinks;
	RepeatInfo mRepeatInfo;
	int mTriggerTasks;
	TriggerType mTriggerType;
	boolean mUseModifiedParentRequirement;
	int mParentRequirementCount;
	Vector<AQuestTask> mTasks;
	ItemStack[] mReward;
	ItemStack[] mRewardChoice;
	ReputationReward[] mReputationRewards;

	private Quest( BitInputStream is) {
		mName = is.readString( DataBitHelper.QUEST_NAME_LENGTH);
		mDesc = is.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
		mX = is.readData( DataBitHelper.QUEST_POS_X);
		mY = is.readData( DataBitHelper.QUEST_POS_Y);
		mBig = is.readBoolean();
		//
		if (is.contains( FileVersion.SETS)) {
			mSetId = is.readData( DataBitHelper.QUEST_SETS);
		}
//		mQuestSet = (QuestSet) QuestLine.getActiveQuestLine().questSets.get( mSetId);
		if (is.contains( FileVersion.SETS) && is.readBoolean()) {
//			mIcon = is.readIcon( false);
		}
		if (is.readBoolean()) {
			mRequirements = Quest.readQuestIds( is);
		}
		if (is.contains( FileVersion.OPTION_LINKS) && is.readBoolean()) {
			mOptionLinks = Quest.readQuestIds( is);
		}
		mRepeatInfo = RepeatInfo.read( is);
		if (is.contains( FileVersion.TRIGGER_QUESTS)) {
			mTriggerType = TriggerType.get( is.readData( DataBitHelper.TRIGGER_TYPE));
			if (mTriggerType.isUseTaskCount()) {
				mTriggerTasks = is.readData( DataBitHelper.TASKS);
			}
		}
		if (is.contains( FileVersion.PARENT_COUNT) && is.readBoolean()) {
			mUseModifiedParentRequirement = true;
			mParentRequirementCount = is.readData( DataBitHelper.QUESTS);
		}
		else {
			mUseModifiedParentRequirement = false;
		}
		mTasks = QuestTask.read( is);
//		mReward = is.readRewardData();
//		mRewardChoice = is.readRewardData();
		mReputationRewards = ReputationReward.read( is);
	}

	public static Vector<Quest> read( BitInputStream is) {
		int count = is.readData( DataBitHelper.QUESTS);
		Vector<Quest> result = new Vector<Quest>();
		for (int i = 0; i < count; ++i) {
			if (is.readBoolean()) {
				result.add( new Quest( is));
			}
		}
		return result;
	}

	private static int[] readQuestIds( BitInputStream is) {
		return is.readIds( DataBitHelper.QUESTS);
	}

	public void addOptionLink( int id) {
	}

	public void addRequirement( int id) {
	}

	public Map<Integer, AQuestTask> getTasks() {
		return null;
	}

	public int getTriggerTasks() {
		return mTriggerTasks;
	}

	public boolean isCompleted( String playerName) {
		return false;
	}

	public boolean isEnabled( String playerName, boolean b) {
		return false;
	}

	public void writeBinary( AWriter out) {
		out.println( "# Quest");
		out.println( mName);
		out.println( mDesc);
		out.println( mX);
		out.println( mY);
		out.println( mBig);
		out.println( "# mSetId(mQuestSet)");
		out.println( mSetId);
//		out.println( mQuestSet);
		out.println( "# mIcon");
		out.print( mIcon);
		if (mRequirements != null) {
			out.println( mRequirements);
		}
		if (mOptionLinks != null) {
			out.println( mOptionLinks);
		}
		out.println( "# mRepeatInfo");
		out.print( mRepeatInfo);
		out.println( "# mTriggerType");
		out.println( mTriggerType);
		out.println( "# mTriggerTasks");
		out.println( mTriggerTasks);
		out.println( "# mUseModifiedParentRequirement");
		out.println( mUseModifiedParentRequirement);
		out.println( "# mParentRequirementCount");
		out.println( mParentRequirementCount);
//		if (mTasks != null) {
//			out.writeBinary( mTasks);
//		}
//		if (mReward != null) {
//			out.writeBinary( mReward);
//		}
//		if (mRewardChoice != null) {
//			out.writeBinary( mRewardChoice);
//		}
//		if (mReputationRewards != null) {
//			out.writeBinary( mReputationRewards);
//		}
	}

	@Override
	public void writeTo( AWriter out) {
		out.beginObject();
		out.print( "name", mName);
		out.print( "description", mDesc);
		out.print( "x", mX);
		out.print( "y", mY);
		out.print( "big", mBig);
		out.print( "setID", mSetId);
		out.print( "icon", mIcon);
		if (mRequirements != null) {
			out.print( "requirements", Helper.toString( mRequirements));
		}
		if (mOptionLinks != null) {
			out.print( "optionLinks", Helper.toString( mOptionLinks));
		}
		out.print( "repeatInfo", mRepeatInfo);
		out.print( "triggerType", mTriggerType);
		if (mTriggerType.isUseTaskCount()) {
			out.print( "triggerTasks", mTriggerTasks);
		}
		if (mUseModifiedParentRequirement) {
			out.print( "parentRequirementCount", mParentRequirementCount);
		}
		out.print( "tasks", mTasks);
		out.println();
		out.print( "reward", mReward);
		out.println();
		out.print( "rewardChoice", mRewardChoice);
		out.println();
		if (mReputationRewards != null) {
			out.print( "reputationRewards", mReputationRewards);
		}
		out.endObject();
	}
}
