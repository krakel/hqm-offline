package de.doerl.hqm.view;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.view.AEntity.MultiIcon;

class LeafQuest extends JLabel {
	private static final long serialVersionUID = -2797500791761791369L;
	private ClickHandler mHandler = new ClickHandler();
	private FQuest mQuest;
	private Type mType = Type.NORM;

	public LeafQuest( FQuest quest) {
		mQuest = quest;
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
//		setBorder( BorderFactory.createLineBorder( Color.MAGENTA));
		update( mType);
		addMouseListener( mHandler);
	}

	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	public FQuest getQuest() {
		return mQuest;
	}

	private MultiIcon getTypeIcon() {
		String key = mType.getKey( mQuest.isBig());
		BufferedImage image = ResourceManager.getImageUI( key);
		int w = AEntity.ZOOM * image.getWidth();
		int h = AEntity.ZOOM * image.getHeight();
		return new MultiIcon( w, h, image);
	}

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}

	public void update() {
		update( mType);
	}

	public void update( Type type) {
		if (type != Type.NORM) {
			mType = type;
		}
		else if (mQuest.mRequirements.isEmpty()) {
			mType = Type.NORM;
		}
		else {
			mType = Type.DARK;
		}
		int x = AEntity.ZOOM * mQuest.getX();
		int y = AEntity.ZOOM * mQuest.getY();
		int w = AEntity.ZOOM * mQuest.getW();
		int h = AEntity.ZOOM * mQuest.getH();
		setIcon( getTypeIcon());
		setBounds( x, y, w, h);
	}

	public static enum Type {
		NORM( "hqm.quest.norm", "hqm.quest.big"),
		DARK( "hqm.dark.norm", "hqm.dark.big"),
		BASE( "hqm.base.norm", "hqm.base.big"),
		PREF( "hqm.pref.norm", "hqm.pref.big"),
		POST( "hqm.post.norm", "hqm.post.big");
		private String mBigKey;
		private String mNormKey;

		private Type( String normKey, String bigKey) {
			mNormKey = normKey;
			mBigKey = bigKey;
		}

		String getKey( boolean big) {
			return big ? mBigKey : mNormKey;
		}
	}
}
