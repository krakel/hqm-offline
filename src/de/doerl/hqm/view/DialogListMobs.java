package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.utils.mods.ImageLoader;
import de.doerl.hqm.utils.mods.Matcher;
import de.doerl.hqm.view.LeafSearch.ISearchListener;
import de.doerl.hqm.view.LeafSearch.SearchEvent;

class DialogListMobs extends ADialogList<FMob> {
	private static final long serialVersionUID = 7903951948404166751L;

	public DialogListMobs( Window owner) {
		super( owner, new Renderer(), new Editor( owner), 4);
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
		updateBtn();
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
			FMob mob = mTask.createMob();
			mob.setName( "new mob");
			return mob;
		}

		public ABase getBase() {
			return mTask;
		}
	}

	private static class Editor extends ADialogEdit<FMob> {
		private static final long serialVersionUID = 7720930197206098500L;
		private JTextField mIcon = new TextFieldAscii();
		private JTextField mIconDmg = new TextFieldInteger();
		private JTextField mName = new TextFieldAscii();
		private JTextField mMob = new TextFieldAscii();
		private JTextField mKills = new TextFieldInteger();
		private JCheckBox mExact = new JCheckBox();
		private LeafSearch mSearch = new LeafSearch();

		public Editor( Window owner) {
			super( owner);
			setTheme( "edit.item.theme");
			addAction( BTN_CANCEL, DialogResult.CANCEL);
			addAction( BTN_OK, DialogResult.APPROVE);
			addEscapeAction();
			mExact.setOpaque( false);
			Insets in = mExact.getInsets();
			mExact.setBorder( BorderFactory.createEmptyBorder( in.top, 0, in.bottom, in.right));
			createMain();
			mSearch.addSearchListener( new ISearchListener() {
				@Override
				public void doAction( SearchEvent event) {
					Matcher match = event.getMatch();
					if (match != null) {
						mIcon.setText( match.getName());
						mIconDmg.setText( String.valueOf( match.getDamage()));
					}
				}
			});
			mName.setPreferredSize( new Dimension( 200, mName.getPreferredSize().height));
		}

		@Override
		public FMob addElement( ICreator<FMob> creator) {
			mIcon.setText( null);
			mIconDmg.setText( "0");
			mName.setText( "unknown");
			mMob.setText( "unknown");
			mKills.setText( "1");
			mExact.setSelected( false);
			if (showDialog() == DialogResult.APPROVE) {
				FMob entry = creator.addElement();
				updateResult( entry);
				return entry;
			}
			else {
				return null;
			}
		}

		@Override
		public FMob changeElement( FMob entry) {
			FItemStack icon = entry.mIcon;
			if (icon != null) {
				mIcon.setText( icon.getItem());
				mIconDmg.setText( String.valueOf( icon.getDamage()));
			}
			else {
				mIconDmg.setText( "0");
			}
			mName.setText( entry.getName());
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

		private Group createLeft( GroupLayout layout, Group hori) {
			Group vert = layout.createSequentialGroup();
			Group leftGrp = layout.createParallelGroup();
			Group rightGrp = layout.createParallelGroup();
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Icon", mIcon));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Icon Damage", mIconDmg));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Name", mName));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Mob", mMob));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Kills", mKills));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Exact", mExact));
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

		private void updateResult( FMob entry) {
			String icon = mIcon.getText();
			if (Utils.validString( icon)) {
				entry.mIcon = new FItemStack( icon, Utils.parseInteger( mIconDmg.getText(), 0), 1);
			}
			else {
				entry.mIcon = null;
			}
			entry.setName( DataBitHelper.NAME_LENGTH.truncate( mName.getText()));
			entry.mMob = DataBitHelper.MOB_ID_LENGTH.truncate( mMob.getText());
			entry.mKills = Utils.parseInteger( mKills.getText(), 1);
			entry.mExact = mExact.isSelected();
		}
	}

	private static class Renderer extends AListCellRenderer<FMob> {
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
		public Component getListCellRendererComponent( JList<? extends FMob> list, FMob value, int index, boolean isSelected, boolean cellHasFocus) {
			Image img = ImageLoader.getImage( value.mIcon, createUpdater( list));
			mIcon.setIcon( new StackIcon( img, ICON_ZOOM, String.valueOf( value.mKills)));
			mName.setText( value.getName());
			mInfo.setText( String.format( "(%s %s) x%d", value.mExact ? '!' : '~', value.mMob, value.mKills));
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
