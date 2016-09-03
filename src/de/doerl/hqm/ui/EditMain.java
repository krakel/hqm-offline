package de.doerl.hqm.ui;

import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.VersionHelper;

public class EditMain {
	public static void main( String[] args) {
		System.setProperty( "hqm.console.logLevel", "ALL");
		Utils.setStackTrace( true);
		EditFrame.createNew();
		VersionHelper.execute();
//		EditManager.logVersion();
	}
}
