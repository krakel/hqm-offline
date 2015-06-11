package de.doerl.hqm.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.medium.ICallback;
import de.doerl.hqm.medium.IMedium;
import de.doerl.hqm.medium.IRefreshListener;
import de.doerl.hqm.medium.MediumOfFile;
import de.doerl.hqm.medium.RefreshEvent;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

class OpenLastAction extends ABundleAction implements IRefreshListener, Runnable {
	private static final long serialVersionUID = -5227055618865128699L;
	private static final Logger LOGGER = Logger.getLogger( OpenLastAction.class.getName());
	private ICallback mCallback;
	private JMenuItem mItem;
	private int mIndex;

	public OpenLastAction( int idx, ICallback cb) {
		super( "hqm.open");
		mIndex = idx;
		mCallback = cb;
		cb.addRefreshListener( this);
		SwingUtilities.invokeLater( this);
	}

	@Override
	public void action( Window frame) {
	}

	@Override
	public void actionPerformed( ActionEvent e) {
		String path = PreferenceManager.getArrayString( BaseDefaults.LAST_OPEN, mIndex);
		IMedium m = MediumOfFile.get( path);
		FHqm hqm = m.openHqm( new File( path));
		if (hqm != null) {
			PreferenceManager.deleteArrayString( BaseDefaults.LAST_OPEN, path);
			PreferenceManager.addArrayString( BaseDefaults.LAST_OPEN, 0, path);
			mCallback.openHQMAction( hqm);
		}
		else {
			Utils.log( LOGGER, Level.INFO, "Can not open {0}", path);
		}
	}

	@Override
	public void run() {
		String file = PreferenceManager.getArrayString( BaseDefaults.LAST_OPEN, mIndex);
		if (file != null) {
			IMedium m = MediumOfFile.get( file);
			putValue( SMALL_ICON, ResourceManager.getIcon( m.getIcon()));
			putValue( NAME, file);
		}
		else {
			putValue( NAME, "");
		}
		if (mItem != null) {
			mItem.setVisible( file != null);
		}
	}

	public void setItem( JMenuItem item) {
		mItem = item;
	}

	@Override
	public void updateAction( RefreshEvent ev) {
		SwingUtilities.invokeLater( this);
	}
}
