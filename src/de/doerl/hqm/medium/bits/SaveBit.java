package de.doerl.hqm.medium.bits;

import java.awt.Window;
import java.io.File;

import javax.swing.JFileChooser;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ASaveFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.MediaManager;

class SaveBit extends ASaveFile {
//	private static final Logger LOGGER = Logger.getLogger( SaveBit.class.getName());
	private static final long serialVersionUID = 7956742276621360019L;

	public SaveBit( ICallback cb) {
		super( "bit.save", cb);
	}

	public void action( Window frame) {
		FHqm hqm = mCallback.updateHQM();
		if (hqm != null) {
			File src = (File) MediaManager.getProperty( hqm, Medium.HQM_PATH);
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
						MediaManager.setProperty( hqm, Medium.HQM_PATH, file);
						src = file;
					}
				}
			}
			if (src != null) {
				if (Medium.saveHqm( hqm, src)) {
					mCallback.savedHQMAction();
				}
			}
		}
	}
}
