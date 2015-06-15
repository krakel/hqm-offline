package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.IndexOf;
import de.doerl.hqm.base.dispatch.SizeOfQuests;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.GuiColor;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.EditFrame;
import de.doerl.hqm.ui.WarnDialogs;
import de.doerl.hqm.utils.Utils;

class EntityQuestSetCat extends AEntity<FQuestSetCat> {
	private static final long serialVersionUID = -5930552368392528379L;
	private static final Logger LOGGER = Logger.getLogger( EntityQuestSetCat.class.getName());
	private final FQuestSetCat mCategory;
	private JToolBar mTool = EditFrame.createToolBar();
	private ABundleAction mNameAction = new NameAction();
	private ABundleAction mDescAction = new DescriptionAction();
	private ABundleAction mAddAction = new AddAction();
	private ABundleAction mDeleteAction = new DeleteAction();
	private ABundleAction mMoveUpAction = new MoveUpAction();
	private ABundleAction mMoveDownAction = new MoveDownAction();
	private LeafList<FQuestSet> mList = new LeafList<>();
	private LeafTextBox mDesc = new LeafTextBox();
	private LeafLabel mTotal = new LeafLabel( GuiColor.BLACK.getColor(), "");
	private LeafLabel mLocked = new LeafLabel( GuiColor.CYAN.getColor(), "0 unlocked quests");
	private LeafLabel mCompleted = new LeafLabel( GuiColor.GREEN.getColor(), "0 completed quests");
	private LeafLabel mAvailible = new LeafLabel( GuiColor.LIGHT_BLUE.getColor(), "0 quests available for completion");
	private LeafLabel mUnclaimed = new LeafLabel( GuiColor.PURPLE.getColor(), "0 quests with unclaimed rewards");
	private LeafLabel mInvisible = new LeafLabel( GuiColor.LIGHT_GRAY.getColor(), "0 quests including invisible ones");
	private JScrollPane mScroll;
	private volatile FQuestSet mActiv;

	public EntityQuestSetCat( FQuestSetCat cat, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 2));
		mCategory = cat;
		mList.setCellRenderer( new ListRenderer());
		createLeafs();
		update();
		updateActive( QuestSetFirst.get( mCategory), true);
		mList.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent evt) {
				FQuestSet qs = mList.getSelectedValue();
				if (qs != null) {
					SwingUtilities.invokeLater( new ListMouseAction( qs));
				}
			}
		});
		mList.addClickListener( mNameAction);
		mDesc.addClickListener( mDescAction);
		mTool.add( mNameAction);
		mTool.add( mDescAction);
		mTool.addSeparator();
		mTool.add( mAddAction);
		mTool.add( mMoveUpAction);
		mTool.add( mMoveDownAction);
		mTool.add( mDeleteAction);
		mTool.addSeparator();
		updateMoveActions();
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	@Override
	public void baseAdded( ModelEvent event) {
		try {
			ABase base = event.mBase;
			if (mCategory.equals( base.getParent())) {
				update();
				updateActive( (FQuestSet) base, true);
				updateMoveActions();
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	@Override
	public void baseChanged( ModelEvent event) {
		if (mCategory.equals( event.mBase)) {
			update();
			updateActive( mActiv, false);
			updateMoveActions();
		}
	}

	@Override
	public void baseRemoved( ModelEvent event) {
		ABase base = event.mBase;
		if (mCategory.equals( base.getParent())) {
			update();
			updateActive( QuestSetFirst.get( mCategory), true);
			updateMoveActions();
		}
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
	public FQuestSetCat getBase() {
		return mCategory;
	}

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}

	private void update() {
		QuestSetUpdate.get( mCategory, mList.getModel());
	}

	private void updateActions( boolean enabled) {
		mNameAction.setEnabled( enabled);
		mDescAction.setEnabled( enabled);
		mDeleteAction.setEnabled( enabled);
	}

	private void updateActive( FQuestSet qs, boolean toggel) {
		if (qs == null) {
			mTotal.setText( String.format( "%d quests in total", 0));
			mDesc.setText( null);
			mScroll.setVisible( false);
			mList.clearSelection();
			updateActions( false);
			mActiv = null;
		}
		else if (toggel && Utils.equals( qs, mActiv)) {
			mTotal.setText( String.format( "%d quests in total", SizeOfQuests.get( qs.mParentCategory)));
			mDesc.setText( null);
			mScroll.setVisible( false);
			mList.clearSelection();
			updateActions( false);
			mActiv = null;
		}
		else {
			mTotal.setText( String.format( "%d quests in total", SizeOfQuests.get( qs)));
			mDesc.setText( qs.mDescr);
			mScroll.setVisible( true);
			mList.setSelectedValue( qs, true);
			updateActions( true);
			mActiv = qs;
		}
	}

	private void updateMoveActions() {
		if (mActiv == null) {
			mMoveUpAction.setEnabled( false);
			mMoveDownAction.setEnabled( false);
		}
		else if (mActiv.isFirst()) {
			mMoveUpAction.setEnabled( false);
			mMoveDownAction.setEnabled( true);
		}
		else if (mActiv.isLast()) {
			mMoveUpAction.setEnabled( true);
			mMoveDownAction.setEnabled( false);
		}
		else {
			mMoveUpAction.setEnabled( true);
			mMoveDownAction.setEnabled( true);
		}
	}

	private final class AddAction extends ABundleAction {
		private static final long serialVersionUID = 6724759221568885874L;

		public AddAction() {
			super( "entity.setcat.add");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			String result = DialogTextField.update( "new", mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
			if (result != null) {
				mCtrl.questSetCreate( mCategory, result);
			}
		}
	}

	private final class DeleteAction extends ABundleAction {
		private static final long serialVersionUID = 7223654325808174399L;

		public DeleteAction() {
			super( "entity.setcat.delete");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (WarnDialogs.askDelete( mCtrl.getFrame())) {
				mCtrl.questSetDelete( mActiv);
			}
		}
	}

	private final class DescriptionAction extends ABundleAction {
		private static final long serialVersionUID = -8367056239473171639L;

		public DescriptionAction() {
			super( "entity.setcat.desc");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				String result = DialogTextBox.update( mActiv.mDescr, mCtrl.getFrame(), DataBitHelper.QUEST_DESCRIPTION_LENGTH);
				if (result != null) {
					mActiv.mDescr = result;
					mCtrl.fireChanged( mCategory);
				}
			}
		}
	}

	private final class ListMouseAction implements Runnable {
		private FQuestSet mQS;

		public ListMouseAction( FQuestSet qs) {
			mQS = qs;
		}

		@Override
		public void run() {
			updateActive( mQS, true);
			updateMoveActions();
		}
	}

	private static class ListRenderer extends AListCellRenderer<FQuestSet> {
		private static final long serialVersionUID = 9081558438188872705L;
		private LeafLabel mTitle = new LeafLabel( UNSELECTED, "", true);
		private LeafLabel mComplete = new LeafLabel( "");

		public ListRenderer() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
			setOpaque( false);
			setBorder( BorderFactory.createEmptyBorder( 0, 10, 10, 10));
			JComponent hori = leafBoxHorizontal( FONT_NORMAL.getSize());
			hori.add( Box.createHorizontalStrut( 24));
			hori.add( mComplete);
			add( mTitle);
			add( hori);
		}

		public Component getListCellRendererComponent( JList<? extends FQuestSet> list, FQuestSet qs, int index, boolean isSelected, boolean cellHasFocus) {
			int idx = IndexOf.getMember( qs) + 1;
			mTitle.setText( String.format( "%d. %s", idx, qs.mName));
			mTitle.setForeground( isSelected ? SELECTED : UNSELECTED);
			boolean enabled = QuestSetIsEnabled.get( qs);
			mComplete.setText( enabled ? "0% Completed" : "Locked");
			mComplete.setEnabled( enabled);
			return this;
		}
	}

	private final class MoveDownAction extends ABundleAction {
		private static final long serialVersionUID = -4792849231706543388L;

		public MoveDownAction() {
			super( "entity.setcat.moveDown");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				mActiv.moveDown();
				mCtrl.fireChanged( mActiv.mParentCategory);
			}
		}
	}

	private final class MoveUpAction extends ABundleAction {
		private static final long serialVersionUID = 6791409995069306038L;

		public MoveUpAction() {
			super( "entity.setcat.moveUp");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				mActiv.moveUp();
				mCtrl.fireChanged( mActiv.mParentCategory);
			}
		}
	}

	private final class NameAction extends ABundleAction {
		private static final long serialVersionUID = -3873930852720932846L;

		public NameAction() {
			super( "entity.setcat.title");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				String result = DialogTextField.update( mActiv.mName, mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
				if (result != null) {
					mActiv.mName = result;
					mCtrl.fireChanged( mCategory);
				}
			}
		}
	}

	private static class QuestSetFirst extends AHQMWorker<FQuestSet, Object> {
		private static final QuestSetFirst WORKER = new QuestSetFirst();

		private QuestSetFirst() {
		}

		public static FQuestSet get( FQuestSetCat cat) {
			return cat.forEachMember( WORKER, null);
		}

		@Override
		public FQuestSet forQuestSet( FQuestSet set, Object p) {
			return set;
		}
	}

	private static class QuestSetIsEnabled extends AHQMWorker<Object, Object> {
		private static final QuestSetIsEnabled WORKER = new QuestSetIsEnabled();

		private QuestSetIsEnabled() {
		}

		public static boolean get( FQuestSet qs) {
			return qs.forEachQuest( WORKER, null) != null;
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			if (quest.isFree()) {
				return Boolean.TRUE;
			}
			return null;
		}
	}

	private static class QuestSetUpdate extends AHQMWorker<Object, DefaultListModel<FQuestSet>> {
		private static final QuestSetUpdate WORKER = new QuestSetUpdate();

		private QuestSetUpdate() {
		}

		public static void get( FQuestSetCat set, DefaultListModel<FQuestSet> model) {
			model.clear();
			set.forEachMember( WORKER, model);
		}

		@Override
		protected Object doBase( ABase base, DefaultListModel<FQuestSet> model) {
			return null;
		}

		@Override
		public Object forQuestSet( FQuestSet set, DefaultListModel<FQuestSet> model) {
			model.addElement( set);
			return null;
		}
	}
}
