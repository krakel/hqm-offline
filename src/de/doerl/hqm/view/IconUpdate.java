package de.doerl.hqm.view;

import java.awt.Image;

import javax.swing.JLabel;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.utils.mods.ImageLoader;
import de.doerl.hqm.utils.mods.ItemNEI;
import de.doerl.hqm.utils.nbt.FCompound;

public class IconUpdate implements Runnable {
	private JLabel mLbl;
	private Image mBack;
	private double mZoom;
	private String mKey;
	private FCompound mNbt;
	private String mTxt;
	private boolean mHide;

	IconUpdate( JLabel lbl, Image back, String key, FCompound nbt, double zoom, String txt, boolean hide) {
		mLbl = lbl;
		mBack = back;
		mZoom = zoom;
		mKey = key;
		mNbt = nbt;
		mTxt = txt;
		mHide = hide;
	}

	public static void create( JLabel lbl, AStack stk, FCompound nbt, double zoom, String txt) {
		if (stk != null) {
			create( lbl, null, zoom, stk.getKey(), nbt, txt, false);
		}
		else {
			create( lbl, null, zoom, null, null, txt, false);
		}
	}

	public static void create( JLabel lbl, FItemStack stk, double zoom, String txt) {
		if (stk != null) {
			create( lbl, null, zoom, stk.getKey(), stk.getNBT(), txt, false);
		}
		else {
			create( lbl, null, zoom, null, null, txt, false);
		}
	}

	public static void create( JLabel lbl, Image back, double zoom, String key, FCompound nbt, String txt, boolean hide) {
		IconUpdate cb = new IconUpdate( lbl, back, key, nbt, zoom, txt, hide);
		cb.update( cb);
	}

	public static void create( JLabel lbl, Image back, FItemStack stk, double zoom, String txt) {
		if (stk != null) {
			create( lbl, back, zoom, stk.getKey(), stk.getNBT(), txt, false);
		}
		else {
			create( lbl, back, zoom, null, null, null, false);
		}
	}

	public static void create( JLabel lbl, ItemNEI item) {
		if (item != null) {
			create( lbl, null, 0.6, item.mKey, item.getNBT(), null, true);
		}
		else {
			create( lbl, null, 0.6, null, null, null, true);
		}
	}

	@Override
	public void run() {
		update( null);
	}

	void update( Runnable cb) {
		Image img = ImageLoader.getImage( cb, mKey, mNbt);
		if (img != null) {
			mLbl.setVisible( true);
		}
		else if (mHide) {
			mLbl.setVisible( false);
		}
		else {
			img = StackIcon.ICON_UNKNOWN;
		}
		if (mBack != null) {
			mLbl.setIcon( new StackIcon( mBack, img, mZoom, mTxt, 1));
		}
		else {
			mLbl.setIcon( new StackIcon( img, mZoom, mTxt));
		}
	}
}
