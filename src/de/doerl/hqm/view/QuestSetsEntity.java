package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
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
import de.doerl.hqm.utils.Utils;

class QuestSetsEntity extends AEntity<FQuestSets> implements MouseListener {
	private static final long serialVersionUID = -5930552368392528379L;
	private static final Logger LOGGER = Logger.getLogger( QuestSetsEntity.class.getName());
	private FQuestSets mSet;
	private ListLeaf<FQuestSet> mListLeaf = new ListLeaf<FQuestSet>();
	private QuestSetLeaf mSetLeaf = new QuestSetLeaf();

	public QuestSetsEntity( EditView view, FQuestSets set) {
		super( view, new GridLayout( 1, 2));
		mSet = set;
		add( mListLeaf);
		add( mSetLeaf);
		mListLeaf.setCellRenderer( new ListLeafCellRenderer());
		mListLeaf.addMouseListener( this);
		QuestSetFactory.get( set, mListLeaf.getModel());
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
						SwingUtilities.invokeLater( new QuestSetsAction( this, qs.mParentGroup));
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

	@Override
	protected void paintComponent( Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		EditView.drawBackground( g2, this);
	}

	private static class ListLeafCellRenderer extends JPanel implements ListCellRenderer<FQuestSet> {
		private static final long serialVersionUID = 9081558438188872705L;
		private static final Color SELECTED = new Color( 0xAAAAAA);
		private static final Color UNSELECTED = new Color( 0x404040);
		private JLabel mTitle;
		private JLabel mComplete;

		ListLeafCellRenderer() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
			mTitle = new JLabel();
			mTitle.setOpaque( false);
			mTitle.setFont( AEntity.FONT_TITLE);
			mTitle.setBorder( null);
			mTitle.setAlignmentX( LEFT_ALIGNMENT);
			mComplete = new JLabel();
			mComplete.setOpaque( false);
			mComplete.setFont( AEntity.FONT_NORMAL);
			mComplete.setBorder( null);
			JPanel hori = new JPanel();
			hori.setLayout( new BoxLayout( hori, BoxLayout.X_AXIS));
			hori.setOpaque( false);
			hori.setAlignmentX( LEFT_ALIGNMENT);
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
			setEnabled( list.isEnabled());
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
		private FQuestSet mQS;
		private QuestSetsEntity mEntity;

		public QuestSetAction( QuestSetsEntity entity, FQuestSet qs) {
			mEntity = entity;
			mQS = qs;
		}

		@Override
		public void run() {
			mEntity.mSetLeaf.setSet( mQS);
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
		private ASet<FQuestSet> mSet;
		private QuestSetsEntity mEntity;

		public QuestSetsAction( QuestSetsEntity entity, ASet<FQuestSet> set) {
			mEntity = entity;
			mSet = set;
		}

		@Override
		public void run() {
			mEntity.mSetLeaf.setSets( mSet);
			mEntity.mListLeaf.unselect();
		}
	}
}
