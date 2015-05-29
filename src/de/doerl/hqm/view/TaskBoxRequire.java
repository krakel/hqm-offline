package de.doerl.hqm.view;

import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;

import de.doerl.hqm.base.AQuestTaskItems;
import de.doerl.hqm.base.ARequirement;
import de.doerl.hqm.base.AStack;

class TaskBoxRequire extends ATaskBox {
	private static final long serialVersionUID = -2578865094375861527L;
	private static final LeafButton BTN1 = new LeafButton( "Manual detect");
	private static final LeafButton BTN2 = new LeafButton( "Manual submit");
	private static final LeafButton BTN3 = new LeafButton( "Select task");
	private LeafFloating mFloating = new LeafFloating();
	private AQuestTaskItems mTask;

	public TaskBoxRequire( AQuestTaskItems task) {
		mTask = task;
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
		Vector<AStack> result = DialogListStacks.updateB( mTask.mRequirements, owner);
		if (result != null) {
			updateStacks( mTask.mRequirements, result);
			return true;
		}
		return false;
	}

	@Override
	public void removeClickListener( ActionListener l) {
		mFloating.removeClickListener( l);
	}

	@Override
	public void update() {
		mFloating.update( mTask);
	}

	private void updateStacks( Vector<ARequirement> param, Vector<AStack> stks) {
		param.clear();
		for (int i = 0; i < stks.size(); i++) {
			AStack itemStack = stks.get( i);
			if (itemStack != null) {
				mTask.createRequirement( false);
			}
		}
	}
}
