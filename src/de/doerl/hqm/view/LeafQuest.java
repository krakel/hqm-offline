package de.doerl.hqm.view;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JLabel;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.view.AEntity.MultiIcon;

class LeafQuest extends JLabel {
	private static final long serialVersionUID = -2797500791761791369L;
	private static final BufferedImage QUEST_NORM = AEntity.MAP.getSubimage( 170, 0, 25, 30);
	private static final BufferedImage QUEST_BIG = AEntity.MAP.getSubimage( 195, 0, 31, 37);
	private static final BufferedImage DARK_BIG = darker( QUEST_BIG, 0.6F);
	private static final BufferedImage DARK_NORM = darker( QUEST_NORM, 0.6F);
	private static final BufferedImage ACTIV_BIG = colored( QUEST_BIG, 0.6F, 1F, 0.6F);
	private static final BufferedImage ACTIV_NORM = colored( QUEST_NORM, 0.6F, 1F, 0.6F);
	private static final BufferedImage PREF_BIG = colored( QUEST_BIG, 0.6F, 0.6F, 1F);
	private static final BufferedImage PREF_NORM = colored( QUEST_NORM, 0.6F, 0.6F, 1F);
	private static final BufferedImage POST_BIG = colored( QUEST_BIG, 1F, 0.6F, 0.6F);
	private static final BufferedImage POST_NORM = colored( QUEST_NORM, 1F, 0.6F, 0.6F);
	private ClickHandler mHandler = new ClickHandler();
	private FQuest mQuest;

	public LeafQuest( FQuest quest) {
		mQuest = quest;
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
//		setBorder( BorderFactory.createLineBorder( Color.MAGENTA));
		update( Type.NORM);
		addMouseListener( mHandler);
	}

	static BufferedImage colored( BufferedImage src, float r, float g, float b) {
		float[] scales = {
			r, g, b, 1F
		};
		RescaleOp op = new RescaleOp( scales, new float[4], null);
		BufferedImage cc = AEntity.copy( src);
		return op.filter( cc, cc);
	}

	static BufferedImage darker( BufferedImage src, float factor) {
		float[] scales = {
			factor, factor, factor, 1F
		};
		RescaleOp op = new RescaleOp( scales, new float[4], null);
		BufferedImage cc = AEntity.copy( src);
		return op.filter( cc, cc);
	}

	static BufferedImage getActiv( FQuest quest) {
		return quest.isBig() ? ACTIV_BIG : ACTIV_NORM;
	}

	static BufferedImage getDarker( FQuest quest) {
		return quest.isBig() ? DARK_BIG : DARK_NORM;
	}

	static BufferedImage getImage( FQuest quest) {
		return quest.isBig() ? QUEST_BIG : QUEST_NORM;
	}

	static BufferedImage getPost( FQuest quest) {
		return quest.isBig() ? POST_BIG : POST_NORM;
	}

	static BufferedImage getPref( FQuest quest) {
		return quest.isBig() ? PREF_BIG : PREF_NORM;
	}

	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	public FQuest getQuest() {
		return mQuest;
	}

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}

	public void update( Type type) {
		int x = AEntity.ZOOM * mQuest.getX();
		int y = AEntity.ZOOM * mQuest.getY();
		int w = AEntity.ZOOM * mQuest.getW();
		int h = AEntity.ZOOM * mQuest.getH();
		BufferedImage image;
		switch (type) {
			case DARK:
				image = getDarker( mQuest);
				break;
			case ACTIV:
				image = getActiv( mQuest);
				break;
			case PREF:
				image = getPref( mQuest);
				break;
			case POST:
				image = getPost( mQuest);
				break;
			default:
				image = mQuest.mRequirements.isEmpty() ? getImage( mQuest) : getDarker( mQuest);
				break;
		}
		setIcon( new MultiIcon( w, h, image));
		setBounds( x, y, w, h);
	}

	public enum Type {
		NORM,
		DARK,
		ACTIV,
		PREF,
		POST;
	}
}
