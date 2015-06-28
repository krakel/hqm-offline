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
import javax.swing.SwingUtilities;

import de.doerl.hqm.Tuple2;
import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestTaskDeath;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.FQuestTaskReputationKill;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.TaskTyp;
import de.doerl.hqm.quest.TriggerType;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.WarnDialogs;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

public class EntityQuest extends AEntity<FQuest> {
	private static final long serialVersionUID = -5707664232506407627L;
	private static final Logger LOGGER = Logger.getLogger( EntityQuest.class.getName());
	private static final double ICON_ZOOM = 0.8;
	private static final Image REPUTATION_BACK = ResourceManager.getImageUI( "hqm.rep.base");
	private static final TaskBoxEmpty BOX_EMPTY = new TaskBoxEmpty();
	private final FQuest mQuest;
	private ABundleAction mNameAction = new NameAction();
	private ABundleAction mDescAction = new DescriptionAction();
	private ABundleAction mRewardAction = new RewardAction();
	private ABundleAction mChoiceAction = new ChoiceAction();
	private ABundleAction mReputationAction = new ReputationAction();
	private ABundleAction mTaskNameAction = new TaskNameAction();
	private ABundleAction mTaskDescAction = new TaskDescAction();
	private ABundleAction mTaskListAction = new TaskListAction();
	private ABundleAction mTaskAddAction = new TaskAddAction();
	private ABundleAction mTaskDeleteAction = new TaskDeleteAction();
	private ABundleAction mTaskMoveUpAction = new TaskMoveUpAction();
	private ABundleAction mTaskMoveDownAction = new TaskMoveDownAction();
	private LeafButton mRewardBtn = new LeafButton( "Claim reward");
	private LeafTextField mTitle = new LeafTextField( true);
	private LeafTextBox mDesc = new LeafTextBox();
	private LeafStacks mRewardList;
	private LeafStacks mChoiceList;
	private LeafIconBox mRepIcon = new LeafIconBox( AEntity.sizeOf( REPUTATION_BACK));
	private LeafTextBox mTaskDesc = new LeafTextBox();
	private Box mTaskContent = Box.createHorizontalBox();
	private LeafList<ATaskBox> mTaskList = new LeafList<>();
	private volatile ATaskBox mActiv = BOX_EMPTY;

	public EntityQuest( FQuest quest, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 2));
		mQuest = quest;
		mRewardList = new LeafStacks();
		mChoiceList = new LeafStacks();
		mTaskList.setCellRenderer( new TaskListRenderer());
		mTaskContent.setAlignmentX( LEFT_ALIGNMENT);
		createLeafs();
		update();
		updateActive( getFirstBox());
		mTaskList.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent evt) {
				try {
					ATaskBox box = mTaskList.getSelectedValue();
					if (box != null) {
						SwingUtilities.invokeLater( new TaskListMouseAction( box));
					}
				}
				catch (ClassCastException ex) {
					Utils.logThrows( LOGGER, Level.WARNING, ex);
				}
			}
		});
		mTitle.addClickListener( mNameAction);
		mDesc.addClickListener( mDescAction);
		mTaskDesc.addClickListener( mTaskDescAction);
		mRewardList.addClickListener( mRewardAction);
		mChoiceList.addClickListener( mChoiceAction);
		mRepIcon.addClickListener( mReputationAction);
		mTool.add( mNameAction);
		mTool.add( mDescAction);
		mTool.addSeparator();
		mTool.add( mRewardAction);
		mTool.add( mChoiceAction);
		mTool.add( mReputationAction);
		mTool.addSeparator();
		mTool.add( mTaskNameAction);
		mTool.add( mTaskDescAction);
		mTool.add( mTaskListAction);
		mTool.addSeparator();
		mTool.add( mTaskAddAction);
		mTool.add( mTaskMoveUpAction);
		mTool.add( mTaskMoveDownAction);
		mTool.add( mTaskDeleteAction);
		mTool.addSeparator();
		updateMoveActions();
	}

	private static void updateStacks( JPanel panel, Vector<FItemStack> list) {
		panel.removeAll();
		if (list.isEmpty()) {
			panel.add( LeafIcon.createEmpty( ICON_ZOOM));
			panel.add( Box.createHorizontalStrut( 3));
		}
		else {
			for (AStack stk : list) {
				LeafIcon leaf = new LeafIcon();
				IconUpdate.create( leaf, stk, ICON_ZOOM, stk.countOf());
				panel.add( leaf);
				panel.add( Box.createHorizontalStrut( 3));
			}
		}
		panel.add( Box.createHorizontalGlue());
		panel.revalidate();
		panel.repaint();
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	@Override
	public void baseAdded( ModelEvent event) {
		try {
			ABase base = event.mBase;
			if (mQuest.equals( base.getParent())) {
				update();
				updateActive( findBoxOf( (AQuestTask) base));
				updateMoveActions();
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
			updateMoveActions();
		}
	}

	@Override
	public void baseRemoved( ModelEvent event) {
		ABase base = event.mBase;
		if (mQuest.equals( base.getParent())) {
			update();
			updateActive( getFirstBox());
			updateMoveActions();
		}
	}

	@Override
	public void baseTreeChange( ModelEvent event) {
	}

	private Box createChoiseBox() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX( LEFT_ALIGNMENT);
		box.add( mChoiceList);
		box.add( mRewardBtn);
		return box;
	}

	@Override
	protected void createLeft( JPanel leaf) {
		leaf.add( mTitle);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( leafScoll( mDesc, 100));
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( leafScoll( mTaskList, 50));
		leaf.add( Box.createVerticalGlue());
		leaf.add( new LeafLabel( "Rewards", true));
		leaf.add( createRewardBox());
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( new LeafLabel( "Choices", true));
		leaf.add( createChoiseBox());
	}

	private Box createRewardBox() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX( LEFT_ALIGNMENT);
		box.add( mRewardList);
		box.add( mRepIcon);
		return box;
	}

	@Override
	protected void createRight( JPanel leaf) {
		leaf.add( leafScoll( mTaskDesc, 160));
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( mTaskContent);
	}

	private ATaskBox findBoxOf( AQuestTask task) {
		DefaultListModel<ATaskBox> model = mTaskList.getModel();
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
		DefaultListModel<ATaskBox> model = mTaskList.getModel();
		return model.size() > 0 ? model.elementAt( 0) : null;
	}

	private boolean isTriggerQuest() {
		return mQuest.mTriggerType == TriggerType.QUEST_TRIGGER;
	}

	private void update() {
		mTitle.setText( mQuest.mName);
		mDesc.setText( mQuest.mDescr);
		TaskBoxUpdate.get( mQuest, mCtrl, mTaskList.getModel());
		mRepIcon.setIcon( new StackIcon( REPUTATION_BACK, ReputationFactory.get( mQuest), 1.0));
		updateStacks( mRewardList, mQuest.mRewards);
		updateStacks( mChoiceList, mQuest.mChoices);
		updateTrigger( !isTriggerQuest());
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
			mTaskList.clearSelection();
			mTaskContent.removeAll();
			mTaskContent.add( BOX_EMPTY);
			updateActions( false);
			updateTaskAction( false);
			mActiv = BOX_EMPTY;
		}
		else {
			mTaskDesc.setText( box.getTask().mDescr);
			mTaskList.setSelectedValue( box, true);
			mTaskContent.removeAll();
			mTaskContent.add( box);
			updateActions( true);
			updateTaskAction( true);
			mActiv = box;
		}
		mActiv.addClickListener( mTaskListAction);
		mActiv.update();
		mTaskContent.revalidate();
		mTaskContent.repaint();
	}

	private void updateMoveActions() {
		AQuestTask task = mActiv.getTask();
		if (task == null) {
			mTaskMoveUpAction.setEnabled( false);
			mTaskMoveDownAction.setEnabled( false);
		}
		else {
			mTaskMoveUpAction.setEnabled( !task.isFirst());
			mTaskMoveDownAction.setEnabled( !task.isLast());
		}
	}

	private void updateTaskAction( boolean enabled) {
		mTaskListAction.setEnabled( enabled);
	}

	private void updateTrigger( boolean value) {
		mRewardAction.setEnabled( value);
		mChoiceAction.setEnabled( value);
		mReputationAction.setEnabled( value);
		mRewardList.setEnabled( value);
		mChoiceList.setEnabled( value);
		mRepIcon.setEnabled( value);
	}

	private final class ChoiceAction extends ABundleAction {
		private static final long serialVersionUID = 3689054802238865168L;

		public ChoiceAction() {
			super( "entity.quest.choice");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (!isTriggerQuest() && DialogListItems.update( mQuest.mChoices, mCtrl.getFrame())) {
				mCtrl.fireChanged( mQuest);
			}
		}
	}

	private final class DescriptionAction extends ABundleAction {
		private static final long serialVersionUID = 2951694398168767124L;

		public DescriptionAction() {
			super( "entity.quest.desc");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			String result = DialogTextBox.update( mQuest.mDescr, mCtrl.getFrame(), DataBitHelper.QUEST_DESCRIPTION_LENGTH);
			if (result != null) {
				mQuest.mDescr = result;
				mCtrl.fireChanged( mQuest);
			}
		}
	}

	private final class NameAction extends ABundleAction {
		private static final long serialVersionUID = -4857945141280262728L;

		public NameAction() {
			super( "entity.quest.title");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			String result = DialogTextField.update( mQuest.mName, mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
			if (result != null) {
				mQuest.mName = result;
				mCtrl.fireChanged( mQuest);
			}
		}
	}

	private final class ReputationAction extends ABundleAction {
		private static final long serialVersionUID = -3007492870966214729L;

		public ReputationAction() {
			super( "entity.quest.reputation");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (!isTriggerQuest() && DialogReputation.update( mQuest, mCtrl.getFrame())) {
				mCtrl.fireChanged( mQuest);
			}
		}
	}

	private static class ReputationFactory extends AHQMWorker<Object, Object> {
		private boolean mPositive;
		private boolean mNegative;
		private boolean mEmpty = true;

		public static Image get( FQuest quest) {
			ReputationFactory worker = new ReputationFactory();
			quest.forEachReward( worker, null);
			if (worker.mEmpty) {
				return null;
			}
			else if (worker.mNegative == worker.mPositive) {
				return ResourceManager.getImageUI( "hqm.rep.norm");
			}
			else {
				return ResourceManager.getImageUI( worker.mPositive ? "hqm.rep.good" : "hqm.rep.bad");
			}
		}

		@Override
		public Object forReputationReward( FReputationReward rr, Object p) {
			mEmpty = false;
			if (rr.mValue < 0) {
				mNegative = true;
			}
			else if (rr.mValue > 0) {
				mPositive = true;
			}
			return null;
		}
	}

	private final class RewardAction extends ABundleAction {
		private static final long serialVersionUID = -5444059797669279335L;

		public RewardAction() {
			super( "entity.quest.reward");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (!isTriggerQuest() && DialogListItems.update( mQuest.mRewards, mCtrl.getFrame())) {
				mCtrl.fireChanged( mQuest);
			}
		}
	}

	private final class TaskAddAction extends ABundleAction {
		private static final long serialVersionUID = 6724759221568885874L;

		public TaskAddAction() {
			super( "entity.task.add");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			Tuple2<TaskTyp, String> result = DialogTaskField.update( null, null, mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
			if (result != null) {
				mCtrl.questTaskCreate( mQuest, result._1, result._2);
			}
		}
	}

	private static class TaskBoxUpdate extends AHQMWorker<Object, DefaultListModel<ATaskBox>> {
		private static final TaskBoxUpdate WORKER = new TaskBoxUpdate();

		private TaskBoxUpdate() {
		}

		public static void get( FQuest quest, EditController ctrl, DefaultListModel<ATaskBox> model) {
			model.clear();
			quest.forEachTask( WORKER, model);
		}

		@Override
		protected Object doTaskItems( AQuestTaskItems task, DefaultListModel<ATaskBox> model) {
			model.addElement( new TaskBoxItems( task));
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
			super( "entity.task.delete");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			AQuestTask task = mActiv.getTask();
			if (task != null && WarnDialogs.askDelete( mCtrl.getFrame())) {
				mCtrl.questTaskDelete( task);
			}
		}
	}

	private final class TaskDescAction extends ABundleAction {
		private static final long serialVersionUID = -8367056239473171639L;

		public TaskDescAction() {
			super( "entity.task.desc");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			AQuestTask task = mActiv.getTask();
			if (task != null) {
				String result = DialogTextBox.update( task.mDescr, mCtrl.getFrame(), DataBitHelper.QUEST_DESCRIPTION_LENGTH);
				if (result != null) {
					task.mDescr = result;
					mCtrl.fireChanged( mQuest);
				}
			}
		}
	}

	private final class TaskListAction extends ABundleAction {
		private static final long serialVersionUID = 4960049088097085011L;

		public TaskListAction() {
			super( "entity.quest.require");
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
			updateMoveActions();
		}
	}

	private static class TaskListRenderer extends AListCellRenderer<ATaskBox> {
		private static final long serialVersionUID = 9081558438188872705L;
		private JLabel mTitle = new LeafLabel( UNSELECTED, "", true);

		public TaskListRenderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( false);
			add( mTitle);
		}

		public Component getListCellRendererComponent( JList<? extends ATaskBox> list, ATaskBox box, int index, boolean isSelected, boolean cellHasFocus) {
			mTitle.setIcon( ResourceManager.getIcon( box.getTask().getTaskTyp().getIcon()));
			mTitle.setText( box.getTask().mName);
			mTitle.setForeground( isSelected ? SELECTED : UNSELECTED);
			return this;
		}
	}

	private final class TaskMoveDownAction extends ABundleAction {
		private static final long serialVersionUID = -6462637624253468303L;

		public TaskMoveDownAction() {
			super( "entity.task.moveDown");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			AQuestTask task = mActiv.getTask();
			if (task != null) {
				task.moveDown();
				mCtrl.fireChanged( task.mParentQuest);
			}
		}
	}

	private final class TaskMoveUpAction extends ABundleAction {
		private static final long serialVersionUID = 6459945568446758606L;

		public TaskMoveUpAction() {
			super( "entity.task.moveUp");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			AQuestTask task = mActiv.getTask();
			if (task != null) {
				task.moveUp();
				mCtrl.fireChanged( task.mParentQuest);
			}
		}
	}

	private final class TaskNameAction extends ABundleAction {
		private static final long serialVersionUID = -3873930852720932846L;

		public TaskNameAction() {
			super( "entity.task.title");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			AQuestTask task = mActiv.getTask();
			if (task != null) {
				String result = DialogTextField.update( task.mName, mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
				if (result != null) {
					task.mName = result;
					mCtrl.fireChanged( mQuest);
				}
			}
		}
	}
}
