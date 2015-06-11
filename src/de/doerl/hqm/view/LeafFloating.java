package de.doerl.hqm.view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.ARequirement;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.IsEmpty;

public class LeafFloating extends JPanel {
	private static final long serialVersionUID = 5193402879918959911L;
	private final RequirementFactory mWorker = new RequirementFactory();
	private ClickHandler mHandler = new ClickHandler();
	private AQuestTaskItems mTask;

	public LeafFloating() {
		setLayout( new FlowLayout( FlowLayout.LEFT, 3, AEntity.GAP / 2));
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
//		setPreferredSize( new Dimension( 7 * AEntity.ICON_SIZE, 4 * AEntity.ICON_SIZE));
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
		removeAll();
		if (IsEmpty.getRequirement( mTask)) {
			add( LeafIcon.createEmpty( 0.8));
		}
		else {
			mTask.forEachRequirement( mWorker, null);
		}
		revalidate();
		repaint();
	}

	private final class RequirementFactory extends AHQMWorker<Object, Object> {
		public RequirementFactory() {
		}

		@Override
		protected Object doRequirement( ARequirement req, Object p) {
			LeafIcon leaf = new LeafIcon();
			IconUpdate.create( leaf, req.getStack(), 0.8, String.valueOf( req.getCount()));
			add( leaf);
			return null;
		}
	}
}
