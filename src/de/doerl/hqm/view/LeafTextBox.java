package de.doerl.hqm.view;

import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import de.doerl.hqm.view.ClickHandler.ClickListener;

class LeafTextBox extends JTextArea implements IClickListener {
	private static final long serialVersionUID = 535359109100667359L;
	private static final Border BORDER = BorderFactory.createBevelBorder( BevelBorder.RAISED);
	private ClickHandler mHandler = new ClickHandler();
	private Border mOldBorder;

	public LeafTextBox() {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		setFont( AEntity.FONT_NORMAL);
		setLineWrap( true);
		setWrapStyleWord( true);
		setEditable( false);
		addMouseListener( mHandler);
		mHandler.getListener().addClickListener( this);
	}

	public ClickListener getHandler() {
		return mHandler.getListener();
	}

	@Override
	public void onDoubleClick( MouseEvent evt) {
	}

	@Override
	public void onEnterClick( MouseEvent evt) {
		mOldBorder = getBorder();
		setBorder( BORDER);
	}

	@Override
	public void onExitClick( MouseEvent evt) {
		setBorder( mOldBorder);
		mOldBorder = null;
	}

	@Override
	public void onSingleClick( MouseEvent evt) {
	}
}
