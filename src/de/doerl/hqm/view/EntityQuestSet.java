package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.Utils;

public class EntityQuestSet extends AEntity<FQuestSet> {
	private static final long serialVersionUID = 4427035968994904913L;
	private static final BufferedImage QUEST_NORM = MAP.getSubimage( 170, 0, 25, 30);
	private static final BufferedImage QUEST_BIG = MAP.getSubimage( 195, 0, 31, 37);
	private static BufferedImage DARK_BIG = darker( QUEST_BIG, 0.6F);
	private static BufferedImage DARK_NORM = darker( QUEST_NORM, 0.6F);
	private FQuestSet mQS;
	private JPanel mLeaf = new LeafAbsolute();

	public EntityQuestSet( EditView view, FQuestSet qs) {
		super( view, new GridLayout( 1, 1));
		mQS = qs;
		add( mLeaf);
		LineFactory.set( qs, mLeaf);
		QuestFactory.set( qs, mLeaf);
	}

	private static BufferedImage darker( BufferedImage src, float factor) {
		float[] scales = {
			factor, factor, factor, 1F
		};
		RescaleOp op = new RescaleOp( scales, new float[4], null);
		BufferedImage cc = copy( src);
		return op.filter( cc, cc);
	}

	private static int getCenterX( FQuest quest) {
		return getX( quest) + getW( quest) / 2;
	}

	private static int getCenterY( FQuest quest) {
		return getY( quest) + getH( quest) / 2;
	}

	private static BufferedImage getDarker( FQuest quest) {
		return quest.mBig.mValue ? DARK_BIG : DARK_NORM;
	}

	private static int getH( FQuest quest) {
		return 2 * getImage( quest).getHeight();
	}

	private static BufferedImage getImage( FQuest quest) {
		return quest.mBig.mValue ? QUEST_BIG : QUEST_NORM;
	}

	private static int getW( FQuest quest) {
		return 2 * getImage( quest).getWidth();
	}

	private static int getX( FQuest quest) {
		if (quest.mBig.mValue) {
			return 2 * quest.mX.mValue;
		}
		else {
			return 2 * (quest.mX.mValue + 1);
		}
	}

	private static int getY( FQuest quest) {
		if (quest.mBig.mValue) {
			return 2 * quest.mY.mValue;
		}
		else {
			return 2 * (quest.mY.mValue + 1);
		}
	}

	@Override
	public FQuestSet getBase() {
		return mQS;
	}

	private static class LineFactory extends AHQMWorker<Object, Object> {
		private static final Color LINE_COLOR = new Color( 0xff404040);
		private FQuestSet mSet;
		private JPanel mLeaf;

		private LineFactory( FQuestSet qs, JPanel leaf) {
			mSet = qs;
			mLeaf = leaf;
		}

		public static void set( FQuestSet qs, JPanel leaf) {
			LineFactory worker = new LineFactory( qs, leaf);
			qs.mParentCategory.mParentHQM.forEachQuest( worker, leaf);
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			if (Utils.equals( quest.mSet, mSet)) {
				for (FQuest req : quest.mRequirements) {
					if (req != null && Utils.equals( quest.mSet, req.mSet)) {
						mLeaf.add( new LeafLine( getCenterX( quest), getCenterY( quest), getCenterX( req), getCenterY( req), 5, LINE_COLOR));
					}
				}
			}
			return null;
		}
	}

	private static class QuestFactory extends AHQMWorker<Object, Object> {
		private FQuestSet mSet;
		private JPanel mLeaf;

		private QuestFactory( FQuestSet qs, JPanel leaf) {
			mSet = qs;
			mLeaf = leaf;
		}

		public static void set( FQuestSet qs, JPanel leaf) {
			QuestFactory worker = new QuestFactory( qs, leaf);
			qs.mParentCategory.mParentHQM.forEachQuest( worker, leaf);
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			if (Utils.equals( quest.mSet, mSet)) {
				int x = getX( quest);
				int y = getY( quest);
				int w = getW( quest);
				int h = getH( quest);
				JLabel lbl = leafImage( x, y, w, h, getDarker( quest));
				mLeaf.add( lbl);
				mLeaf.setComponentZOrder( lbl, 0);
			}
			return null;
		}
	}
}
