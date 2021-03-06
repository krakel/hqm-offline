package de.doerl.hqm.medium.gson;

import java.awt.Window;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.AOpenFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.utils.Utils;

class OpenGSON extends AOpenFile {
	private static final long serialVersionUID = 7035477705122951087L;
	private static final Logger LOGGER = Logger.getLogger( OpenGSON.class.getName());

	public OpenGSON( ICallback cb) {
		super( "gson.open", cb);
		setEnabled( true);
	}

	private static boolean verifyLastHQM( File file) {
		if (file.exists()) {
			setLastHQM( file);
			return true;
		}
		Utils.log( LOGGER, Level.FINER, "Common.error {0}", file);
		return false;
	}

	@Override
	public void action( Window frame) {
		String pfad = getLastOpenDir();
		JFileChooser chooser = createChooser( pfad);
//		chooser.setFileFilter( Medium.FILTER);
		chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
		File base = selectOpenDialog( frame, chooser);
		if (base != null) {
			if (base != null && verifyLastHQM( base)) {
				FHqm hqm = Medium.loadHqm( base);
				if (hqm != null) {
					mCallback.openHQMAction( hqm);
				}
			}
		}
	}

	@Override
	public void run() {
		setEnabled( mCallback.canOpenHQM());
	}
}
