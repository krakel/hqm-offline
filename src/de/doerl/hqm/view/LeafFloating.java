package de.doerl.hqm.view;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JPanel;

import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.ARequirement;
import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.ImageLoader;

public class LeafFloating extends JPanel {
	private static final long serialVersionUID = 5193402879918959911L;
	private final RequirementFactory mWorker = new RequirementFactory();
	private ClickHandler mHandler = new ClickHandler();
	private AQuestTaskItems mTask;
	private Runnable mCallback = new Runnable() {
		@Override
		public void run() {
			updateIcons( null);
		}
	};

	public LeafFloating() {
		setLayout( new FlowLayout( FlowLayout.LEFT, 0, AEntity.GAP / 2));
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		addMouseListener( mHandler);
		addMouseListener( new BorderAdapter( this));
	}

	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}

	public void update( AQuestTaskItems task) {
		mTask = task;
		updateIcons( mCallback);
	}

	private void updateIcons( Runnable cb) {
		removeAll();
		if (RequirementIsEmpty.get( mTask)) {
			add( new LeafIcon( new StackIcon( null, 0.8, "0%")));
			add( Box.createHorizontalStrut( 3));
		}
		else {
			mTask.forEachRequirement( mWorker, cb);
		}
		revalidate();
		repaint();
	}

	private final class RequirementFactory extends AHQMWorker<Object, Runnable> {
		public RequirementFactory() {
		}

		@Override
		protected Object doRequirement( ARequirement req, Runnable cb) {
			AStack stk = req.getStack().mValue;
			Image img = ImageLoader.getImage( stk.getKey(), cb);
			add( new LeafIcon( new StackIcon( img, 0.8, "0%")));
			add( Box.createHorizontalStrut( 3));
			return null;
		}
	}

	private static class RequirementIsEmpty extends AHQMWorker<Boolean, Object> {
		private static final RequirementIsEmpty WORKER = new RequirementIsEmpty();

		private RequirementIsEmpty() {
		}

		public static boolean get( AQuestTaskItems task) {
			return task.forEachRequirement( WORKER, null) == null;
		}

		@Override
		protected Boolean doRequirement( ARequirement req, Object p) {
			return Boolean.TRUE;
		}
	}
}
