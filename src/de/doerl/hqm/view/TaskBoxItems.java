package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.Box;

import de.doerl.hqm.base.AQuestTaskItems;

class TaskBoxItems extends ATaskBox {
	private static final long serialVersionUID = -2578865094375861527L;
	private LeafFloating mFloating = new LeafFloating();
	private AQuestTaskItems mTask;

	public TaskBoxItems( AQuestTaskItems task) {
		mTask = task;
		mFloating.setPreferredSize( new Dimension( WIDTH, 4 * AEntity.ICON_SIZE));
		add( AEntity.leafScoll( mFloating, HEIGHT));
		switch (mTask.getTaskTyp()) {
			case TASK_ITEMS_DETECT:
				add( Box.createVerticalStrut( AEntity.GAP));
				add( AEntity.leafButtons( new LeafButton( "Manual detect")));
				break;
			case TASK_ITEMS_CONSUME:
			case TASK_ITEMS_CONSUME_QDS:
				add( Box.createVerticalStrut( AEntity.GAP));
				add( AEntity.leafButtons( new LeafButton( "Manual submit"), new LeafButton( "Select task")));
				break;
			default:
		}
	}

	@Override
	public void addClickListener( ActionListener l) {
		mFloating.addClickListener( l);
	}

	@Override
	public AQuestTaskItems getTask() {
		return mTask;
	}

	@Override
	public boolean onAction( Window owner) {
		return DialogListRequirements.update( mTask, owner);
	}

	@Override
	public void removeClickListener( ActionListener l) {
		mFloating.removeClickListener( l);
	}

	@Override
	public void update() {
		mFloating.update( mTask);
	}
}
