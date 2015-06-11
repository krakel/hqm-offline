package de.doerl.hqm.medium.json;

import java.awt.Window;
import java.io.File;

import javax.swing.JFileChooser;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ASaveAsFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.MediaManager;

class SaveAsJSON extends ASaveAsFile {
//	private static final Logger LOGGER = Logger.getLogger( SaveAsJSON.class.getName());
	private static final long serialVersionUID = -1904996548131026440L;

	public SaveAsJSON( ICallback cb) {
		super( "json.saveAs", cb);
	}

	@Override
	public void action( Window frame) {
		FHqm hqm = mCallback.updateHQM();
		if (hqm != null) {
			File last = (File) MediaManager.getProperty( hqm, MediaManager.ACTIV_PATH);
			String pfad = last != null ? last.getAbsolutePath() : getLastOpenDir();
			JFileChooser chooser = createChooser( pfad);
			chooser.setFileFilter( Medium.FILTER);
			chooser.setSelectedFile( Medium.suggest( hqm.mName));
			File choose = selectSaveDialog( frame, chooser);
			if (choose != null) {
				File file = Medium.normalize( choose);
				if (!file.exists() || mCallback.askOverwrite()) {
					setLastHQM( file);
					if (Medium.saveHQM( hqm, file)) {
						mCallback.savedHQMAction();
					}
				}
			}
		}
	}
}
