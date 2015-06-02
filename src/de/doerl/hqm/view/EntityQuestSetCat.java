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
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.IndexOf;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
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
		mTool.add( mDeleteAction);
		mTool.addSeparator();
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	@Override
	public void baseAdded( ModelEvent event) {
		try {
			ABase base = event.mBase;
			if (mCategory.equals( base.getHierarchy())) {
				update();
				updateActive( (FQuestSet) base, true);
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
		}
	}

	@Override
	public void baseRemoved( ModelEvent event) {
		ABase base = event.mBase;
		if (mCategory.equals( base.getHierarchy())) {
			update();
			updateActive( QuestSetFirst.get( mCategory), true);
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
			mTotal.setText( String.format( "%d quests in total", QuestSizeOf.get( qs.mParentCategory)));
			mDesc.setText( null);
			mScroll.setVisible( false);
			mList.clearSelection();
			updateActions( false);
			mActiv = null;
		}
		else {
			mTotal.setText( String.format( "%d quests in total", QuestSizeOf.get( qs)));
			mDesc.setText( qs.mDescr);
			mScroll.setVisible( true);
			mList.setSelectedValue( qs, true);
			updateActions( true);
			mActiv = qs;
		}
	}

	private final class AddAction extends ABundleAction {
		private static final long serialVersionUID = 6724759221568885874L;

		public AddAction() {
			super( "entity.add");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			String result = DialogTextField.update( null, mCtrl.getFrame());
			if (result != null) {
				FQuestSet qs = mCtrl.questSetCreate( mCategory, result);
				mCtrl.fireAdded( qs);
			}
		}
	}

	private final class DeleteAction extends ABundleAction {
		private static final long serialVersionUID = 7223654325808174399L;

		public DeleteAction() {
			super( "entity.delete");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (WarnDialogs.askDelete( mCtrl.getFrame())) {
				mCtrl.questSetDelete( mActiv);
				mCtrl.fireRemoved( mActiv);
			}
		}
	}

	private final class DescriptionAction extends ABundleAction {
		private static final long serialVersionUID = -8367056239473171639L;

		public DescriptionAction() {
			super( "entity.textbox");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				String result = DialogTextBox.update( mActiv.mDescr, mCtrl.getFrame());
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
		}
	}

	private static class ListRenderer extends JPanel implements ListCellRenderer<FQuestSet> {
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

	private final class NameAction extends ABundleAction {
		private static final long serialVersionUID = -3873930852720932846L;

		public NameAction() {
			super( "entity.textfield");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				String result = DialogTextField.update( mActiv.mName, mCtrl.getFrame());
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

	private static class QuestSetIsEnabled extends AHQMWorker<Object, FQuestSet> {
		private static final QuestSetIsEnabled WORKER = new QuestSetIsEnabled();

		private QuestSetIsEnabled() {
		}

		public static boolean get( FQuestSet qs) {
			return qs.mParentCategory.mParentHQM.forEachQuest( WORKER, qs) != null;
		}

		@Override
		public Object forQuest( FQuest quest, FQuestSet set) {
			if (Utils.equals( quest.mQuestSet, set) && quest.isFree()) {
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

	private static class QuestSizeOf extends AHQMWorker<Object, Object> {
		private int mResult;
		private FQuestSet mRef;

		private QuestSizeOf( FQuestSet ref) {
			mRef = ref;
		}

		public static int get( ACategory<FQuestSet> set) {
			QuestSizeOf size = new QuestSizeOf( null);
			set.mParentHQM.forEachQuest( size, null);
			return size.mResult;
		}

		public static int get( FQuestSet qs) {
			QuestSizeOf size = new QuestSizeOf( qs);
			qs.mParentCategory.mParentHQM.forEachQuest( size, null);
			return size.mResult;
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			if (quest.isDeleted()) {
				return null;
			}
			if (mRef == null || Utils.equals( quest.mQuestSet, mRef)) {
				++mResult;
			}
			return null;
		}
	}
}
