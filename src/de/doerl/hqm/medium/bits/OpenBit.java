package de.doerl.hqm.medium.bits;

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.AOpenFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IMedium;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.medium.MediumUtils;
import de.doerl.hqm.utils.Utils;

class OpenBit extends AOpenFile {
	private static final long serialVersionUID = 4850961276225686266L;
	private static final Logger LOGGER = Logger.getLogger( OpenBit.class.getName());

	public OpenBit( ICallback cb) {
		super( "bit.open", cb);
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
		chooser.setFileFilter( Medium.FILTER);
		File file = selectOpenDialog( frame, chooser);
		if (file != null && verifyLastHQM( file)) {
			try {
				FHqm hqm = new FHqm( file);
				InputStream is = MediumUtils.getSource( file);
				if (Medium.readHqm( hqm, is, mCallback)) {
					MediaManager.setProperty( hqm, Medium.HQM_PATH, file);
					MediaManager.setProperty( hqm, IMedium.ACTIV_MEDIUM, Medium.MEDIUM);
					mCallback.openHQMAction( hqm);
				}
			}
			catch (IOException ex) {
				Utils.logThrows( LOGGER, Level.FINER, ex);
			}
		}
	}

	@Override
	public void run() {
		setEnabled( mCallback.canOpenHQM());
	}
}
