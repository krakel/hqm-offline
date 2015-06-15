package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.quest.Visibility;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ImageLoader;

class DialogListLocations extends ADialogList<FLocation> {
	private static final long serialVersionUID = 7903951948404166751L;

	public DialogListLocations( Window owner) {
		super( owner, new Renderer(), new Editor( owner));
		setTheme( "edit.mob.theme");
	}

	public static boolean update( FQuestTaskLocation task, Window owner) {
		DialogListLocations dlg = new DialogListLocations( owner);
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

	private void updateMain( FQuestTaskLocation task) {
		mModel.clear();
		for (FLocation loc : task.mLocations) {
			mModel.addElement( loc);
		}
	}

	private void updateResult( FQuestTaskLocation task) {
		task.mLocations.clear();
		for (int i = 0; i < mModel.size(); ++i) {
			FLocation e = mModel.get( i);
			task.mLocations.add( e);
		}
	}

	private static class Creator implements ICreator<FLocation> {
		private FQuestTaskLocation mTask;

		private Creator( FQuestTaskLocation task) {
			mTask = task;
		}

		@Override
		public FLocation addElement() {
			return mTask.createLocation( "new");
		}

		public ABase getBase() {
			return mTask;
		}
	}

	private static class Editor extends ADialogEdit<FLocation> {
		private static final long serialVersionUID = 7720930197206098500L;
		private JTextField mName = new JTextField();
		private JTextField mX = new JTextField();
		private JTextField mY = new JTextField();
		private JTextField mZ = new JTextField();
		private JTextField mRadius = new JTextField();
		private JTextField mDim = new JTextField();
		private JComboBox<Visibility> mVisible = new JComboBox<>( Visibility.values());

		public Editor( Window owner) {
			super( owner);
			setTheme( "edit.item.theme");
			addAction( BTN_CANCEL, DialogResult.CANCEL);
			addAction( BTN_OK, DialogResult.APPROVE);
			addEscapeAction();
			KeyAdaptorInterger kh = new KeyAdaptorInterger();
			mX.addKeyListener( kh);
			mY.addKeyListener( kh);
			mZ.addKeyListener( kh);
			mRadius.addKeyListener( kh);
			mDim.addKeyListener( kh);
			createMain();
		}

		@Override
		public FLocation addElement( ICreator<FLocation> creator) {
			mName.setText( "unknown");
			mX.setText( String.valueOf( 0));
			mY.setText( String.valueOf( 0));
			mZ.setText( String.valueOf( 0));
			mRadius.setText( String.valueOf( 3));
			mDim.setText( String.valueOf( 0));
			mVisible.setSelectedItem( Visibility.LOCATION);
			if (showDialog() == DialogResult.APPROVE) {
				FLocation entry = creator.addElement();
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
		public FLocation changeElement( FLocation entry) {
			mName.setText( entry.mName);
			mX.setText( String.valueOf( entry.mX));
			mY.setText( String.valueOf( entry.mY));
			mZ.setText( String.valueOf( entry.mZ));
			mRadius.setText( String.valueOf( entry.mRadius));
			mDim.setText( String.valueOf( entry.mDim));
			mVisible.setSelectedItem( entry.mVisibility);
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
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "x", mX));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "y", mY));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "z", mZ));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Radius", mRadius));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Dimension", mDim));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Visibility", mVisible));
			hori.addGroup( leftGrp);
			hori.addGroup( rightGrp);
			layout.setHorizontalGroup( hori);
			layout.setVerticalGroup( vert);
			mMain.add( box);
		}

		private void updateResult( FLocation loc) {
			loc.mName = mName.getText();
			loc.mX = Utils.parseInteger( mX.getText(), 0);
			loc.mY = Utils.parseInteger( mY.getText(), 0);
			loc.mZ = Utils.parseInteger( mZ.getText(), 0);
			loc.mRadius = Utils.parseInteger( mRadius.getText(), 3);
			loc.mDim = Utils.parseInteger( mDim.getText(), 0);
			loc.mVisibility = Visibility.get( mVisible.getSelectedIndex());
		}
	}

	private static class Renderer extends AListCellRenderer<FLocation> {
		private static final long serialVersionUID = -430644712741965086L;
		private LeafIcon mIcon = new LeafIcon();
		private LeafLabel mName = new LeafLabel( "Unknown");
		private LeafLabel mInfo = new LeafLabel( "");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 1, 0, 1, 0));
			mIcon.setIcon( new StackIcon());
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
		public Component getListCellRendererComponent( JList<? extends FLocation> list, FLocation value, int index, boolean isSelected, boolean cellHasFocus) {
			mIcon.setIcon( new StackIcon( ImageLoader.getImage( value.mIcon, createUpdater( list))));
			mName.setText( value.mName);
			mInfo.setText( String.format( "%s (dim %d) (%d, %d, %d)[radius %d]", value.mVisibility, value.mDim, value.mX, value.mY, value.mZ, value.mRadius));
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
