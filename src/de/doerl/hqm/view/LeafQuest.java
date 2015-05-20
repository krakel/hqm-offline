package de.doerl.hqm.view;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JLabel;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.view.AEntity.MultiIcon;

class LeafQuest extends JLabel {
	private static final long serialVersionUID = -2797500791761791369L;
	private static final BufferedImage QUEST_NORM = AEntity.MAP.getSubimage( 170, 0, 25, 30);
	private static final BufferedImage QUEST_BIG = AEntity.MAP.getSubimage( 195, 0, 31, 37);
	private static BufferedImage DARK_BIG = darker( QUEST_BIG, 0.6F);
	private static BufferedImage DARK_NORM = darker( QUEST_NORM, 0.6F);
	private ClickHandler mHandler = new ClickHandler();
	private FQuest mQuest;

	public LeafQuest( FQuest quest) {
		mQuest = quest;
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
//		setBorder( BorderFactory.createLineBorder( Color.MAGENTA));
		update();
		addMouseListener( mHandler);
	}

	private static BufferedImage darker( BufferedImage src, float factor) {
		float[] scales = {
			factor, factor, factor, 1F
		};
		RescaleOp op = new RescaleOp( scales, new float[4], null);
		BufferedImage cc = AEntity.copy( src);
		return op.filter( cc, cc);
	}

	static BufferedImage getDarker( FQuest quest) {
		return quest.isBig() ? DARK_BIG : DARK_NORM;
	}

	static BufferedImage getImage( FQuest quest) {
		return quest.isBig() ? QUEST_BIG : QUEST_NORM;
	}

	public void addClickListener( IClickListener l) {
		mHandler.addClickListener( l);
	}

	public FQuest getQuest() {
		return mQuest;
	}

	public void removeClickListener( IClickListener l) {
		mHandler.removeClickListener( l);
	}

	public void update() {
		int x = AEntity.ZOOM * mQuest.getX();
		int y = AEntity.ZOOM * mQuest.getY();
		int w = AEntity.ZOOM * mQuest.getW();
		int h = AEntity.ZOOM * mQuest.getH();
		setIcon( new MultiIcon( w, h, getImage( mQuest)));
		setBounds( x, y, w, h);
	}
}
