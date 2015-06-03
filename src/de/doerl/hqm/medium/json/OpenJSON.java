package de.doerl.hqm.medium.json;

import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.AOpenFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IMedium;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.utils.Utils;

class OpenJSON extends AOpenFile {
	private static final long serialVersionUID = 2810099814270240119L;
	private static final Logger LOGGER = Logger.getLogger( OpenJSON.class.getName());

	public OpenJSON( ICallback cb) {
		super( "json.open", cb);
		setEnabled( true);
	}

	private static boolean verifyLastPipeDef( File file) {
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
		if (file != null && verifyLastPipeDef( file)) {
			InputStream is = null;
			try {
				String name = Medium.toName( file);
				FHqm hqm = new FHqm( name);
				is = new FileInputStream( file);
				Medium.readHqm( hqm, is);
				MediaManager.setProperty( hqm, Medium.JSON_PATH, file);
				MediaManager.setProperty( hqm, IMedium.ACTIV_MEDIUM, Medium.MEDIUM);
				mCallback.openHQMAction( hqm);
			}
			catch (Exception ex) {
				Utils.logThrows( LOGGER, Level.FINER, ex);
			}
			finally {
				Utils.closeIgnore( is);
			}
		}
	}

	@Override
	public void run() {
		setEnabled( mCallback.canOpenHQM());
	}
}
