package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.ARequirement;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FParameterStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestTaskDeath;
import de.doerl.hqm.base.FQuestTaskItemsConsume;
import de.doerl.hqm.base.FQuestTaskItemsConsumeQDS;
import de.doerl.hqm.base.FQuestTaskItemsCrafting;
import de.doerl.hqm.base.FQuestTaskItemsDetect;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FQuestTaskReputationKill;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FReward;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.utils.Utils;

public class EntityQuest extends AEntity<FQuest> implements MouseListener {
	private static final long serialVersionUID = -5707664232506407627L;
	private static final Logger LOGGER = Logger.getLogger( EntityQuest.class.getName());
	protected static final BufferedImage REPUATION = MAP.getSubimage( 30, 82, 16, 16);
	protected static final BufferedImage REP_GOOD = MAP.getSubimage( 78, 82, 16, 16);
	protected static final BufferedImage REP_BAD = MAP.getSubimage( 94, 82, 16, 16);
	protected static final BufferedImage REP_NORM = MAP.getSubimage( 110, 82, 16, 16);
	private FQuest mQuest;
	private JLabel mTitle = leafTitle( "");
	private LeafTextBox mDesc = new LeafTextBox();
	private LeafTextBox mTaskDesc = new LeafTextBox();
	private DefaultListModel<AQuestTask> mTaskModel = new DefaultListModel<AQuestTask>();
	private JList<AQuestTask> mTasks;
	private JLabel mRewards = leafTitle( "Rewards");
	private JLabel mPickOne = leafTitle( "Pick one");
	private JComponent mRewardList = leafBoxHorizontal( ICON_SIZE);
	private JComponent mPickOneList = leafBoxHorizontal( ICON_SIZE);
	private JComponent mTaskInfo = leafBoxVertical( 240);
	private JLabel mReputation;

	public EntityQuest( EditView view, FQuest quest) {
		super( view, new GridLayout( 1, 2));
		mQuest = quest;
		createLeafs();
//		mDesc.connectTo( quest.mDesc);
		QuestTaskFactory.get( quest, mTaskModel);
		mTasks.setSelectedIndex( 0);
		SwingUtilities.invokeLater( new TaskSetAction( this, QuestTaskFirst.get( quest)));
	}

	@Override
	public void baseAdded( ModelEvent event) {
	}

	@Override
	public void baseChanged( ModelEvent event) {
	}

	@Override
	public void baseRemoved( ModelEvent event) {
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	private void createIconList( JComponent panel, Vector<FParameterStack> list, JLabel btn) {
		for (FParameterStack stk : list) {
			panel.add( leafStack( stk.mValue));
			panel.add( Box.createHorizontalStrut( 3));
		}
		panel.add( Box.createHorizontalGlue());
		if (btn != null) {
			panel.add( btn);
		}
	}

	@Override
	protected void createLeft( JPanel leaf) {
		mTitle.setText( mQuest.mName.mValue);
		mDesc.setText( mQuest.mDesc.mValue);
		mTasks = leafList( mTaskModel);
		mTasks.setCellRenderer( new CellRenderer());
		mTasks.addMouseListener( this);
		leaf.add( mTitle);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( leafScoll( mDesc, 100));
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( leafScoll( mTasks, 50));
//		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( Box.createVerticalGlue());
		leaf.add( mRewards);
		mReputation = leafIcon( REPUATION, ReputationFactory.get( mQuest));
		createIconList( mRewardList, mQuest.mRewards, mReputation);
		leaf.add( mRewardList);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( mPickOne);
		createIconList( mPickOneList, mQuest.mChoices, leafButton( "Claim reward"));
		leaf.add( mPickOneList);
	}

	@Override
	protected void createRight( JPanel leaf) {
		leaf.add( leafScoll( mTaskDesc, 80));
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( mTaskInfo);
	}

	@Override
	public FQuest getBase() {
		return mQuest;
	}

	@Override
	public JToolBar getToolBar() {
		return null;
	}

	@Override
	public void mouseClicked( MouseEvent evt) {
		try {
			JList<?> list = (JList<?>) evt.getSource();
			int row = list.locationToIndex( evt.getPoint());
			if (row >= 0) {
				ListModel<?> model = list.getModel();
				AQuestTask qs = (AQuestTask) model.getElementAt( row);
				switch (evt.getButton()) {
					case MouseEvent.BUTTON1:
						SwingUtilities.invokeLater( new TaskSetAction( this, qs));
						break;
					default:
				}
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	@Override
	public void mouseEntered( MouseEvent evt) {
	}

	@Override
	public void mouseExited( MouseEvent evt) {
	}

	@Override
	public void mousePressed( MouseEvent evt) {
	}

	@Override
	public void mouseReleased( MouseEvent evt) {
	}

	public void setTask( AQuestTask task) {
		mTaskDesc.setText( task.mDesc.mValue);
		mTaskInfo.removeAll();
		QuestTaskUpdate.get( task, mTaskInfo);
		mTaskInfo.add( Box.createHorizontalGlue());
		mTaskInfo.revalidate();
		mTaskInfo.repaint();
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
			mTitle.setText( qs.mName.mValue);
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

	private static class QuestTaskFirst extends AHQMWorker<AQuestTask, Object> {
		private static final QuestTaskFirst WORKER = new QuestTaskFirst();

		public static AQuestTask get( FQuest quest) {
			return quest.forEachQuestTask( WORKER, null);
		}

		@Override
		protected AQuestTask doTask( AQuestTask task, Object p) {
			return task;
		}
	}

	private static class QuestTaskUpdate extends AHQMWorker<Object, JComponent> {
		private static final QuestTaskUpdate WORKER = new QuestTaskUpdate();

		private QuestTaskUpdate() {
		}

		public static void get( AQuestTask task, JComponent comp) {
			task.accept( WORKER, comp);
		}

		@Override
		protected Object doRequirement( ARequirement req, JComponent comp) {
			comp.add( leafStack( req.getStack().mValue));
			comp.add( Box.createHorizontalStrut( 3));
			return null;
		}

		@Override
		protected Object doTaskItems( AQuestTaskItems task, JComponent comp) {
			JComponent itemBox = leafBoxFloat( 2 * (ICON_SIZE + GAP));
			comp.add( itemBox);
			task.forEachRequirement( this, itemBox);
			comp.add( Box.createVerticalGlue());
			return null;
		}

		@Override
		public Object forLocation( FLocation loc, JComponent comp) {
			JComponent locBox = leafBoxHorizontal( 54);
			locBox.add( leafStack( loc.mIcon.mValue));
			locBox.add( Box.createHorizontalStrut( GAP));
			JComponent dataBox = leafBox( BoxLayout.Y_AXIS);
			dataBox.add( leafTitle( loc.mName.mValue));
			dataBox.add( leafLabel( String.format( "Dimension %d, [%d radius]", loc.mDim.mValue, loc.mRadius.mValue)));
			dataBox.add( leafLabel( String.format( "(%d, %d, %d)", loc.mX.mValue, loc.mY.mValue, loc.mZ.mValue)));
			locBox.add( dataBox);
			comp.add( locBox);
			comp.add( Box.createVerticalStrut( GAP));
			return null;
		}

		@Override
		public Object forMob( FMob mob, JComponent comp) {
			JComponent mobBox = leafBoxHorizontal( 54);
			mobBox.add( leafStack( mob.mIcon.mValue));
			mobBox.add( Box.createHorizontalStrut( GAP));
			JComponent dataBox = leafBox( BoxLayout.Y_AXIS);
			dataBox.add( leafTitle( mob.mMob.mValue));
			dataBox.add( leafLabel( "0[0%] killed"));
			dataBox.add( leafLabel( String.format( "Kill a total of %d", mob.mKills.mValue)));
			mobBox.add( dataBox);
			comp.add( mobBox);
			comp.add( Box.createVerticalStrut( GAP));
			return null;
		}

		@Override
		public Object forSetting( FSetting rs, JComponent comp) {
			JComponent repBox = leafBoxHorizontal( 54);
//			repBox.add( Box.createHorizontalStrut( GAP));
			JComponent dataBox = leafBox( BoxLayout.Y_AXIS);
//			dataBox.add( leafTitle( rs.mMob.mValue));
//			dataBox.add( leafLabel( "0[0%] killed"));
//			dataBox.add( leafLabel( String.format( "Kill a total of %d", mob.mKills.mValue)));
//			mobBox.add( dataBox);
			dataBox.add( leafRepImage( rs));
			dataBox.add( leafLabel( String.format( "    %s: %s (%d)", rs.mRep.mName, rs.mRep.mNeutral.mValue, 0)));
			repBox.add( dataBox);
			comp.add( repBox);
			comp.add( Box.createVerticalStrut( GAP));
			return null;
		}

		@Override
		public Object forTaskDeath( FQuestTaskDeath task, JComponent comp) {
			int deaths = task.mDeaths.mValue;
			String text = String.format( "You've died 0 of %d %s", deaths, deaths <= 1 ? "time" : "times");
			comp.add( leafLabel( Color.BLACK, text));
			comp.add( Box.createVerticalGlue());
			return null;
		}

		@Override
		public Object forTaskItemsConsume( FQuestTaskItemsConsume task, JComponent comp) {
			doTaskItems( task, comp);
			comp.add( AEntity.leafButtons( leafButton( "Manual submit"), leafButton( "Select task")));
			return null;
		}

		@Override
		public Object forTaskItemsConsumeQDS( FQuestTaskItemsConsumeQDS task, JComponent comp) {
			doTaskItems( task, comp);
			comp.add( AEntity.leafButtons( leafButton( "Manual submit"), leafButton( "Select task")));
			return null;
		}

		@Override
		public Object forTaskItemsCrafting( FQuestTaskItemsCrafting task, JComponent comp) {
			doTaskItems( task, comp);
			return null;
		}

		@Override
		public Object forTaskItemsDetect( FQuestTaskItemsDetect task, JComponent comp) {
			doTaskItems( task, comp);
			comp.add( AEntity.leafButtons( leafButton( "Manual detect")));
			return null;
		}

		@Override
		public Object forTaskLocation( FQuestTaskLocation task, JComponent comp) {
			task.forEachLocation( this, comp);
			comp.add( Box.createVerticalGlue());
			return null;
		}

		@Override
		public Object forTaskMob( FQuestTaskMob task, JComponent comp) {
			task.forEachMob( this, comp);
			comp.add( Box.createVerticalGlue());
			return null;
		}

		@Override
		public Object forTaskReputationKill( FQuestTaskReputationKill task, JComponent comp) {
			int kills = task.mKills.mValue;
			String text = String.format( "You've killed 0 of %d %s", kills, kills <= 1 ? "player" : "players");
			comp.add( leafLabel( Color.BLACK, text));
			comp.add( Box.createVerticalGlue());
			return null;
		}

		@Override
		public Object forTaskReputationTarget( FQuestTaskReputationTarget task, JComponent comp) {
			task.forEachSetting( this, comp);
			comp.add( Box.createVerticalGlue());
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
		public Object forReward( FReward rr, Object p) {
			if (rr.mValue.mValue < 0) {
				negative = true;
			}
			else if (rr.mValue.mValue > 0) {
				positive = true;
			}
			return null;
		}
	}

	private static class TaskSetAction implements Runnable {
		private EntityQuest mEntity;
		private AQuestTask mTask;

		public TaskSetAction( EntityQuest entity, AQuestTask task) {
			mEntity = entity;
			mTask = task;
		}

		@Override
		public void run() {
			mEntity.setTask( mTask);
		}
	}
}
