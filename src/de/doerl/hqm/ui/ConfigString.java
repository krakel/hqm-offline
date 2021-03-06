package de.doerl.hqm.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.ResourceManager;

public class ConfigString extends AConfigControl implements ActionListener {
	private static final long serialVersionUID = 2928531296460355345L;
//	private static final Logger LOGGER = Logger.getLogger( ConfigString.class.getName());
	private JLabel mLabel = new JLabel();
	private JTextField mText = new JTextField();
	private String mProperty;

	public ConfigString( String prop, String lbl) {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
//	    setBorder( BorderFactory.createTitledBorder( ""));
		setAlignmentX( LEFT_ALIGNMENT);
		setAlignmentY( TOP_ALIGNMENT);
		mProperty = prop;
		createControl();
		initComponent( lbl);
	}

	@Override
	public void actionPerformed( ActionEvent event) {
		boolean enabled = ConfigBoolean.SELECT.equals( event.getActionCommand());
		mLabel.setEnabled( enabled);
		mText.setEnabled( enabled);
	}

	@Override
	public void applyChange() {
		PreferenceManager.setString( mProperty, mText.getText());
	}

	public void createControl() {
		Box hori = Box.createHorizontalBox();
		hori.setAlignmentX( Component.LEFT_ALIGNMENT);
		hori.add( mLabel);
		hori.add( Box.createHorizontalStrut( 10));
		hori.add( mText);
		add( hori);
	}

	private void initComponent( String lbl) {
		mLabel.setText( ResourceManager.getString( lbl));
		mText.setAlignmentX( Component.LEFT_ALIGNMENT);
		mText.setPreferredSize( new Dimension( 100, 2 * mText.getFont().getSize()));
		mText.setMaximumSize( new Dimension( Short.MAX_VALUE, 2 * mText.getFont().getSize()));
	}

	@Override
	public void initControl() {
		mText.setText( PreferenceManager.getString( mProperty));
	}
}
