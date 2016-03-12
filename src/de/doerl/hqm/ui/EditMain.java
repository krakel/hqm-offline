package de.doerl.hqm.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.medium.MediumUtils;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.VersionHelper;

public class EditMain {
	public static final String SOURCE = "UltimateQuestpack";
	public static final String PATH = "/Games/Minecraft/hqm/";
	public static final String SUFFIX_HQM = ".hqm";
	public static final String SUFFIX_JSON = ".json";
	public static final String PATH_SRC = PATH + SOURCE + SUFFIX_HQM;
	public static final String PATH_DST = PATH + SOURCE + SUFFIX_JSON;

	public static void createTest() {
		try {
			File src = new File( PATH_SRC);
			FHqm hqm = new FHqm( SOURCE);
			InputStream is = MediumUtils.getSource( src);
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
		VersionHelper.execute();
//		EditManager.logVersion();
//		createTest();
	}
}
