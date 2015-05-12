package de.doerl.hqm.questX;

import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;

class QuestSet implements IWriter {
	private static final QuestSet[] DEF_QUEST_SET = new QuestSet[] {
		new QuestSet()
	};
	String mName;
	String mDesc;

	private QuestSet() {
		mName = "Automatically generated";
		mDesc = "This set was automatically generated. All your quests were put in this one.";
	}

	private QuestSet( BitInputStream is) {
		mName = is.readString( DataBitHelper.QUEST_NAME_LENGTH);
		mDesc = is.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
	}

	public static QuestSet[] read( BitInputStream is) {
		if (is.contains( FileVersion.SETS)) {
			int count = is.readData( DataBitHelper.QUEST_SETS);
			QuestSet[] result = new QuestSet[count];
			for (int i = 0; i < result.length; i++) {
				result[i] = new QuestSet( is);
			}
			return result;
		}
		else {
			return DEF_QUEST_SET;
		}
	}

	@Override
	public void writeTo( AWriter out) {
		out.beginObject();
		out.print( "name", mName);
		out.print( "decription", mDesc);
		out.endObject();
	}
}
