package de.doerl.hqm.ui;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Comparator;

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
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.nbt.FCompound;

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

	abstract void saveFile( FHqm hqm, File file);

	abstract File suggest( String name);

	@Override
	public void updateAction( RefreshEvent event) {
		SwingUtilities.invokeLater( this);
	}

	protected static final class Item {
		public String mName;
		public FCompound mNBT;
		public int mDmg;

		public Item( String name, int dmg, FCompound nbt) {
			mName = name;
			mDmg = dmg;
			mNBT = nbt;
		}

		@Override
		public boolean equals( Object obj) {
			if (obj == null) {
				return false;
			}
			if (this == obj) {
				return true;
			}
			try {
				Item other = (Item) obj;
				if (mDmg != other.mDmg) {
					return false;
				}
				if (Utils.different( mName, other.mName)) {
					return false;
				}
				if (Utils.different( mNBT, other.mNBT)) {
					return false;
				}
			}
			catch (ClassCastException ex) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + mDmg;
			result = prime * result + (mNBT == null ? 0 : mNBT.hashCode());
			result = prime * result + (mName == null ? 0 : mName.hashCode());
			return result;
		}
	}

	protected static final class ItemCompare implements Comparator<Item> {
		@Override
		public int compare( Item o1, Item o2) {
			if (o1.mName == null && o2.mName != null) {
				return -1;
			}
			if (o1.mName != null && o2.mName == null) {
				return 1;
			}
			if (o1.mName == null || o1.mName.equals( o2.mName)) {
				if (o1.mDmg != o2.mDmg) {
					return o1.mDmg < o2.mDmg ? -1 : 1;
				}
				if (o1.mNBT == null && o2.mNBT != null) {
					return -1;
				}
				if (o1.mNBT != null && o2.mNBT == null) {
					return 1;
				}
				if (o1.mNBT == null || o1.mNBT.equals( o2.mNBT)) {
					return 0;
				}
				return o1.mNBT.toString().compareTo( o2.mNBT.toString());
			}
			return o1.mName.compareTo( o2.mName);
		}
	}
}
