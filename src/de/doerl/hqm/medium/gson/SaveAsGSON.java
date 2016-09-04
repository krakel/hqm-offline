package de.doerl.hqm.medium.gson;

import java.awt.Window;
import java.io.File;

import javax.swing.JFileChooser;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ASaveAsFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.MediaManager;

public class SaveAsGSON extends ASaveAsFile {
	//	private static final Logger LOGGER = Logger.getLogger( SaveAsGSON.class.getName());
	private static final long serialVersionUID = -1499924448908051432L;

	public SaveAsGSON( ICallback cb) {
		super( "gson.saveAs", cb);
	}

	@Override
	public void action( Window frame) {
		FHqm hqm = mCallback.updateHQM();
		if (hqm != null) {
			File last = (File) MediaManager.getProperty( hqm, MediaManager.ACTIV_PATH);
			String pfad = last != null ? last.getAbsolutePath() : getLastOpenDir();
			JFileChooser chooser = createChooser( pfad);
//			chooser.setFileFilter( Medium.FILTER);
			chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
			chooser.setSelectedFile( Medium.suggest( hqm.mName));
			File base = selectSaveDialog( frame, chooser);
			if (base != null) {
				if (base.exists()) {
					if (!base.isDirectory()) {
						base = base.getParentFile();
					}
					if (base.listFiles().length == 0 || mCallback.askOverwrite()) {
						base.delete();
						writeHQM( hqm, base);
					}
				}
				else {
					writeHQM( hqm, base);
				}
			}
		}
	}

	private void writeHQM( FHqm hqm, File base) {
		setLastHQM( base);
		if (Medium.saveHQM( hqm, base)) {
			mCallback.savedHQMAction();
		}
	}
}
