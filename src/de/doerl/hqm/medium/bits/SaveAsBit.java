package de.doerl.hqm.medium.bits;

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

class SaveAsBit extends ASaveAsFile {
	private static final long serialVersionUID = -7572089145353012288L;
	private static final Logger LOGGER = Logger.getLogger( SaveAsBit.class.getName());

	public SaveAsBit( ICallback cb) {
		super( "bit.saveAs", cb);
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
							MediaManager.setProperty( hqm, Medium.HQM_PATH, file);
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
