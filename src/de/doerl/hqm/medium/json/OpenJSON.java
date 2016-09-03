package de.doerl.hqm.medium.json;

import java.awt.Window;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.AOpenFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.utils.Utils;

class OpenJSON extends AOpenFile {
	private static final long serialVersionUID = 2810099814270240119L;
	private static final Logger LOGGER = Logger.getLogger( OpenJSON.class.getName());

	public OpenJSON( ICallback cb) {
		super( "json.open", cb);
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
			FHqm hqm = Medium.loadHqm( file);
			if (hqm != null) {
				mCallback.openHQMAction( hqm);
			}
		}
	}

	@Override
	public void run() {
		setEnabled( mCallback.canOpenHQM());
	}
}
