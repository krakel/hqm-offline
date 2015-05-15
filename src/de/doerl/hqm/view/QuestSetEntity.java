package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JPanel;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.Utils;

public class QuestSetEntity extends AEntity<FQuestSet> {
	private static final long serialVersionUID = 4427035968994904913L;
	private static final BufferedImage QUEST_NORM = MAP.getSubimage( 170, 0, 25, 30);
	private static final BufferedImage QUEST_BIG = MAP.getSubimage( 195, 0, 31, 37);
	private FQuestSet mQS;
	private JPanel mLeaf = leafPanelAbsolut();

	public QuestSetEntity( EditView view, FQuestSet qs) {
		super( view, new GridLayout( 1, 1));
		mQS = qs;
		add( mLeaf);
		LineFactory.set( qs, mLeaf);
		QuestFactory.set( qs, mLeaf);
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
						mLeaf.add( leafLine( 2 * quest.getCenterX(), 2 * quest.getCenterY(), 2 * req.getCenterX(), 2 * req.getCenterY(), 5, LINE_COLOR));
					}
				}
			}
			return null;
		}
	}

	private static class QuestFactory extends AHQMWorker<Object, Object> {
		private static BufferedImage DARK_BIG = darker( QUEST_BIG, 0.6F);
		private static BufferedImage DARK_NORM = darker( QUEST_NORM, 0.6F);
		private FQuestSet mSet;
		private JPanel mLeaf;

		private QuestFactory( FQuestSet qs, JPanel leaf) {
			mSet = qs;
			mLeaf = leaf;
		}

		private static BufferedImage darker( BufferedImage src, float factor) {
			float[] scales = {
				factor, factor, factor, 1F
			};
			RescaleOp op = new RescaleOp( scales, new float[4], null);
			BufferedImage cc = copy( src);
			return op.filter( cc, cc);
		}

		public static void set( FQuestSet qs, JPanel leaf) {
			QuestFactory worker = new QuestFactory( qs, leaf);
			qs.mParentCategory.mParentHQM.forEachQuest( worker, leaf);
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			if (Utils.equals( quest.mSet, mSet)) {
				boolean big = quest.mBig.mValue;
				int x = quest.mX.mValue;
				int y = quest.mY.mValue;
				if (big) {
					++x;
					++y;
				}
				int w = quest.getW();
				int h = quest.getH();
				mLeaf.add( leafImage( 2 * x, 2 * y, 2 * w, 2 * h, big ? DARK_BIG : DARK_NORM));
			}
			return null;
		}
	}
}
