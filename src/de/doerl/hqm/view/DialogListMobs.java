package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ImageLoader;

class DialogListMobs extends ADialogList<FMob> {
	private static final long serialVersionUID = 7903951948404166751L;

	public DialogListMobs( Window owner) {
		super( owner, new Renderer(), new Editor( owner));
		setTheme( "edit.mob.theme");
	}

	public static boolean update( FQuestTaskMob task, Window owner) {
		DialogListMobs dlg = new DialogListMobs( owner);
		dlg.createMain( new Creator( task));
		dlg.updateMain( task);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.updateResult( task);
			return true;
		}
		else {
			return false;
		}
	}

	private void updateMain( FQuestTaskMob task) {
		mModel.clear();
		for (FMob mob : task.mMobs) {
			mModel.addElement( mob);
		}
	}

	private void updateResult( FQuestTaskMob task) {
		task.mMobs.clear();
		for (int i = 0; i < mModel.size(); ++i) {
			FMob e = mModel.get( i);
			task.mMobs.add( e);
		}
	}

	private static class Creator implements ICreator<FMob> {
		private FQuestTaskMob mTask;

		private Creator( FQuestTaskMob task) {
			mTask = task;
		}

		@Override
		public FMob addElement() {
			return mTask.createMob( null, "new");
		}
	}

	private static class Editor extends ADialogEdit<FMob> {
		private static final long serialVersionUID = 7720930197206098500L;
		private JTextField mName = new JTextField();
		private JTextField mMob = new JTextField();
		private JTextField mKills = new JTextField();
		private JCheckBox mExact = new JCheckBox();

		public Editor( Window owner) {
			super( owner);
			setTheme( "edit.item.theme");
			addAction( BTN_CANCEL, DialogResult.CANCEL);
			addAction( BTN_OK, DialogResult.APPROVE);
			addEscapeAction();
			mExact.setOpaque( false);
			Insets in = mExact.getInsets();
			mExact.setBorder( BorderFactory.createEmptyBorder( in.top, 0, in.bottom, in.right));
			mKills.addKeyListener( new KeyAdaptorInterger());
			createMain();
		}

		@Override
		public FMob addElement( ICreator<FMob> creator) {
			mName.setText( "unknown");
			mMob.setText( "unknown");
			mKills.setText( "1");
			mExact.setSelected( true);
			if (showDialog() == DialogResult.APPROVE) {
				FMob entry = creator.addElement();
				updateResult( entry);
				return entry;
			}
			else {
				return null;
			}
		}

		private ParallelGroup addLine( GroupLayout layout, ParallelGroup leftGrp, ParallelGroup rightGrp, String descr, JComponent comp) {
			JLabel lbl = new JLabel( descr);
			leftGrp.addComponent( lbl);
			rightGrp.addComponent( comp);
			return layout.createParallelGroup( Alignment.BASELINE).addComponent( lbl).addComponent( comp);
		}

		@Override
		public FMob changeElement( FMob entry) {
			mName.setText( entry.mName);
			mMob.setText( entry.mMob);
			mKills.setText( String.valueOf( entry.mKills));
			mExact.setSelected( entry.mExact);
			if (showDialog() == DialogResult.APPROVE) {
				updateResult( entry);
				return entry;
			}
			else {
				return null;
			}
		}

		@Override
		protected void createMain() {
			JPanel box = new JPanel();
			GroupLayout layout = new GroupLayout( box);
			box.setLayout( layout);
			box.setOpaque( false);
//			box.setBorder( BorderFactory.createLineBorder( Color.RED));
			layout.setAutoCreateGaps( true);
			SequentialGroup hori = layout.createSequentialGroup();
			SequentialGroup vert = layout.createSequentialGroup();
			ParallelGroup leftGrp = layout.createParallelGroup();
			ParallelGroup rightGrp = layout.createParallelGroup();
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Name", mName));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Mob", mMob));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Kills", mKills));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Exact", mExact));
			hori.addGroup( leftGrp);
			hori.addGroup( rightGrp);
			layout.setHorizontalGroup( hori);
			layout.setVerticalGroup( vert);
			mMain.add( box);
		}

		private void updateResult( FMob entry) {
			entry.mName = mName.getText();
			entry.mMob = mMob.getText();
			entry.mKills = Utils.parseInteger( mKills.getText(), 1);
			entry.mExact = mExact.isSelected();
		}
	}

	private static class Renderer extends JPanel implements ListCellRenderer<FMob> {
		private static final long serialVersionUID = -430644712741965086L;
		private LeafIcon mIcon = new LeafIcon( StackIcon.ICON_BACK);
		private LeafLabel mName = new LeafLabel( "Unknown");
		private LeafLabel mInfo = new LeafLabel( "");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 1, 0, 1, 0));
			mIcon.setIcon( new StackIcon( null, 0.6));
			add( mIcon);
			add( Box.createHorizontalStrut( 5));
			add( createBox());
		}

		private Box createBox() {
			mName.setFont( AEntity.FONT_STACK);
			mInfo.setFont( AEntity.FONT_SMALL);
			mName.setAlignmentX( LEFT_ALIGNMENT);
			mInfo.setAlignmentX( LEFT_ALIGNMENT);
			Box box = Box.createVerticalBox();
			box.setAlignmentY( CENTER_ALIGNMENT);
			box.add( mName);
			box.add( mInfo);
			return box;
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends FMob> list, FMob value, int index, boolean isSelected, boolean cellHasFocus) {
			Image img = ImageLoader.getImage( value.mIcon, null);
			mIcon.setIcon( new StackIcon( img, 0.6, String.valueOf( value.mKills)));
			mName.setText( value.mName);
			mInfo.setText( String.format( "(%2s %s) x%d", value.mExact ? '!' : '~', value.mMob, value.mKills));
			if (isSelected) {
				setBackground( list.getSelectionBackground());
			}
			else {
				setBackground( list.getBackground());
			}
			return this;
		}
	}
}
