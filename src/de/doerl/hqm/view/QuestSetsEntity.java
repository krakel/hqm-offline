package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ASet;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSets;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.QuestSetIndex;
import de.doerl.hqm.base.dispatch.QuestSetSizeOf;
import de.doerl.hqm.quest.GuiColor;
import de.doerl.hqm.utils.Utils;

class QuestSetsEntity extends AEntity<FQuestSets> implements MouseListener {
	private static final long serialVersionUID = -5930552368392528379L;
	private static final Logger LOGGER = Logger.getLogger( QuestSetsEntity.class.getName());
	private FQuestSets mSet;
	private DefaultListModel<FQuestSet> mListModel = new DefaultListModel<FQuestSet>();
	private JList<FQuestSet> mList;
	private JPanel mLeafLeft = leafPanel( true);
	private JPanel mLeafRight = leafPanel( false);
	private JTextArea mDesc = leafTextArea();
	private JLabel mTotal = leafLabel( GuiColor.BLACK.getColor(), "");
	private JLabel mLocked = leafLabel( GuiColor.CYAN.getColor(), "0 unlocked quests");
	private JLabel mCompleted = leafLabel( GuiColor.GREEN.getColor(), "0 completed quests");
	private JLabel mAvailible = leafLabel( GuiColor.LIGHT_BLUE.getColor(), "0 quests available for completion");
	private JLabel mUnclaimed = leafLabel( GuiColor.PURPLE.getColor(), "0 quests with unclaimed rewards");
	private JLabel mInvisible = leafLabel( GuiColor.LIGHT_GRAY.getColor(), "0 quests including invisible ones");
	private JScrollPane mScroll;

	public QuestSetsEntity( EditView view, FQuestSets set) {
		super( view, new GridLayout( 1, 2));
		mSet = set;
		createLeft( mLeafLeft);
		createRight( mLeafRight);
		add( mLeafLeft);
		add( mLeafRight);
		QuestSetFactory.get( set, mListModel);
	}

	private void createLeft( JPanel leaf) {
		mList = leafList( mListModel);
		mList.setCellRenderer( new CellRenderer());
		mList.addMouseListener( this);
		leaf.add( leafScoll( mList, 10000));
	}

	private void createRight( JPanel leaf) {
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
		return mSet;
	}

	@Override
	public void mouseClicked( MouseEvent evt) {
		try {
			JList<?> list = (JList<?>) evt.getSource();
			int row = list.locationToIndex( evt.getPoint());
			if (row >= 0) {
				ListModel<?> model = list.getModel();
				FQuestSet qs = (FQuestSet) model.getElementAt( row);
				switch (evt.getButton()) {
					case MouseEvent.BUTTON1:
						SwingUtilities.invokeLater( new QuestSetAction( this, qs));
						break;
					case MouseEvent.BUTTON3:
						SwingUtilities.invokeLater( new QuestSetsAction( this, qs.mParentSet));
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

	public void setSet( FQuestSet qs) {
		mTotal.setText( String.format( "%d quests in total", QuestSetSizeOf.get( qs)));
		mDesc.setText( qs.mDesc.mValue);
		mScroll.setVisible( true);
	}

	public void setSets( ASet<FQuestSet> set) {
		mTotal.setText( String.format( "%d quests in total", QuestSetSizeOf.get( set)));
		mDesc.setText( null);
		mScroll.setVisible( false);
	}

	private static class CellRenderer extends JPanel implements ListCellRenderer<FQuestSet> {
		private static final long serialVersionUID = 9081558438188872705L;
		private JLabel mTitle = leafTitle( UNSELECTED, "");
		private JLabel mComplete = leafLabel( Color.BLACK, "");

		public CellRenderer() {
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
			mTitle.setText( String.format( "%d. %s", idx, qs.getName()));
			mTitle.setForeground( isSelected ? SELECTED : UNSELECTED);
			boolean enabled = QuestFactory.isEnabled( qs);
			mComplete.setText( enabled ? "0% Completed" : "Locked");
			mComplete.setEnabled( enabled);
			return this;
		}
	}

	private static class QuestFactory extends AHQMWorker<Object, Object> {
//		private static final QuestFactory WORKER = new QuestFactory();
		private QuestFactory() {
		}

		public static boolean isEnabled( FQuestSet qs) {
			return false;
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			// 1. isLinkFree() // verlinkte quests
//			return  && ( triggerType.doesWorkAsInvisible() || isVisible( playerName)) && enabledParentEvaluator.isValid( playerName);
			return null;
		}
	}

	private static class QuestSetAction implements Runnable {
		private QuestSetsEntity mEntity;
		private FQuestSet mQS;

		public QuestSetAction( QuestSetsEntity entity, FQuestSet qs) {
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

	private static class QuestSetsAction implements Runnable {
		private QuestSetsEntity mEntity;
		private ASet<FQuestSet> mSet;

		public QuestSetsAction( QuestSetsEntity entity, ASet<FQuestSet> set) {
			mEntity = entity;
			mSet = set;
		}

		@Override
		public void run() {
			mEntity.setSets( mSet);
			mEntity.mList.clearSelection();
		}
	}
}
