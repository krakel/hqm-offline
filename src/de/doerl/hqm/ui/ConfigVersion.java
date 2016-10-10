package de.doerl.hqm.ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.ResourceManager;

class ConfigVersion extends AConfigControl {
	private static final long serialVersionUID = 6522254272858137830L;
	private JLabel mLabel = new JLabel();
	private JComboBox<String> mCombo = new JComboBox<String>();
	private String mProperty;

	public ConfigVersion( String prop, String lbl) {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
//	    setBorder( BorderFactory.createTitledBorder( ""));
		setAlignmentX( LEFT_ALIGNMENT);
		setAlignmentY( TOP_ALIGNMENT);
		mProperty = prop;
		createControl();
		initComponent( lbl);
	}

	@Override
	public void applyChange() {
		Object obj = mCombo.getSelectedItem();
		if (obj != null) {
			PreferenceManager.setString( mProperty, obj.toString());
		}
	}

	public void createControl() {
		Box hori = Box.createHorizontalBox();
		hori.setAlignmentX( Component.LEFT_ALIGNMENT);
		hori.add( mLabel);
		hori.add( Box.createHorizontalStrut( 10));
		hori.add( mCombo);
		add( hori);
	}

	public void initComponent( String lbl) {
		mLabel.setText( ResourceManager.getString( lbl));
		mCombo.setAlignmentX( Component.LEFT_ALIGNMENT);
		mCombo.setMaximumSize( new Dimension( mCombo.getMaximumSize().width, mCombo.getPreferredSize().height));
		mCombo.setEditable( false);
		FileVersion[] arr = FileVersion.values();
		for (FileVersion vers : arr) {
			mCombo.addItem( vers.toString());
		}
	}

	@Override
	public void initControl() {
		String val = PreferenceManager.getString( mProperty);
		if (val != null) {
			mCombo.setSelectedItem( val);
		}
		else {
			mCombo.setSelectedItem( FileVersion.last().toString());
		}
	}
}
