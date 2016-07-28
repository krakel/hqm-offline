package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Image;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JList;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.mods.ImageLoader;
import de.doerl.hqm.view.leafs.LeafIcon;
import de.doerl.hqm.view.leafs.LeafLabel;

abstract class ADialogStacks extends ADialogList<StackEntry> {
	private static final long serialVersionUID = -47899015784969950L;

	public ADialogStacks( Window owner, boolean require) {
		super( owner, new Renderer( require), new DialogStack( owner, require), 99);
	}

	public static class Creator implements ICreator<StackEntry> {
		@Override
		public StackEntry addElement() {
			return new StackEntry();
		}

		@Override
		public ABase getBase() {
			return null;
		}
	}

	private static class Renderer extends AListCellRenderer<StackEntry> {
		private static final long serialVersionUID = 5239073494468176719L;
		private static final double ICON_ZOOM = 0.6;
		private LeafIcon mIcon = new LeafIcon();
		private LeafLabel mName = new LeafLabel( "Unknown");
		private LeafLabel mInfo = new LeafLabel( "");
		private boolean mRequire;

		public Renderer( boolean require) {
			mRequire = require;
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 1, 0, 1, 0));
			mName.setAlignmentY( TOP_ALIGNMENT);
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
		public Component getListCellRendererComponent( JList<? extends StackEntry> list, StackEntry value, int index, boolean isSelected, boolean cellHasFocus) {
			Image img = ImageLoader.getImage( createUpdater( list), value.getKey());
			mIcon.setIcon( new StackIcon( img, ICON_ZOOM, String.valueOf( value.mCount)));
			mName.setText( value.getDisplayLong());
			if (mRequire) {
				mInfo.setText( String.format( "%s, dmg %2d, count %d", value.getPrecision(), value.mDmg, value.mCount));
			}
			else {
				mInfo.setText( String.format( "dmg %2d, count %d", value.mDmg, value.mCount));
			}
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
