package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Image;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JList;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.utils.mods.ImageLoader;

abstract class ADialogStacks extends ADialogList<StackEntry> {
	private static final long serialVersionUID = -47899015784969950L;

	public ADialogStacks( Window owner, boolean require) {
		super( owner, new Renderer( require), new DialogStack( owner, require));
	}

	protected static class Creator implements ICreator<StackEntry> {
		@Override
		public StackEntry addElement() {
			return new StackEntry();
		}

		public ABase getBase() {
			return null;
		}
	}

	private static class Renderer extends AListCellRenderer<StackEntry> {
		private static final long serialVersionUID = 5239073494468176719L;
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
		public Component getListCellRendererComponent( JList<? extends StackEntry> list, StackEntry value, int index, boolean isSelected, boolean cellHasFocus) {
			Image img = ImageLoader.getImage( value.getKey(), createUpdater( list));
			mIcon.setIcon( new StackIcon( img, String.valueOf( value.mCount)));
			mName.setText( value.getName());
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
