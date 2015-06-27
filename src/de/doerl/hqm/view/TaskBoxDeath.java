package de.doerl.hqm.view;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.Box;

import de.doerl.hqm.base.FQuestTaskDeath;

class TaskBoxDeath extends ATaskBox {
	private static final long serialVersionUID = -6803523729510354404L;
	private ClickHandler mHandler = new ClickHandler();
	private FQuestTaskDeath mTask;
	private LeafLabel mDeaths = new LeafLabel( "");

	public TaskBoxDeath( FQuestTaskDeath task) {
		mTask = task;
		setPreferredSize( new Dimension( WIDTH, 5 * AEntity.ICON_SIZE));
		mDeaths.setPreferredSize( new Dimension( WIDTH, AEntity.ICON_SIZE));
		mDeaths.setMaximumSize( new Dimension( Short.MAX_VALUE, AEntity.ICON_SIZE));
		addMouseListener( mHandler);
		addMouseListener( new BorderAdapter( this));
		add( mDeaths);
		add( Box.createVerticalGlue());
	}

	@Override
	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	@Override
	public FQuestTaskDeath getTask() {
		return mTask;
	}

	@Override
	public boolean onAction( Window owner) {
		return DialogIntegerField.update( mTask, owner);
	}

	@Override
	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}

	@Override
	public void update() {
		int deaths = mTask.mDeaths;
		mDeaths.setText( String.format( "You've died 0 of %d %s", deaths, deaths <= 1 ? "time" : "times"));
	}
}
