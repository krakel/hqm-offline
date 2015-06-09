package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.Box;

import de.doerl.hqm.base.AQuestTaskItems;

class TaskBoxItems extends ATaskBox {
	private static final long serialVersionUID = -2578865094375861527L;
	private static final LeafButton BTN1 = new LeafButton( "Manual detect");
	private static final LeafButton BTN2 = new LeafButton( "Manual submit");
	private static final LeafButton BTN3 = new LeafButton( "Select task");
	private LeafFloating mFloating = new LeafFloating();
	private AQuestTaskItems mTask;

	public TaskBoxItems( AQuestTaskItems task) {
		mTask = task;
		mFloating.setPreferredSize( new Dimension( 100, 30));
		add( AEntity.leafScoll( mFloating, HEIGHT));
		add( Box.createVerticalStrut( AEntity.GAP));
		switch (mTask.getTaskTyp()) {
			case TASK_ITEMS_DETECT:
				add( AEntity.leafButtons( BTN1));
				break;
			case TASK_ITEMS_CONSUME:
			case TASK_ITEMS_CONSUME_QDS:
				add( AEntity.leafButtons( BTN2, BTN3));
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
