package de.doerl.hqm.view;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.Icon;
import javax.swing.JLabel;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.view.AEntity.MultiIcon;

class LeafQuest extends JLabel {
	private static final long serialVersionUID = -2797500791761791369L;
	private static final BufferedImage QUEST_NORM = AEntity.MAP.getSubimage( 170, 0, 25, 30);
	private static final BufferedImage QUEST_BIG = AEntity.MAP.getSubimage( 195, 0, 31, 37);
	private static BufferedImage DARK_BIG = darker( QUEST_BIG, 0.6F);
	private static BufferedImage DARK_NORM = darker( QUEST_NORM, 0.6F);

//	private ClickHandler mHandler = new ClickHandler();
	public LeafQuest( FQuest quest) {
		int x = quest.getX();
		int y = quest.getY();
		int w = quest.getW();
		int h = quest.getH();
		MultiIcon icon = new MultiIcon( w, h, getImage( quest));
		init( x, y, w, h, icon);
	}

	public LeafQuest( int x, int y, int w, int h, BufferedImage... arr) {
		MultiIcon icon = new MultiIcon( w, h, arr);
		init( x, y, w, h, icon);
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

//	public ClickListener getHandler() {
//		return mHandler.getListener();
//	}
//
	private void init( int x, int y, int w, int h, Icon icon) {
		setIcon( icon);
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
//		setBorder( BorderFactory.createLineBorder( Color.MAGENTA));
		setBounds( x, y, w, h);
//		addMouseListener( mHandler);
//		mHandler.getListener().addClickListener( this);
	}
}
