package de.doerl.hqm.medium.json;

import java.awt.Window;
import java.io.File;

import javax.swing.JFileChooser;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ASaveFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.utils.Utils;

class SaveJSON extends ASaveFile {
//	private static final Logger LOGGER = Logger.getLogger( SaveJSON.class.getName());
	private static final long serialVersionUID = -2140887631447065333L;

	public SaveJSON( ICallback cb) {
		super( "json.save", cb);
	}

	public void action( Window frame) {
		FHqm hqm = mCallback.updateHQM();
		if (hqm != null) {
			File src = (File) MediaManager.getProperty( hqm, Medium.JSON_PATH);
			if (src == null) {
				File last = (File) MediaManager.getProperty( hqm, MediaManager.ACTIV_PATH);
				String pfad = last != null ? last.getAbsolutePath() : getLastOpenDir();
				JFileChooser chooser = createChooser( pfad);
				chooser.setFileFilter( Medium.FILTER);
				File choose = selectSaveDialog( frame, chooser);
				if (choose != null) {
					File file = Medium.normalize( choose);
					if (!file.exists() || mCallback.askOverwrite()) {
						setLastHQM( file);
						MediaManager.setProperty( hqm, Medium.JSON_PATH, file);
						src = file;
					}
				}
			}
			if (src != null) {
				if (Medium.saveHQM( hqm, src)) {
					mCallback.savedHQMAction();
				}
			}
		}
	}

	public void run() {
		FHqm hqm = mCallback.updateHQM();
		if (hqm != null) {
			Object medium = MediaManager.getProperty( hqm, MediaManager.ACTIV_MEDIUM);
			setEnabled( mCallback.isModifiedHQM() && Utils.equals( medium, Medium.MEDIUM));
		}
		else {
			setEnabled( false);
		}
	}
}
