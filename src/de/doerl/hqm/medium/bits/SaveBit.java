package de.doerl.hqm.medium.bits;

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
import de.doerl.hqm.medium.MediumUtils;
import de.doerl.hqm.utils.Utils;

class SaveBit extends ASaveFile {
	private static final long serialVersionUID = 7956742276621360019L;
	private static final Logger LOGGER = Logger.getLogger( SaveBit.class.getName());

	public SaveBit( ICallback cb) {
		super( "bit.save", cb);
	}

	public void action( Window frame) {
		FHqm hqm = mCallback.updateHQM();
		if (hqm != null) {
			File src = (File) MediaManager.getProperty( hqm, Medium.HQM_PATH);
			if (src == null) {
				String pfad = getLastOpenDir();
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
				OutputStream os = null;
				try {
					File old = Medium.protectOriginal( src);
					MediumUtils.createBackup( src);
					os = new FileOutputStream( src);
					Medium.writeHQM( hqm, os);
					MediaManager.setProperty( hqm, IMedium.ACTIV_MEDIUM, Medium.MEDIUM);
					mCallback.savedHQMAction();
					Medium.restoreOriginal( old);
				}
				catch (Exception ex) {
					Utils.logThrows( LOGGER, Level.WARNING, ex);
				}
				finally {
					Utils.closeIgnore( os);
				}
			}
		}
	}
}
