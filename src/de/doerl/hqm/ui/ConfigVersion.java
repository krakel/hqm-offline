package de.doerl.hqm.ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import de.doerl.hqm.quest.FileVersion;
import de.doerl.hqm.utils.BaseDefaults;
import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.ResourceManager;

class ConfigVersion extends AConfigControl {
	private static final long serialVersionUID = 6522254272858137830L;
	protected JComboBox<String> mCombo = new JComboBox<String>();
	protected JLabel mLabel = new JLabel();
	private String mProperty;

	public ConfigVersion() {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
//	    setBorder( BorderFactory.createTitledBorder( ""));
		setAlignmentX( LEFT_ALIGNMENT);
		setAlignmentY( TOP_ALIGNMENT);
		mProperty = BaseDefaults.FILE_VERSION;
		createControl();
		initComponent( "config.version");
	}

	@Override
	public void applyChange() {
		Object obj = mCombo.getSelectedItem();
		if (obj != null) {
			PreferenceManager.setString( mProperty, obj.toString());
		}
	}

	public void createControl() {
		Box result = Box.createHorizontalBox();
		result.setAlignmentX( Component.LEFT_ALIGNMENT);
		result.add( mLabel);
		result.add( Box.createHorizontalStrut( 10));
		result.add( mCombo);
		add( result);
	}

	public void initComponent( String lbl) {
		mLabel.setText( ResourceManager.getString( lbl));
		mCombo.setAlignmentX( Component.LEFT_ALIGNMENT);
		mCombo.setMaximumSize( new Dimension( mCombo.getMaximumSize().width, mCombo.getPreferredSize().height));
		mCombo.setEditable( false);
		FileVersion[] arr = FileVersion.values();
		for (FileVersion name : arr) {
			mCombo.addItem( name.toString());
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
