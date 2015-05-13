package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

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
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.dispatch.AHQMWorker;

public class QuestEntity extends AEntity<FQuest> {
	private static final long serialVersionUID = -5707664232506407627L;
	private FQuest mQuest;
	private JPanel mLeafLeft = createLeaf( true);
	private JPanel mLeafRight = createLeaf( false);
	private JLabel mTitle = createTitle( "");
	private JTextArea mDesc = createText();
	private DefaultListModel<AQuestTask> mTaskModel = new DefaultListModel<AQuestTask>();
	private JList<AQuestTask> mTasks;
	private JLabel mRewards = createTitle( "Rewards");
	private JLabel mPickOne = createTitle( "Pick one");

	public QuestEntity( EditView view, FQuest quest) {
		super( view, new GridLayout( 1, 2));
		mQuest = quest;
		createLeft( mLeafLeft, quest);
		createRight( mLeafRight);
		add( mLeafLeft);
		add( mLeafRight);
		QuestTaskFactory.get( quest, mTaskModel);
	}

	private void createLeft( JPanel leaf, FQuest quest) {
		mTitle.setText( quest.getName());
		mDesc.setText( quest.mDesc.mValue);
		mTasks = createList( mTaskModel);
		mTasks.setCellRenderer( new CellRenderer());
//		mTasks.setVisibleRowCount( 3);
		leaf.add( mTitle);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( createScoll( mDesc, 140));
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( createScoll( mTasks, 80));
//		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( Box.createVerticalGlue());
		leaf.add( mRewards);
//		leaf.add( RewardList);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( mPickOne);
//		leaf.add( PickOneList);
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
		private JLabel mTitle = createTitle( UNSELECTED, "");

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
}
