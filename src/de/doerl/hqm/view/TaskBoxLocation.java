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
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FLocation;
import de.doerl.hqm.base.FQuestTaskLocation;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.ImageLoader;

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
		return false;
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

	private final class Renderer extends JPanel implements ListCellRenderer<FLocation> {
		private static final long serialVersionUID = -5631026361875552358L;
		private LeafIcon mIcon = new LeafIcon( StackIcon.ICON_BACK);
		private LeafLabel mName = new LeafLabel( "", true);
		private LeafLabel mDimension = new LeafLabel( "");
		private LeafLabel mCoorinate = new LeafLabel( "");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( false);
			setBorder( BorderFactory.createEmptyBorder( 2, 0, 2, 0));
			mName.setAlignmentY( TOP_ALIGNMENT);
			mIcon.setIcon( new StackIcon( null, 0.6));
			add( mIcon);
			add( Box.createHorizontalStrut( AEntity.GAP));
			add( createDescription());
		}

		private JComponent createDescription() {
			JComponent result = AEntity.leafBox( BoxLayout.Y_AXIS);
			result.add( mName);
			result.add( mDimension);
			result.add( mCoorinate);
			return result;
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends FLocation> list, FLocation loc, int index, boolean isSelected, boolean cellHasFocus) {
			Runnable cb = null;
			AStack stk = loc.mIcon.mValue;
			Image img = ImageLoader.getImage( stk.getKey(), cb);
			mIcon.setIcon( new StackIcon( img, 0.8, null));
			mName.setText( loc.mName.mValue);
			mDimension.setText( String.format( "Dimension %d, [%d radius]", loc.mDim.mValue, loc.mRadius.mValue));
			mCoorinate.setText( String.format( "(%d, %d, %d)", loc.mX.mValue, loc.mY.mValue, loc.mZ.mValue));
			return this;
		}
	}
}
