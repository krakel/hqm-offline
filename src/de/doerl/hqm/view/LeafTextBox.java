package de.doerl.hqm.view;

import javax.swing.JTextArea;

import de.doerl.hqm.view.ClickHandler.ClickListener;

class LeafTextBox extends JTextArea {
	private static final long serialVersionUID = 535359109100667359L;
	private ClickHandler mHandler = new ClickHandler( this);

	public LeafTextBox() {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		setFont( AEntity.FONT_NORMAL);
		setLineWrap( true);
		setWrapStyleWord( true);
		setEditable( false);
		addMouseListener( mHandler);
	}

	public ClickListener getHandler() {
		return mHandler.getListener();
	}
}
