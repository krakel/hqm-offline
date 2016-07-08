package de.doerl.hqm.view.leafs;

import java.awt.event.ActionListener;

import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.ui.ADialog.TextAreaAscii;
import de.doerl.hqm.view.BorderAdapter;
import de.doerl.hqm.view.ClickHandler;

public class LeafTextBox extends TextAreaAscii {
	private static final long serialVersionUID = 535359109100667359L;
	private ClickHandler mHandler = new ClickHandler();

	public LeafTextBox() {
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
		setFont( ADialog.FONT_NORMAL);
		setLineWrap( true);
		setWrapStyleWord( true);
		setEditable( false);
		addMouseListener( mHandler);
		addMouseListener( new BorderAdapter( this));
	}

	public void addClickListener( ActionListener l) {
		mHandler.addClickListener( l);
	}

	public void removeClickListener( ActionListener l) {
		mHandler.removeClickListener( l);
	}
}
