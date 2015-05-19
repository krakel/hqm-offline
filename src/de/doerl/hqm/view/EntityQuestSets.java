package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSets;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.QuestSetIndex;
import de.doerl.hqm.quest.GuiColor;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.EditFrame;
import de.doerl.hqm.ui.WarnDialogs;
import de.doerl.hqm.utils.Utils;

class EntityQuestSets extends AEntity<FQuestSets> {
	private static final long serialVersionUID = -5930552368392528379L;
	private static final Logger LOGGER = Logger.getLogger( EntityQuestSets.class.getName());
	private FQuestSets mCategory;
	private JToolBar mTool = EditFrame.createToolBar();
	private LeafList<FQuestSet> mList = new LeafList<FQuestSet>();
	private LeafTextBox mDesc = new LeafTextBox();
	private TextBoxAction mDescAction = new TextBoxAction();
	private TextFieldAction mNameAction = new TextFieldAction();
	private AddSetAction mAddAction = new AddSetAction();
	private DeleteSetAction mDeleteAction = new DeleteSetAction();
	private JLabel mTotal = leafLabel( GuiColor.BLACK.getColor(), "");
	private JLabel mLocked = leafLabel( GuiColor.CYAN.getColor(), "0 unlocked quests");
	private JLabel mCompleted = leafLabel( GuiColor.GREEN.getColor(), "0 completed quests");
	private JLabel mAvailible = leafLabel( GuiColor.LIGHT_BLUE.getColor(), "0 quests available for completion");
	private JLabel mUnclaimed = leafLabel( GuiColor.PURPLE.getColor(), "0 quests with unclaimed rewards");
	private JLabel mInvisible = leafLabel( GuiColor.LIGHT_GRAY.getColor(), "0 quests including invisible ones");
	private JScrollPane mScroll;
	private volatile FQuestSet mActiv;

	public EntityQuestSets( EditView view, FQuestSets cat) {
		super( view, new GridLayout( 1, 2));
		mCategory = cat;
		mList.setCellRenderer( new QuestSetRenderer());
		createLeafs();
		updateList();
		mList.getHandler().addClickListener( new AClickListener() {
			@Override
			public void onDoubleClick( MouseEvent evt) {
				updateName();
			}

			@Override
			public void onSingleClick( MouseEvent evt) {
				updateListSingle( evt);
			}
		});
		mDesc.getHandler().addClickListener( new AClickListener() {
			@Override
			public void onDoubleClick( MouseEvent evt) {
				updateDesc();
			}
		});
		mTool.add( mAddAction);
		mTool.add( mDeleteAction);
		mTool.addSeparator();
		mTool.add( mNameAction);
		mTool.add( mDescAction);
		mTool.addSeparator();
	}

	@Override
	protected void createLeft( JPanel leaf) {
		leaf.add( leafScoll( mList, 10000));
	}

	@Override
	protected void createRight( JPanel leaf) {
//		top.add( Box.createVerticalStrut( 100));
		mScroll = leafScoll( mDesc, 160);
		mScroll.setVisible( false);
		leaf.add( mScroll);
		leaf.add( mTotal);
		leaf.add( mLocked);
		leaf.add( mCompleted);
		leaf.add( mAvailible);
		leaf.add( mUnclaimed);
		leaf.add( mInvisible);
		leaf.add( Box.createVerticalGlue());
	}

	@Override
	public FQuestSets getBase() {
		return mCategory;
	}

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}

	private void setSet( FQuestSet qs) {
		if (qs == null) {
			mTotal.setText( String.format( "%d quests in total", 0));
			mDesc.setText( null);
			mScroll.setVisible( false);
			mList.clearSelection();
			mDeleteAction.setEnabled( false);
			mDescAction.setEnabled( false);
			mNameAction.setEnabled( false);
			mActiv = null;
		}
		else if (Utils.equals( qs, mActiv)) {
			mTotal.setText( String.format( "%d quests in total", QuestSetSizeOf.get( qs.mParentCategory)));
			mDesc.setText( null);
			mScroll.setVisible( false);
			mList.clearSelection();
			mDeleteAction.setEnabled( false);
			mDescAction.setEnabled( false);
			mNameAction.setEnabled( false);
			mActiv = null;
		}
		else {
			mTotal.setText( String.format( "%d quests in total", QuestSetSizeOf.get( qs)));
			mDesc.setText( qs.mDesc.mValue);
			mScroll.setVisible( true);
			mList.setSelectedValue( qs, true);
			mDeleteAction.setEnabled( true);
			mDescAction.setEnabled( true);
			mNameAction.setEnabled( true);
			mActiv = qs;
		}
	}

	private void updateAddSet() {
		WarnDialogs.warnMissing( mView);
	}

	private void updateDeleteSet() {
		WarnDialogs.warnMissing( mView);
	}

	private void updateDesc() {
		if (mActiv != null) {
			DialogTextBox.update( mActiv.mDesc, mView);
			mDesc.setText( mActiv.mDesc.mValue);
		}
	}

	private void updateList() {
		DefaultListModel<FQuestSet> model = mList.getModel();
		model.clear();
		QuestSetFactory.get( mCategory, model);
		setSet( QuestSetFirst.get( mCategory));
	}

	private void updateListSingle( MouseEvent evt) {
		try {
			int row = mList.locationToIndex( evt.getPoint());
			if (row >= 0) {
				ListModel<FQuestSet> model = mList.getModel();
				FQuestSet qs = model.getElementAt( row);
				SwingUtilities.invokeLater( new QuestSetAction( this, qs));
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	private void updateName() {
		if (mActiv != null) {
			DialogTextField.update( mActiv.mName, mView);
			mList.revalidate();
			mList.repaint();
		}
	}

	private class AddSetAction extends ABundleAction {
		private static final long serialVersionUID = 6724759221568885874L;

		public AddSetAction() {
			super( "entity.add");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			updateAddSet();
		}
	}

	private class DeleteSetAction extends ABundleAction {
		private static final long serialVersionUID = 7223654325808174399L;

		public DeleteSetAction() {
			super( "entity.delete");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			updateDeleteSet();
		}
	}

	private static class QuestFactory extends AHQMWorker<Object, Object> {
//		private static final QuestFactory WORKER = new QuestFactory();
		private QuestFactory() {
		}

		public static boolean isEnabled( FQuestSet qs) {
			return true;
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			// 1. isLinkFree() // verlinkte quests
//			return  && ( triggerType.doesWorkAsInvisible() || isVisible( playerName)) && enabledParentEvaluator.isValid( playerName);
			return null;
		}
	}

	private static class QuestSetAction implements Runnable {
		private EntityQuestSets mEntity;
		private FQuestSet mQS;

		public QuestSetAction( EntityQuestSets entity, FQuestSet qs) {
			mEntity = entity;
			mQS = qs;
		}

		@Override
		public void run() {
			mEntity.setSet( mQS);
		}
	}

	private static class QuestSetFactory extends AHQMWorker<Object, DefaultListModel<FQuestSet>> {
		private static final QuestSetFactory WORKER = new QuestSetFactory();

		private QuestSetFactory() {
		}

		public static void get( FQuestSets set, DefaultListModel<FQuestSet> model) {
			model.clear();
			set.forEachMember( WORKER, model);
		}

		@Override
		protected Object doBase( ABase base, DefaultListModel<FQuestSet> model) {
			return null;
		}

		@Override
		public Object forQuestSet( FQuestSet qs, DefaultListModel<FQuestSet> model) {
			model.addElement( qs);
			return null;
		}
	}

	private static class QuestSetFirst extends AHQMWorker<FQuestSet, Object> {
		private static final QuestSetFirst WORKER = new QuestSetFirst();

		private QuestSetFirst() {
		}

		public static FQuestSet get( FQuestSets cat) {
			return cat.forEachMember( WORKER, null);
		}

		@Override
		public FQuestSet forQuestSet( FQuestSet qs, Object p) {
			return qs;
		}
	}

	private static class QuestSetRenderer extends JPanel implements ListCellRenderer<FQuestSet> {
		private static final long serialVersionUID = 9081558438188872705L;
		private JLabel mTitle = leafTitle( UNSELECTED, "");
		private JLabel mComplete = leafLabel( Color.BLACK, "");

		public QuestSetRenderer() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
			JComponent hori = leafBoxHorizontal( FONT_NORMAL_HIGH);
			hori.add( Box.createHorizontalStrut( 24));
			hori.add( mComplete);
			add( mTitle);
			add( hori);
			setOpaque( false);
			setBorder( BorderFactory.createEmptyBorder( 0, 10, 10, 10));
		}

		public Component getListCellRendererComponent( JList<? extends FQuestSet> list, FQuestSet qs, int index, boolean isSelected, boolean cellHasFocus) {
			int idx = QuestSetIndex.get( qs) + 1;
			mTitle.setText( String.format( "%d. %s", idx, qs.mName));
			mTitle.setForeground( isSelected ? SELECTED : UNSELECTED);
			boolean enabled = QuestFactory.isEnabled( qs);
			mComplete.setText( enabled ? "0% Completed" : "Locked");
			mComplete.setEnabled( enabled);
			return this;
		}
	}

	private static class QuestSetSizeOf extends AHQMWorker<Object, Object> {
		private int mResult;
		private FQuestSet mRef;

		private QuestSetSizeOf( FQuestSet ref) {
			mRef = ref;
		}

		public static int get( ACategory<FQuestSet> set) {
			QuestSetSizeOf size = new QuestSetSizeOf( null);
			set.mParentHQM.forEachQuest( size, null);
			return size.mResult;
		}

		public static int get( FQuestSet qs) {
			QuestSetSizeOf size = new QuestSetSizeOf( qs);
			qs.mParentCategory.mParentHQM.forEachQuest( size, null);
			return size.mResult;
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			if (mRef == null || Utils.equals( quest.mSet, mRef) && !quest.isDeleted()) {
				++mResult;
			}
			return null;
		}
	}

	private class TextBoxAction extends ABundleAction {
		private static final long serialVersionUID = -8367056239473171639L;

		public TextBoxAction() {
			super( "entity.textbox");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			updateDesc();
		}
	}

	private class TextFieldAction extends ABundleAction {
		private static final long serialVersionUID = -3873930852720932846L;

		public TextFieldAction() {
			super( "entity.textfield");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			updateName();
		}
	}
}
