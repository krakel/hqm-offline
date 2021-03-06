package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.quest.Visibility;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ImageLoader;
import de.doerl.hqm.utils.mods.ItemNEI;
import de.doerl.hqm.view.leafs.LeafIcon;
import de.doerl.hqm.view.leafs.LeafLabel;
import de.doerl.hqm.view.leafs.LeafSearch;
import de.doerl.hqm.view.leafs.LeafSearch.ISearchListener;
import de.doerl.hqm.view.leafs.LeafSearch.SearchEvent;

class DialogListLocations extends ADialogList<FLocation> {
	private static final long serialVersionUID = 7903951948404166751L;

	public DialogListLocations( Window owner) {
		super( owner, new Renderer(), new Editor( owner), 4);
		setTheme( "edit.location.theme");
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
		updateBtn();
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
			FLocation loc = mTask.createLocation();
			loc.setName( "new");
			return loc;
		}

		@Override
		public ABase getBase() {
			return mTask;
		}
	}

	private static class Editor extends ADialogEdit<FLocation> {
		private static final long serialVersionUID = 7720930197206098500L;
		private JTextField mIcon = new TextFieldAscii();
		private JTextField mIconDmg = new TextFieldInteger();
		private JTextField mName = new TextFieldAscii();
		private JTextField mX = new TextFieldInteger();
		private JTextField mY = new TextFieldInteger();
		private JTextField mZ = new TextFieldInteger();
		private JTextField mRadius = new TextFieldInteger();
		private JTextField mDim = new TextFieldInteger();
		private JComboBox<Visibility> mVisible = new JComboBox<>( Visibility.values());
		private LeafSearch mSearch = new LeafSearch();

		public Editor( Window owner) {
			super( owner);
			setTheme( "edit.item.theme");
			addAction( BTN_CANCEL, DialogResult.CANCEL);
			addAction( BTN_OK, DialogResult.APPROVE);
			addEscapeAction();
			createMain();
			mSearch.addSearchListener( new ISearchListener() {
				@Override
				public void doAction( SearchEvent event) {
					ItemNEI item = event.getItem();
					if (item != null) {
						mIcon.setText( item.mName);
						mIconDmg.setText( String.valueOf( item.mDamage));
					}
				}
			});
			mName.setPreferredSize( new Dimension( 200, mName.getPreferredSize().height));
		}

		@Override
		public FLocation addElement( ICreator<FLocation> creator) {
			mIcon.setText( null);
			mIconDmg.setText( "0");
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

		@Override
		public FLocation changeElement( FLocation entry) {
			FItemStack icon = entry.mIcon;
			if (icon != null) {
				mIcon.setText( icon.getItem());
				mIconDmg.setText( String.valueOf( icon.getDamage()));
			}
			else {
				mIconDmg.setText( "0");
			}
			mName.setText( entry.getName());
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

		private Group createLeft( GroupLayout layout, Group hori) {
			Group vert = layout.createSequentialGroup();
			Group leftGrp = layout.createParallelGroup();
			Group rightGrp = layout.createParallelGroup();
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Icon", mIcon));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Icon Damage", mIconDmg));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Name", mName));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "x", mX));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "y", mY));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "z", mZ));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Radius", mRadius));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Dimension", mDim));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Visibility", mVisible));
			hori.addGroup( leftGrp);
			hori.addGroup( rightGrp);
			return vert;
		}

		@Override
		protected void createMain() {
			JPanel box = new JPanel();
			GroupLayout layout = new GroupLayout( box);
			box.setLayout( layout);
			box.setOpaque( false);
//			box.setBorder( BorderFactory.createLineBorder( Color.RED));
			layout.setAutoCreateGaps( true);
			Group hori = layout.createSequentialGroup();
			Group vert = layout.createParallelGroup();
			vert.addGroup( createLeft( layout, hori));
			vert.addComponent( mSearch);
			hori.addComponent( mSearch);
			layout.setHorizontalGroup( hori);
			layout.setVerticalGroup( vert);
			mMain.add( box);
			mSearch.doSearch();
		}

		private void updateResult( FLocation entry) {
			String icon = mIcon.getText();
			if (Utils.validString( icon)) {
				entry.mIcon = new FItemStack( icon, Utils.parseInteger( mIconDmg.getText(), 0), 1, null);
			}
			else {
				entry.mIcon = null;
			}
			entry.setName( DataBitHelper.NAME_LENGTH.truncate( mName.getText()));
			entry.mX = Utils.parseInteger( mX.getText(), 0);
			entry.mY = Utils.parseInteger( mY.getText(), 0);
			entry.mZ = Utils.parseInteger( mZ.getText(), 0);
			entry.mRadius = Utils.parseInteger( mRadius.getText(), 3);
			entry.mDim = Utils.parseInteger( mDim.getText(), 0);
			entry.mVisibility = Visibility.get( mVisible.getSelectedIndex());
		}
	}

	private static class Renderer extends AListCellRenderer<FLocation> {
		private static final long serialVersionUID = -430644712741965086L;
		private static final double ICON_ZOOM = 0.6;
		private LeafIcon mIcon = new LeafIcon();
		private LeafLabel mName = new LeafLabel( "Unknown");
		private LeafLabel mInfo = new LeafLabel( "");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 1, 0, 1, 0));
			mIcon.setIcon( new StackIcon( ICON_ZOOM));
			add( mIcon);
			add( Box.createHorizontalStrut( 5));
			add( createBox());
		}

		private Box createBox() {
			mName.setFont( ADialog.FONT_STACK);
			mInfo.setFont( ADialog.FONT_SMALL);
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
			Image img = ImageLoader.getImage( createUpdater( list), value.mIcon);
			mIcon.setIcon( new StackIcon( img, ICON_ZOOM));
			mName.setText( value.getName());
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
