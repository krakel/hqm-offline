package de.doerl.hqm.view;

import java.awt.event.MouseEvent;

import javax.swing.event.ChangeEvent;

public class ClickEvent extends ChangeEvent {
	private static final long serialVersionUID = 5342622899778419381L;
	private MouseEvent mMouse;

	public ClickEvent( Object source, MouseEvent mouse) {
		super( source);
		mMouse = mouse;
	}

	public MouseEvent getMouse() {
		return mMouse;
	}
}
