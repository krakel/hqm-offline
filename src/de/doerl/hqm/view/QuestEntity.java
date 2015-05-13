package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FParameterStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.dispatch.AHQMWorker;

public class QuestEntity extends AEntity<FQuest> {
	private static final long serialVersionUID = -5707664232506407627L;
	protected static final BufferedImage REPUATION = MAP.getSubimage( 30, 82, 16, 16);
	protected static final BufferedImage REP_GOOD = MAP.getSubimage( 78, 82, 16, 16);
	protected static final BufferedImage REP_BAD = MAP.getSubimage( 94, 82, 16, 16);
	protected static final BufferedImage REP_NORM = MAP.getSubimage( 110, 82, 16, 16);
	private FQuest mQuest;
	private JPanel mLeafLeft = leafPanel( true);
	private JPanel mLeafRight = leafPanel( false);
	private JLabel mTitle = leafTitle( "");
	private JTextArea mDesc = leafTextArea();
	private DefaultListModel<AQuestTask> mTaskModel = new DefaultListModel<AQuestTask>();
	private JList<AQuestTask> mTasks;
	private JLabel mRewards = leafTitle( "Rewards");
	private JLabel mPickOne = leafTitle( "Pick one");
	private JPanel mRewardList = leafBox( BoxLayout.X_AXIS);
	private JPanel mPickOneList = leafBox( BoxLayout.X_AXIS);
	private JLabel mReputation;

	public QuestEntity( EditView view, FQuest quest) {
		super( view, new GridLayout( 1, 2));
		mQuest = quest;
		createLeft( mLeafLeft, quest);
		createRight( mLeafRight);
		add( mLeafLeft);
		add( mLeafRight);
		QuestTaskFactory.get( quest, mTaskModel);
	}

	private void createIconList( JPanel panel, Vector<FParameterStack> list, JLabel btn) {
		panel.setPreferredSize( new Dimension( Integer.MAX_VALUE, ICON_SIZE));
		for (FParameterStack stk : list) {
			int amount = stk.mValue.getAmount();
			JLabel lbl = new JLabel( new CountIcon( ICON_BACK, amount > 1 ? Integer.toString( amount) : null));
			lbl.setBorder( null);
			panel.add( lbl);
			panel.add( Box.createHorizontalStrut( 3));
		}
		panel.add( Box.createHorizontalGlue());
		if (btn != null) {
			panel.add( btn);
		}
	}

	private void createLeft( JPanel leaf, FQuest quest) {
		mTitle.setText( quest.getName());
		mDesc.setText( quest.mDesc.mValue);
		mTasks = leafList( mTaskModel);
		mTasks.setCellRenderer( new CellRenderer());
		leaf.add( mTitle);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( leafScoll( mDesc, 140));
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( leafScoll( mTasks, 80));
//		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( Box.createVerticalGlue());
		leaf.add( mRewards);
		mReputation = leafImage( new BufferedImage[] {
			REPUATION, ReputationFactory.get( quest)
		});
		createIconList( mRewardList, quest.mRewards, mReputation);
		leaf.add( mRewardList);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( mPickOne);
		createIconList( mPickOneList, quest.mChoices, leafButton( "Claim reward"));
		leaf.add( mPickOneList);
	}

	private void createRight( JPanel leaf) {
		leaf.setBackground( Color.RED);
	}

	@Override
	public FQuest getBase() {
		return mQuest;
	}

	private static class CellRenderer extends JPanel implements ListCellRenderer<AQuestTask> {
		private static final long serialVersionUID = 9081558438188872705L;
		private JLabel mTitle = leafTitle( UNSELECTED, "");

		public CellRenderer() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
			add( mTitle);
			setOpaque( false);
		}

		public Component getListCellRendererComponent( JList<? extends AQuestTask> list, AQuestTask qs, int index, boolean isSelected, boolean cellHasFocus) {
			mTitle.setText( qs.getName());
			mTitle.setForeground( isSelected ? SELECTED : UNSELECTED);
			return this;
		}
	}

	private static class QuestTaskFactory extends AHQMWorker<Object, DefaultListModel<AQuestTask>> {
		private static final QuestTaskFactory WORKER = new QuestTaskFactory();

		private QuestTaskFactory() {
		}

		public static void get( FQuest quest, DefaultListModel<AQuestTask> model) {
			model.clear();
			quest.forEachQuestTask( WORKER, model);
		}

		@Override
		protected Object doBase( ABase base, DefaultListModel<AQuestTask> model) {
			return null;
		}

		@Override
		protected Object doTask( AQuestTask task, DefaultListModel<AQuestTask> model) {
			model.addElement( task);
			return null;
		}
	}

	private static class ReputationFactory extends AHQMWorker<Object, Object> {
		private boolean positive;
		private boolean negative;

		public static BufferedImage get( FQuest quest) {
			ReputationFactory worker = new ReputationFactory();
			quest.forEachReputationReward( worker, null);
			if (worker.negative == worker.positive) {
				return REP_NORM;
			}
			else {
				return worker.positive ? REP_GOOD : REP_BAD;
			}
		}

		@Override
		public Object forReputationReward( FReputationReward rr, Object p) {
			if (rr.mValue.mValue < 0) {
				negative = true;
			}
			else if (rr.mValue.mValue > 0) {
				positive = true;
			}
			return null;
		}
	}
}
