package de.doerl.hqm.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.medium.MediumUtils;
import de.doerl.hqm.utils.Utils;

public class EditMain {
//	public static final String SOURCE = "regrowth";
//	public static final String SOURCE = "pathfinder";
//	public static final String SOURCE = "landing";
//	public static final String SOURCE = "phoenix";
//	public static final String SOURCE = "quests7";
	public static final String SOURCE = "UltimateQuestpack";
	public static final String PATH = "/Games/Minecraft/hqm/";
	public static final String SUFFIX_HQM = ".hqm";
	public static final String SUFFIX_JSON = ".json";
	public static final String PATH_SRC = PATH + SOURCE + SUFFIX_HQM;
	public static final String PATH_DST = PATH + SOURCE + SUFFIX_JSON;

	public static void createTest() {
		try {
			FHqm hqm = new FHqm( new File( PATH_SRC));
			InputStream is = MediumUtils.getSource( hqm.getSource());
			MediaManager.get( "bit").testLoad( hqm, is);
			FileOutputStream os = new FileOutputStream( PATH_DST);
			MediaManager.get( "json").testSave( hqm, os);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void main( String[] args) {
		System.setProperty( "hqm.console.logLevel", "ALL");
		Utils.setStackTrace( true);
		EditFrame.createNew();
//		EditManager.logVersion();
//		createTest();
	}
}
