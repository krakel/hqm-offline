package de.doerl.hqm.medium.json;

import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ASaveFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IMedium;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.Utils;

@SuppressWarnings( "nls")
class SaveJSON extends ASaveFile {
	private static final long serialVersionUID = -2140887631447065333L;
	private static final Logger LOGGER = Logger.getLogger( SaveJSON.class.getName());

	public SaveJSON( ICallback cb) {
		super( "json.save", cb);
	}

	@SuppressWarnings( "nls")
	public void action( Window frame) {
		FHqm hqm = mCallback.updateHQM();
		if (hqm != null) {
			File path = (File) MediaManager.getProperty( hqm, Medium.JSON_PATH);
			if (path == null) {
				String pfad = getLastOpenDir( BaseDefaults.LAST_OPEN_DIR);
				JFileChooser chooser = createChooser( pfad);
				chooser.setFileFilter( Medium.FILTER);
				File choose = selectSaveDialog( frame, chooser);
				if (choose != null) {
					File file = Medium.normalize( choose);
					if (!file.exists() || mCallback.askOverwrite()) {
						setLastHQM( file);
//						ANamed.rename( def, norm);
						path = file;
					}
				}
			}
			if (path != null) {
				try {
					OutputStream os = new FileOutputStream( path);
					Medium.writeHQM( hqm, os, mCallback);
					MediaManager.setProperty( hqm, IMedium.ACTIV_MEDIUM, Medium.MEDIUM);
					mCallback.savedHQMAction();
				}
				catch (Exception ex) {
					Utils.logThrows( LOGGER, Level.WARNING, ex);
				}
			}
		}
	}

	public void run() {
		setEnabled( mCallback.isModifiedHQM());
	}
}
