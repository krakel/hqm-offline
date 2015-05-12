package de.doerl.hqm.view;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import de.doerl.hqm.base.ASet;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.QuestSetSizeOf;
import de.doerl.hqm.quest.GuiColor;

class QuestSetLeaf extends JPanel {
	private static final long serialVersionUID = 3043305476206141325L;
	private JTextArea mDescription = new JTextArea();
	private JLabel mTotal = new JLabel();
	private JLabel mLocked = new JLabel( "0 unlocked quests");
	private JLabel mCompleted = new JLabel( "0 completed quests");
	private JLabel mAvailible = new JLabel( "0 quests available for completion");
	private JLabel mUnclaimed = new JLabel( "0 quests with unclaimed rewards");
	private JLabel mInvisible = new JLabel( "0 quests including invisible ones");
	private JScrollPane mScroll;

	public QuestSetLeaf() {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
		setOpaque( false);
		setBorder( BorderFactory.createEmptyBorder( 40, 10, 40, 40));
		mDescription.setLineWrap( true);
		mDescription.setWrapStyleWord( true);
		mDescription.setOpaque( false);
		mDescription.setFont( AEntity.FONT_NORMAL);
		mDescription.setPreferredSize( new Dimension( 200, 80));
		mTotal.setForeground( GuiColor.BLACK.getColor());
		mLocked.setForeground( GuiColor.CYAN.getColor());
		mCompleted.setForeground( GuiColor.GREEN.getColor());
		mAvailible.setForeground( GuiColor.LIGHT_BLUE.getColor());
		mUnclaimed.setForeground( GuiColor.PURPLE.getColor());
		mInvisible.setForeground( GuiColor.LIGHT_GRAY.getColor());
//		top.add( Box.createVerticalStrut( 100));
		mScroll = new JScrollPane( mDescription, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mScroll.getViewport().setOpaque( false);
		mScroll.setVisible( false);
		addComponent( mScroll);
		addComponent( mTotal);
		addComponent( mLocked);
		addComponent( mCompleted);
		addComponent( mAvailible);
		addComponent( mUnclaimed);
		addComponent( mInvisible);
		add( Box.createVerticalGlue());
	}

	private void addComponent( JComponent comp) {
		comp.setOpaque( false);
		comp.setBorder( null);
		comp.setAlignmentX( LEFT_ALIGNMENT);
		add( comp);
	}

	public void setSet( FQuestSet qs) {
		mTotal.setText( String.format( "%d quests in total", QuestSetSizeOf.get( qs)));
		mDescription.setText( qs.mDesc.mValue);
		mScroll.setVisible( true);
	}

	public void setSets( ASet<FQuestSet> set) {
		mTotal.setText( String.format( "%d quests in total", QuestSetSizeOf.get( set)));
		mDescription.setText( null);
		mScroll.setVisible( false);
	}
}
