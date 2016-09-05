package de.doerl.hqm.ui;

import java.awt.event.ActionEvent;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.PreferenceManager;

class ActionNew extends ABundleAction {
	private static final long serialVersionUID = -44344993759630964L;
	private EditFrame mFrame;

	public ActionNew( EditFrame frame) {
		super( "hqm.new");
		mFrame = frame;
	}

	public void actionPerformed( ActionEvent e) {
		FHqm hqm = new FHqm( null);
		String pref = PreferenceManager.getString( "config.version");
		hqm.setVersion( FileVersion.parse( pref));
		mFrame.getModel().loadHQM( hqm);
		mFrame.showHqm( hqm);
	}
}
