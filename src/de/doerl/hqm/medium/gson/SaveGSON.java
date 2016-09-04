package de.doerl.hqm.medium.gson;

import java.awt.Window;
import java.io.File;

import javax.swing.JFileChooser;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ASaveFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.utils.Utils;

public class SaveGSON extends ASaveFile {
//	private static final Logger LOGGER = Logger.getLogger( SaveGSON.class.getName());
	private static final long serialVersionUID = -4187793901939448766L;

	public SaveGSON( ICallback cb) {
		super( "gson.save", cb);
	}

	@Override
	public void action( Window frame) {
		FHqm hqm = mCallback.updateHQM();
		if (hqm != null) {
			File src = (File) MediaManager.getProperty( hqm, Medium.GSON_PATH);
			if (src == null) {
				File last = (File) MediaManager.getProperty( hqm, MediaManager.ACTIV_PATH);
				String pfad = last != null ? last.getAbsolutePath() : getLastOpenDir();
				JFileChooser chooser = createChooser( pfad);
//				chooser.setFileFilter( Medium.FILTER);
				chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
				File base = selectSaveDialog( frame, chooser);
				if (base != null) {
					if (base.exists()) {
						if (!base.isDirectory()) {
							base = base.getParentFile();
						}
						if (base.listFiles().length == 0 || mCallback.askOverwrite()) {
							base.delete();
							setLastHQM( base);
							MediaManager.setProperty( hqm, Medium.GSON_PATH, base);
							src = base;
						}
					}
					else {
						setLastHQM( base);
						MediaManager.setProperty( hqm, Medium.GSON_PATH, base);
						src = base;
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

	@Override
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
