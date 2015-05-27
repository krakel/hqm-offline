package de.doerl.hqm.view;

import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.utils.ImageLoader;
import de.doerl.hqm.utils.ResourceManager;

class LeafQuest extends JLabel {
	private static final long serialVersionUID = -2797500791761791369L;
	private ClickHandler mHandler = new ClickHandler();
	private FQuest mQuest;
	private Type mType = Type.NORM;
	private Runnable mCallback = new Runnable() {
		@Override
		public void run() {
			updateIcon( null);
			Container parent = getParent();
			if (parent != null) {
				parent.repaint();
			}
		}
	};

	public LeafQuest( FQuest quest) {
		mQuest = quest;
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		addMouseListener( mHandler);
	}

	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	private StackIcon createIcon( Image img) {
		String key = mType.getKey( mQuest.isBig());
		Image back = ResourceManager.getImageUI( key);
		return new StackIcon( back, img);
	}

	public FQuest getQuest() {
		return mQuest;
	}

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}

	public void update( Type type) {
		updateType( type);
		updateBounds();
		updateIcon( mCallback);
	}

	private void updateBounds() {
		boolean big = mQuest.isBig();
		int x = AEntity.ZOOM * mQuest.getX();
		int y = AEntity.ZOOM * mQuest.getY();
		int w = AEntity.ZOOM * ResourceManager.getW( big);
		int h = AEntity.ZOOM * ResourceManager.getH( big);
		setBounds( x, y, w, h);
	}

	private void updateIcon( Runnable cb) {
		AStack stk = mQuest.mIcon.mValue;
		if (stk == null) {
			setIcon( createIcon( ResourceManager.getImageUI( "hqm.unknown")));
		}
		else {
			Image img = ImageLoader.getImage( stk.getName(), stk.getDamage(), cb);
			if (img != null) {
				setIcon( createIcon( img));
			}
			else {
				setIcon( createIcon( ResourceManager.getImageUI( "hqm.unknown")));
			}
		}
	}

	private void updateType( Type type) {
		if (type != Type.NORM) {
			mType = type;
		}
		else if (mQuest.isFree()) {
			mType = Type.NORM;
		}
		else {
			mType = Type.DARK;
		}
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
