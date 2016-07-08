package de.doerl.hqm.view;

import java.awt.Image;

import javax.swing.JLabel;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.utils.mods.ImageLoader;
import de.doerl.hqm.utils.mods.Matcher;

public class IconUpdate implements Runnable {
	private JLabel mLbl;
	private Image mBack;
	private double mZoom;
	private String mKey;
	private String mTxt;
	private boolean mHide;

	IconUpdate( JLabel lbl, Image back, String key, double zoom, String txt, boolean hide) {
		mLbl = lbl;
		mBack = back;
		mZoom = zoom;
		mKey = key;
		mTxt = txt;
		mHide = hide;
	}

	public static void create( JLabel lbl, AStack stk, double zoom, String txt) {
		if (stk != null) {
			create( lbl, null, zoom, stk.getKey(), txt, false);
		}
		else {
			create( lbl, null, zoom, null, txt, false);
		}
	}

	public static void create( JLabel lbl, Image back, AStack stk, double zoom, String txt) {
		if (stk != null) {
			create( lbl, back, zoom, stk.getKey(), txt, false);
		}
		else {
			create( lbl, back, zoom, null, null, false);
		}
	}

	public static void create( JLabel lbl, Image back, double zoom, String key, String txt, boolean hide) {
		IconUpdate cb = new IconUpdate( lbl, back, key, zoom, txt, hide);
		cb.update( cb);
	}

	public static void create( JLabel lbl, Matcher match) {
		if (match != null) {
			create( lbl, null, 0.6, match.mKey, null, true);
		}
		else {
			create( lbl, null, 0.6, null, null, true);
		}
	}

	@Override
	public void run() {
		update( null);
	}

	void update( Runnable cb) {
		Image img = ImageLoader.getImage( mKey, cb);
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
