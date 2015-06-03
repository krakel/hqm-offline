package de.doerl.hqm.questX;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Vector;

import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.ui.EditMain;
import de.doerl.hqm.utils.Utils;

public class HQMFile implements IWriter {
	FileVersion mVersion;
	String mPassCode;
	String mDescr;
	QuestSet[] mQuestSets;
	Reputation[] mReputations;
	Vector<Quest> mQuests;

	public HQMFile( BitInputStream is) throws IOException {
		mVersion = is.mVersion;
		if (is.contains( FileVersion.LOCK)) {
			mPassCode = is.readString( DataBitHelper.PASS_CODE);
		}
		if (is.contains( FileVersion.LORE)) {
			mDescr = is.readString( DataBitHelper.QUEST_DESCRIPTION_LENGTH);
		}
		else {
			mDescr = "No description";
		}
		mQuestSets = QuestSet.read( is);
		mReputations = Reputation.read( is);
		mQuests = Quest.read( is);
		for (Quest quest : mQuests) {
			if (quest.mRequirements != null) {
				for (int id : quest.mRequirements) {
					quest.addRequirement( id);
				}
			}
			if (quest.mOptionLinks != null) {
				for (int id : quest.mOptionLinks) {
					quest.addOptionLink( id);
				}
			}
		}
		if (is.contains( FileVersion.BAGS)) {
			GroupTier.readAll( is);
			Group.readAll( is);
		}
	}

	static FileWriter getDestination( String name) throws IOException {
		File dst = new File( name);
		//		if (dst.exists()) {
		//			throw new IOException( "SOURCE aleady exist: " + name);
		//		}
		return new FileWriter( dst);
	}

	static BitInputStream getSource( String name) throws IOException {
		File src = new File( name);
		if (!src.exists()) {
			throw new IOException( "SOURCE does not exist: " + name);
		}
		byte[] buff = new byte[(int) src.length()];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream( src);
			fis.read( buff);
		}
		finally {
			Utils.closeIgnore( fis);
		}
		return new BitInputStream( new ByteArrayInputStream( buff));
	}

	static void makePlain( String name, Writer os) throws IOException {
		//		AWriter out = null;
		//		try {
		//			out = new JsonWriter( os);
		//			HQMFile cf = readFile( name);
		//			cf.writeTo( out);
		//		}
		//		finally {
		//			Utils.closeIgnore( out);
		//		}
	}

	static HQMFile readFile( String name) throws IOException {
		HQMFile result = null;
		BitInputStream os = null;
		try {
			os = getSource( name);
			result = new HQMFile( os);
		}
		finally {
			Utils.closeIgnore( os);
		}
		return result;
	}

	public static void run() {
		Writer os = null;
		try {
//			os = new StringWriter();
			os = getDestination( EditMain.PATH_DST);
			makePlain( EditMain.PATH_SRC, os);
//			System.out.print( os.toString());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			Utils.closeIgnore( os);
		}
	}

	@Override
	public void writeTo( AWriter out) {
		out.beginObject();
		out.print( "version", mVersion);
		if (mPassCode != null) {
			out.print( "passcode", mPassCode);
		}
		out.print( "decription", mDescr);
		out.print( "questSets", mQuestSets);
		out.println();
		out.print( "reputations", mReputations);
		out.println();
		out.print( "quests", mQuests);
		out.println();
		out.print( "groupTiers", GroupTier.sTiers);
		out.println();
		out.print( "groups", Group.sGroupList);
		out.endObject();
	}
}
