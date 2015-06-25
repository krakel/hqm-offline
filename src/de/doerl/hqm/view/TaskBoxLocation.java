package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;

import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.mods.ImageLoader;

class TaskBoxLocation extends ATaskBox {
	private static final long serialVersionUID = -5352886896563883981L;
	private FQuestTaskLocation mTask;
	private LeafList<FLocation> mList = new LeafList<>();

	public TaskBoxLocation( FQuestTaskLocation task) {
		mTask = task;
		mList.setCellRenderer( new Renderer());
		mList.addMouseListener( new BorderAdapter( mList));
		add( AEntity.leafScoll( mList, HEIGHT));
	}

	@Override
	public void addClickListener( ActionListener l) {
		mList.addClickListener( l);
	}

	@Override
	public FQuestTaskLocation getTask() {
		return mTask;
	}

	@Override
	public boolean onAction( Window owner) {
		return DialogListLocations.update( mTask, owner);
	}

	@Override
	public void removeClickListener( ActionListener l) {
		mList.removeClickListener( l);
	}

	@Override
	public void update() {
		LocationFactory.get( mTask, mList.getModel());
	}

	private static class LocationFactory extends AHQMWorker<Object, DefaultListModel<FLocation>> {
		private static final LocationFactory WORKER = new LocationFactory();

		private LocationFactory() {
		}

		public static void get( FQuestTaskLocation task, DefaultListModel<FLocation> model) {
			model.clear();
			task.forEachLocation( WORKER, model);
		}

		@Override
		public Object forLocation( FLocation loc, DefaultListModel<FLocation> model) {
			model.addElement( loc);
			return null;
		}
	}

	private final class Renderer extends AListCellRenderer<FLocation> {
		private static final long serialVersionUID = -5631026361875552358L;
		private LeafIcon mIcon = new LeafIcon();
		private LeafLabel mName = new LeafLabel( "", true);
		private LeafLabel mDimension = new LeafLabel( "");
		private LeafLabel mCoorinate = new LeafLabel( "");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( false);
			setBorder( BorderFactory.createEmptyBorder( 2, 0, 2, 0));
			mName.setAlignmentY( TOP_ALIGNMENT);
			mIcon.setIcon( new StackIcon());
			add( mIcon);
			add( Box.createHorizontalStrut( AEntity.GAP));
			add( createBox());
		}

		private JComponent createBox() {
			JComponent box = AEntity.leafBox( BoxLayout.Y_AXIS);
			box.add( mName);
			box.add( mDimension);
			box.add( mCoorinate);
			return box;
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends FLocation> list, FLocation loc, int index, boolean isSelected, boolean cellHasFocus) {
			Image img = ImageLoader.getImage( loc.mIcon, createUpdater( list));
			mIcon.setIcon( new StackIcon( img));
			mName.setText( loc.mName);
			mDimension.setText( String.format( "%s (dim %d) [radius %d]", loc.mVisibility, loc.mDim, loc.mRadius));
			mCoorinate.setText( String.format( "(%d, %d, %d)", loc.mX, loc.mY, loc.mZ));
			return this;
		}
	}
}
