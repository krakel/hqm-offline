package de.doerl.hqm.medium;

import java.awt.Window;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import de.doerl.hqm.utils.Utils;

public class OpenFile extends AOpenFile {
	private static final long serialVersionUID = -6906125595603507775L;
	private static final Logger LOGGER = Logger.getLogger( OpenFile.class.getName());

	public OpenFile( ICallback cb) {
		super( "action.dialog.select.file", cb);
		setEnabled( mCallback.canOpenDialog());
	}

	private static boolean verifyLastOpen( File file) {
		if (file.exists()) {
			setLastOpen( file);
			return true;
		}
		Utils.log( LOGGER, Level.FINER, "Common.error {0}", file);
		return false;
	}

	public void action( Window frame) {
		String path = getLastOpen();
		JFileChooser chooser = createChooser( path);
		File file = selectOpenDialog( frame, chooser);
		if (file != null) {
			verifyLastOpen( file);
			mCallback.openDialogAction( "file://" + file.getPath());
		}
	}

	public void run() {
		setEnabled( mCallback.canOpenDialog());
	}
}
