package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.IndexOf;
import de.doerl.hqm.base.dispatch.SizeOfQuests;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.GuiColor;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.leafs.LeafLabel;
import de.doerl.hqm.view.leafs.LeafTextBox;

class EntityQuestSetCat extends AEntityCat<FQuestSet> {
	private static final long serialVersionUID = -5930552368392528379L;
//	private static final Logger LOGGER = Logger.getLogger( EntityQuestSetCat.class.getName());
	private ABundleAction mDescAction = new DescriptionAction();
	private LeafTextBox mDesc = new LeafTextBox();
	private LeafLabel mTotal = new LeafLabel( GuiColor.BLACK.getColor(), "");
	private LeafLabel mLocked = new LeafLabel( GuiColor.CYAN.getColor(), "0 unlocked quests");
	private LeafLabel mCompleted = new LeafLabel( GuiColor.GREEN.getColor(), "0 completed quests");
	private LeafLabel mAvailible = new LeafLabel( GuiColor.LIGHT_BLUE.getColor(), "0 quests available for completion");
	private LeafLabel mUnclaimed = new LeafLabel( GuiColor.PURPLE.getColor(), "0 quests with unclaimed rewards");
	private LeafLabel mInvisible = new LeafLabel( GuiColor.LIGHT_GRAY.getColor(), "0 quests including invisible ones");
	private JScrollPane mScroll;

	public EntityQuestSetCat( FQuestSetCat cat, EditController ctrl) {
		super( cat, ctrl, new ListRenderer());
		init();
		mDesc.addClickListener( mDescAction);
		initTools();
		mTool.add( mDescAction);
		mTool.addSeparator();
		updateMoveActions();
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
	protected void doDelete() {
		mCtrl.questSetDelete( mActiv);
	}

	@Override
	protected FQuestSet getFirst() {
		return QuestSetFirst.get( mCategory);
	}

	@Override
	protected void update() {
		QuestSetUpdate.get( mCategory, mList.getModel());
	}

	private void updateActions( boolean enabled) {
		mNameAction.setEnabled( enabled);
		mDescAction.setEnabled( enabled);
		mDeleteAction.setEnabled( enabled);
	}

	@Override
	protected void updateActive( FQuestSet qs, boolean toggel) {
		if (qs == null) {
			mTotal.setText( String.format( "%d quests in total", 0));
			mDesc.setText( null);
			mList.clearSelection();
			updateActions( false);
			updateControls( false);
			mActiv = null;
		}
		else if (toggel && Utils.equals( qs, mActiv)) {
			mTotal.setText( String.format( "%d quests in total", SizeOfQuests.get( qs.mParentCategory)));
			mDesc.setText( null);
			mList.clearSelection();
			updateActions( false);
			updateControls( false);
			mActiv = null;
		}
		else {
			mTotal.setText( String.format( "%d quests in total", SizeOfQuests.get( qs)));
			mDesc.setText( qs.getDescr());
			mList.setSelectedValue( qs, true);
			updateActions( true);
			updateControls( true);
			mActiv = qs;
		}
	}

	private void updateControls( boolean enabled) {
		mScroll.setVisible( enabled);
	}

	private final class DescriptionAction extends ABundleAction {
		private static final long serialVersionUID = -8367056239473171639L;

		public DescriptionAction() {
			super( "entity.setcat.desc");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				String result = DialogTextBox.update( mActiv.getDescr(), mCtrl.getFrame(), DataBitHelper.QUEST_DESCRIPTION_LENGTH);
				if (result != null) {
					mActiv.setDescr( result);
					mCtrl.fireChanged( mCategory);
				}
			}
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
			JComponent hori = leafBoxHorizontal( ADialog.FONT_NORMAL.getSize());
			hori.add( Box.createHorizontalStrut( 24));
			hori.add( mComplete);
			add( mTitle);
			add( hori);
		}

		public Component getListCellRendererComponent( JList<? extends FQuestSet> list, FQuestSet qs, int index, boolean isSelected, boolean cellHasFocus) {
			int idx = IndexOf.getMember( qs) + 1;
			mTitle.setText( String.format( "%d. %s", idx, qs.getName()));
			mTitle.setForeground( isSelected ? SELECTED : UNSELECTED);
			boolean enabled = QuestSetIsEnabled.get( qs);
			mComplete.setText( enabled ? "0% Completed" : "Locked");
			mComplete.setEnabled( enabled);
			return this;
		}
	}

	private static class QuestSetFirst extends AHQMWorker<FQuestSet, Object> {
		private static final QuestSetFirst WORKER = new QuestSetFirst();

		private QuestSetFirst() {
		}

		public static FQuestSet get( ACategory<FQuestSet> cat) {
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
			if (quest.isEnabled()) {
				return Boolean.TRUE;
			}
			return null;
		}
	}

	private static class QuestSetUpdate extends AHQMWorker<Object, DefaultListModel<FQuestSet>> {
		private static final QuestSetUpdate WORKER = new QuestSetUpdate();

		private QuestSetUpdate() {
		}

		public static void get( ACategory<FQuestSet> set, DefaultListModel<FQuestSet> model) {
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
