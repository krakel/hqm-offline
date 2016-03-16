package de.doerl.hqm.ui;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ADialogFile;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IRefreshListener;
import de.doerl.hqm.medium.MediaManager;
import de.doerl.hqm.medium.RefreshEvent;
import de.doerl.hqm.utils.Security;

abstract class AReport extends ABundleAction implements IRefreshListener, Runnable {
	private static final long serialVersionUID = -3001796596162739183L;
	public static final FileFilter FILTER = new FileNameExtensionFilter( "txt file", "txt");
	protected static final String NL = Security.getProperty( "line.separator", "\n");
	protected ICallback mCallback;

	public AReport( String name, ICallback cb) {
		super( name);
		mCallback = cb;
		cb.addRefreshListener( this);
		setEnabled( false);
	}

	protected static String truncName( String name, String suffix) {
		if (name.toLowerCase().endsWith( suffix)) {
			return name.substring( 0, name.length() - suffix.length());
		}
		return name;
	}

	@Override
	public void action( Window frame) {
		FHqm hqm = mCallback.updateHQM();
		if (hqm != null) {
			File last = (File) MediaManager.getProperty( hqm, MediaManager.ACTIV_PATH);
			String pfad = last != null ? last.getAbsolutePath() : ADialogFile.getLastOpenDir();
			JFileChooser chooser = ADialogFile.createChooser( pfad);
			chooser.setFileFilter( FILTER);
			chooser.setSelectedFile( suggest( hqm.mName));
			File choose = ADialogFile.selectSaveDialog( frame, chooser);
			if (choose != null) {
				File file = normalize( choose);
				if (!file.exists() || mCallback.askOverwrite()) {
					saveFile( hqm, file);
				}
			}
		}
	}

	@Override
	public void actionPerformed( ActionEvent event) {
		Window frame = ADialog.getParentFrame( (Container) event.getSource());
		action( frame);
	}

	abstract File normalize( File choose);

	@Override
	public void run() {
		FHqm hqm = mCallback.updateHQM();
		setEnabled( hqm != null);
	}

	abstract boolean saveFile( FHqm hqm, File file);

	abstract File suggest( String name);

	@Override
	public void updateAction( RefreshEvent event) {
		SwingUtilities.invokeLater( this);
	}
}
