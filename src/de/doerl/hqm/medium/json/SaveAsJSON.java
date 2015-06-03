package de.doerl.hqm.medium.json;

import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ASaveAsFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IMedium;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.utils.Utils;

class SaveAsJSON extends ASaveAsFile {
	private static final long serialVersionUID = -1904996548131026440L;
	private static final Logger LOGGER = Logger.getLogger( SaveAsJSON.class.getName());

	public SaveAsJSON( ICallback cb) {
		super( "json.saveAs", cb);
	}

	@Override
	public void action( Window frame) {
		FHqm hqm = mCallback.updateHQM();
		if (hqm != null) {
			String pfad = getLastOpenDir();
			JFileChooser chooser = createChooser( pfad);
			chooser.setFileFilter( Medium.FILTER);
			File choose = selectSaveDialog( frame, chooser);
			if (choose != null) {
				File file = Medium.normalize( choose);
				if (!file.exists() || mCallback.askOverwrite()) {
					setLastHQM( file);
					try {
						OutputStream os = new FileOutputStream( file);
						if (Medium.writeHQM( hqm, os, mCallback)) {
							MediaManager.setProperty( hqm, Medium.JSON_PATH, file);
							MediaManager.setProperty( hqm, IMedium.ACTIV_MEDIUM, Medium.MEDIUM);
							mCallback.savedHQMAction();
						}
					}
					catch (Exception ex) {
						Utils.logThrows( LOGGER, Level.WARNING, ex);
					}
				}
			}
		}
	}
}
