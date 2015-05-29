package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import de.doerl.hqm.Tuple2;
import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FParameterStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestTaskDeath;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FQuestTaskReputationKill;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FReward;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.EditFrame;
import de.doerl.hqm.ui.WarnDialogs;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

public class EntityQuest extends AEntity<FQuest> {
	private static final long serialVersionUID = -5707664232506407627L;
	private static final Logger LOGGER = Logger.getLogger( EntityQuest.class.getName());
	private static final TaskBoxEmpty BOX_EMPTY = new TaskBoxEmpty();
	private FQuest mQuest;
	private JToolBar mTool = EditFrame.createToolBar();
	private ABundleAction mTitleAction = new TitleAction();
	private ABundleAction mDescAction = new DescriptionAction();
	private ABundleAction mRewardAction = new RewardAction();
	private ABundleAction mChoiceAction = new ChoiceAction();
	private ABundleAction mTaskNameAction = new TaskNameAction();
	private ABundleAction mTaskDescAction = new TaskDescAction();
	private ABundleAction mTaskListAction = new TaskListAction();
	private ABundleAction mTaskAddAction = new TaskAddAction();
	private ABundleAction mTaskDeleteAction = new TaskDeleteAction();
	private LeafTextField mTitle = new LeafTextField( true);
	private LeafTextBox mDesc = new LeafTextBox();
	private LeafStacks mRewardList;
	private LeafStacks mChoiceList;
	private LeafTextBox mTaskDesc = new LeafTextBox();
	private Box mBoxContainer = Box.createHorizontalBox();
	private LeafList<ATaskBox> mBoxList = new LeafList<>();
	private volatile ATaskBox mActiv = BOX_EMPTY;

	public EntityQuest( FQuest quest, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 2));
		mQuest = quest;
		StackIcon icon = new StackIcon( ResourceManager.getImageUI( "hqm.rep.base"), ReputationFactory.get( quest), 1.0);
		mRewardList = new LeafStacks( ICON_SIZE, quest.mRewards, new LeafIcon( icon));
		mChoiceList = new LeafStacks( ICON_SIZE, quest.mChoices, new LeafButton( "Claim reward"));
		mBoxList.setCellRenderer( new TaskListRenderer());
		mBoxContainer.setAlignmentX( LEFT_ALIGNMENT);
		createLeafs();
		update();
		updateActive( getFirstBox());
		mBoxList.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent evt) {
				try {
					ATaskBox box = mBoxList.getSelectedValue();
					if (box != null) {
						SwingUtilities.invokeLater( new TaskListMouseAction( box));
					}
				}
				catch (ClassCastException ex) {
					Utils.logThrows( LOGGER, Level.WARNING, ex);
				}
			}
		});
		mTitle.addClickListener( mTitleAction);
		mDesc.addClickListener( mDescAction);
		mTaskDesc.addClickListener( mTaskDescAction);
		mRewardList.addClickListener( mRewardAction);
		mChoiceList.addClickListener( mChoiceAction);
		mTool.add( mTitleAction);
		mTool.add( mDescAction);
		mTool.addSeparator();
		mTool.add( mRewardAction);
		mTool.add( mChoiceAction);
		mTool.addSeparator();
		mTool.add( mTaskNameAction);
		mTool.add( mTaskDescAction);
		mTool.add( mTaskListAction);
		mTool.addSeparator();
		mTool.add( mTaskAddAction);
		mTool.add( mTaskDeleteAction);
		mTool.addSeparator();
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	@Override
	public void baseAdded( ModelEvent event) {
		try {
			ABase base = event.mBase;
			if (mQuest.equals( base.getHierarchy())) {
				update();
				updateActive( findBoxOf( (AQuestTask) base));
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	@Override
	public void baseChanged( ModelEvent event) {
		ABase base = event.mBase;
		if (Utils.equals( base, mQuest)) {
			update();
			updateActive( mActiv);
		}
	}

	@Override
	public void baseRemoved( ModelEvent event) {
		ABase base = event.mBase;
		if (mQuest.equals( base.getHierarchy())) {
			update();
			updateActive( getFirstBox());
		}
	}

	@Override
	protected void createLeft( JPanel leaf) {
		leaf.add( mTitle);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( leafScoll( mDesc, 100));
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( leafScoll( mBoxList, 50));
		leaf.add( Box.createVerticalGlue());
		leaf.add( new LeafLabel( "Rewards", true));
		leaf.add( mRewardList);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( new LeafLabel( "Choices", true));
		leaf.add( mChoiceList);
	}

	@Override
	protected void createRight( JPanel leaf) {
		leaf.add( leafScoll( mTaskDesc, 160));
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( mBoxContainer);
	}

	private ATaskBox findBoxOf( AQuestTask task) {
		DefaultListModel<ATaskBox> model = mBoxList.getModel();
		for (int i = 0; i < model.size(); ++i) {
			ATaskBox box = model.elementAt( i);
			if (Utils.equals( box.getTask(), task)) {
				return box;
			}
		}
		return null;
	}

	@Override
	public FQuest getBase() {
		return mQuest;
	}

	public ATaskBox getFirstBox() {
		DefaultListModel<ATaskBox> model = mBoxList.getModel();
		return model.size() > 0 ? model.elementAt( 0) : null;
	}

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}

	private void update() {
		mTitle.setText( mQuest.mName.mValue);
		mDesc.setText( mQuest.mDescr.mValue);
		TaskBoxUpdate.get( mQuest, mCtrl, mBoxList.getModel());
		mRewardList.update();
		mChoiceList.update();
	}

	private void updateActions( boolean enabled) {
		mTaskNameAction.setEnabled( enabled);
		mTaskDescAction.setEnabled( enabled);
		mTaskDeleteAction.setEnabled( enabled);
	}

	private void updateActive( ATaskBox box) {
		mActiv.removeClickListener( mTaskListAction);
		if (box == null || BOX_EMPTY.equals( box)) {
			mTaskDesc.setText( null);
			mBoxList.clearSelection();
			mBoxContainer.removeAll();
			mBoxContainer.add( BOX_EMPTY);
			updateActions( false);
			updateTaskAction( false);
			mActiv = BOX_EMPTY;
		}
		else {
			mTaskDesc.setText( box.getTask().mDescr.mValue);
			mBoxList.setSelectedValue( box, true);
			mBoxContainer.removeAll();
			mBoxContainer.add( box);
			updateActions( true);
			updateTaskAction( true);
			mActiv = box;
		}
		mActiv.addClickListener( mTaskListAction);
		mActiv.update();
		mBoxContainer.revalidate();
		mBoxContainer.repaint();
	}

	private void updateStacks( Vector<FParameterStack> param, Vector<AStack> stks) {
		param.clear();
		for (int i = 0; i < stks.size(); i++) {
			AStack itemStack = stks.get( i);
			if (itemStack != null) {
				FParameterStack stk = new FParameterStack( mQuest);
				stk.mValue = itemStack;
				param.add( stk);
			}
		}
	}

	private void updateTaskAction( boolean enabled) {
		mTaskListAction.setEnabled( enabled);
	}

	private final class ChoiceAction extends ABundleAction {
		private static final long serialVersionUID = 3689054802238865168L;

		public ChoiceAction() {
			super( "entity.choice");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			Vector<AStack> result = DialogListStacks.updateA( mQuest.mChoices, mCtrl.getFrame());
			if (result != null) {
				updateStacks( mQuest.mChoices, result);
				mCtrl.fireChanged( mQuest);
			}
		}
	}

	private final class DescriptionAction extends ABundleAction {
		private static final long serialVersionUID = 2951694398168767124L;

		public DescriptionAction() {
			super( "entity.textbox");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			String result = DialogTextBox.update( mQuest.mDescr.mValue, mCtrl.getFrame());
			if (result != null) {
				mQuest.mDescr.mValue = result;
				mCtrl.fireChanged( mQuest);
			}
		}
	}

	private static class ReputationFactory extends AHQMWorker<Object, Object> {
		private boolean positive;
		private boolean negative;

		public static Image get( FQuest quest) {
			ReputationFactory worker = new ReputationFactory();
			quest.forEachReputationReward( worker, null);
			if (worker.negative == worker.positive) {
				return ResourceManager.getImageUI( "hqm.rep.norm");
			}
			else {
				return ResourceManager.getImageUI( worker.positive ? "hqm.rep.good" : "hqm.rep.bad");
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

	private final class RewardAction extends ABundleAction {
		private static final long serialVersionUID = -5444059797669279335L;

		public RewardAction() {
			super( "entity.reward");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			Vector<AStack> result = DialogListStacks.updateA( mQuest.mRewards, mCtrl.getFrame());
			if (result != null) {
				updateStacks( mQuest.mRewards, result);
				mCtrl.fireChanged( mQuest);
			}
		}
	}

	private final class TaskAddAction extends ABundleAction {
		private static final long serialVersionUID = 6724759221568885874L;

		public TaskAddAction() {
			super( "entity.add");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			Tuple2<TaskTyp, String> result = DialogTaskField.update( null, null, mCtrl.getFrame());
			if (result != null) {
				AQuestTask task = mCtrl.questTaskCreate( mQuest, result._1, result._2);
				mCtrl.fireAdded( task);
			}
		}
	}

	private static class TaskBoxUpdate extends AHQMWorker<Object, DefaultListModel<ATaskBox>> {
		private static final TaskBoxUpdate WORKER = new TaskBoxUpdate();

		private TaskBoxUpdate() {
		}

		public static void get( FQuest quest, EditController ctrl, DefaultListModel<ATaskBox> model) {
			model.clear();
			quest.forEachQuestTask( WORKER, model);
		}

		@Override
		protected Object doTaskItems( AQuestTaskItems task, DefaultListModel<ATaskBox> model) {
			model.addElement( new TaskBoxRequire( task));
			return null;
		}

		@Override
		public Object forTaskDeath( FQuestTaskDeath task, DefaultListModel<ATaskBox> model) {
			model.addElement( new TaskBoxDeath( task));
			return null;
		}

		@Override
		public Object forTaskLocation( FQuestTaskLocation task, DefaultListModel<ATaskBox> model) {
			model.addElement( new TaskBoxLocation( task));
			return null;
		}

		@Override
		public Object forTaskMob( FQuestTaskMob task, DefaultListModel<ATaskBox> model) {
			model.addElement( new TaskBoxMob( task));
			return null;
		}

		@Override
		public Object forTaskReputationKill( FQuestTaskReputationKill task, DefaultListModel<ATaskBox> model) {
			model.addElement( new TaskBoxReputationKill( task));
			return null;
		}

		@Override
		public Object forTaskReputationTarget( FQuestTaskReputationTarget task, DefaultListModel<ATaskBox> model) {
			model.addElement( new TaskBoxReputationTarget( task));
			return null;
		}
	}

	private final class TaskDeleteAction extends ABundleAction {
		private static final long serialVersionUID = 7223654325808174399L;

		public TaskDeleteAction() {
			super( "entity.delete");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (WarnDialogs.askDelete( mCtrl.getFrame())) {
				mCtrl.questTaskDelete( mActiv.getTask());
				mCtrl.fireRemoved( mActiv.getTask());
			}
		}
	}

	private final class TaskDescAction extends ABundleAction {
		private static final long serialVersionUID = -8367056239473171639L;

		public TaskDescAction() {
			super( "entity.textbox");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				String result = DialogTextBox.update( mActiv.getTask().mDescr.mValue, mCtrl.getFrame());
				if (result != null) {
					mActiv.getTask().mDescr.mValue = result;
					mCtrl.fireChanged( mQuest);
				}
			}
		}
	}

	private final class TaskListAction extends ABundleAction {
		private static final long serialVersionUID = 4960049088097085011L;

		public TaskListAction() {
			super( "entity.require");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv.onAction( mCtrl.getFrame())) {
				mCtrl.fireChanged( mQuest);
			}
		}
	}

	private final class TaskListMouseAction implements Runnable {
		private ATaskBox mBox;

		public TaskListMouseAction( ATaskBox box) {
			mBox = box;
		}

		@Override
		public void run() {
			updateActive( mBox);
		}
	}

	private static class TaskListRenderer extends JPanel implements ListCellRenderer<ATaskBox> {
		private static final long serialVersionUID = 9081558438188872705L;
		private JLabel mTitle = new LeafLabel( UNSELECTED, "", true);

		public TaskListRenderer() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
			add( mTitle);
			setOpaque( false);
		}

		public Component getListCellRendererComponent( JList<? extends ATaskBox> list, ATaskBox box, int index, boolean isSelected, boolean cellHasFocus) {
			mTitle.setText( box.getTask().mName.mValue);
			mTitle.setForeground( isSelected ? SELECTED : UNSELECTED);
			return this;
		}
	}

	private final class TaskNameAction extends ABundleAction {
		private static final long serialVersionUID = -3873930852720932846L;

		public TaskNameAction() {
			super( "entity.textfield");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				String result = DialogTextField.update( mActiv.getTask().mName.mValue, mCtrl.getFrame());
				if (result != null) {
					mActiv.getTask().mName.mValue = result;
					mCtrl.fireChanged( mQuest);
				}
			}
		}
	}

//	private final class TaskUpdate extends AHQMWorker<Object, Object> {
//		private TaskUpdate() {
//		}
//
//		@Override
//		public Object forLocation( FLocation loc, Object p) {
//			JComponent locBox = leafBoxHorizontal( 54);
//			locBox.add( new LeafIcon( new StackIcon( loc.mIcon.mValue, 0.6)));
//			locBox.add( Box.createHorizontalStrut( GAP));
//			JComponent dataBox = leafBox( BoxLayout.Y_AXIS);
//			dataBox.add( new LeafLabel( loc.mName.mValue, true));
//			dataBox.add( new LeafLabel( String.format( "Dimension %d, [%d radius]", loc.mDim.mValue, loc.mRadius.mValue)));
//			dataBox.add( new LeafLabel( String.format( "(%d, %d, %d)", loc.mX.mValue, loc.mY.mValue, loc.mZ.mValue)));
//			locBox.add( dataBox);
//			mTaskInfo.add( locBox);
//			mTaskInfo.add( Box.createVerticalStrut( GAP));
//			return null;
//		}
//
//		@Override
//		public Object forMob( FMob mob, Object p) {
//			JComponent mobBox = leafBoxHorizontal( 54);
//			mobBox.add( new LeafIcon( new StackIcon( mob.mIcon.mValue, 0.6)));
//			mobBox.add( Box.createHorizontalStrut( GAP));
//			JComponent dataBox = leafBox( BoxLayout.Y_AXIS);
//			dataBox.add( new LeafLabel( mob.mMob.mValue, true));
//			dataBox.add( new LeafLabel( "0[0%] killed"));
//			dataBox.add( new LeafLabel( String.format( "Kill a total of %d", mob.mKills.mValue)));
//			mobBox.add( dataBox);
//			mTaskInfo.add( mobBox);
//			mTaskInfo.add( Box.createVerticalStrut( GAP));
//			return null;
//		}
//
//		@Override
//		public Object forSetting( FSetting rs, Object p) {
//			JComponent repBox = leafBoxHorizontal( 54);
////			repBox.add( Box.createHorizontalStrut( GAP));
//			JComponent dataBox = leafBox( BoxLayout.Y_AXIS);
////			dataBox.add( leafTitle( rs.mMob.mValue));
////			dataBox.add( leafLabel( "0[0%] killed"));
////			dataBox.add( leafLabel( String.format( "Kill a total of %d", mob.mKills.mValue)));
////			mobBox.add( dataBox);
//			dataBox.add( new LeafReputation( rs));
//			dataBox.add( new LeafLabel( String.format( "    %s: %s (%d)", rs.mRep.mName, rs.mRep.mNeutral.mValue, 0)));
//			repBox.add( dataBox);
//			mTaskInfo.add( repBox);
//			mTaskInfo.add( Box.createVerticalStrut( GAP));
//			return null;
//		}
//
//		@Override
//		public Object forTaskDeath( FQuestTaskDeath task, Object p) {
//			int deaths = task.mDeaths.mValue;
//			String text = String.format( "You've died 0 of %d %s", deaths, deaths <= 1 ? "time" : "times");
//			mTaskInfo.add( new LeafLabel( text));
//			mTaskInfo.add( Box.createVerticalGlue());
//			return null;
//		}
//
//		@Override
//		public Object forTaskLocation( FQuestTaskLocation task, Object p) {
//			task.forEachLocation( this, p);
//			mTaskInfo.add( Box.createVerticalGlue());
//			return null;
//		}
//
//		@Override
//		public Object forTaskMob( FQuestTaskMob task, Object p) {
//			task.forEachMob( this, p);
//			mTaskInfo.add( Box.createVerticalGlue());
//			return null;
//		}
//
//		@Override
//		public Object forTaskReputationKill( FQuestTaskReputationKill task, Object p) {
//			int kills = task.mKills.mValue;
//			String text = String.format( "You've killed 0 of %d %s", kills, kills <= 1 ? "player" : "players");
//			mTaskInfo.add( new LeafLabel( text));
//			mTaskInfo.add( Box.createVerticalGlue());
//			return null;
//		}
//
//		@Override
//		public Object forTaskReputationTarget( FQuestTaskReputationTarget task, Object p) {
//			task.forEachSetting( this, p);
//			mTaskInfo.add( Box.createVerticalGlue());
//			return null;
//		}
//	}
	private final class TitleAction extends ABundleAction {
		private static final long serialVersionUID = -4857945141280262728L;

		public TitleAction() {
			super( "entity.textfield");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			String result = DialogTextField.update( mQuest.mName.mValue, mCtrl.getFrame());
			if (result != null) {
				mQuest.mName.mValue = result;
				mCtrl.fireChanged( mQuest);
			}
		}
	}
}
