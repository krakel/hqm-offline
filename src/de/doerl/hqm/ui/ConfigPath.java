package de.doerl.hqm.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

class ConfigPath extends JPanel {
	private static final long serialVersionUID = -3966232057993858800L;
	private static final Logger LOGGER = Logger.getLogger( ConfigPath.class.getName());
	private JButton mButton = new JButton( "...");
//	private JCheckBox mCheck = AConfig.createCheckBox( null, mParent);
	private JLabel mLabel = new JLabel();
	private JTextField mText = new JTextField();
	private String mProperty;

	public ConfigPath( String prop, String lbl) {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
//	    setBorder( BorderFactory.createTitledBorder( ""));
		setAlignmentX( LEFT_ALIGNMENT);
		setAlignmentY( TOP_ALIGNMENT);
		mProperty = prop;
		createControl();
		initComponent( lbl);
	}

	public void applyChange() {
		PreferenceManager.setString( mProperty, mText.getText());
	}

	private void createControl() {
		add( mLabel);
		add( createEditPath());
	}

	private Box createEditPath() {
		Box hori = Box.createHorizontalBox();
		hori.setAlignmentX( LEFT_ALIGNMENT);
//		hori.add( mCheck);
		hori.add( mText);
		hori.add( Box.createHorizontalStrut( 10));
		hori.add( mButton);
		return hori;
	}

	private void initComponent( String lbl) {
		mLabel.setText( ResourceManager.getString( lbl));
		mText.setAlignmentX( Component.LEFT_ALIGNMENT);
		mText.setPreferredSize( new Dimension( 400, 2 * mText.getFont().getSize()));
		mText.setMaximumSize( new Dimension( Short.MAX_VALUE, 2 * mText.getFont().getSize()));
//		mText.addFocusListener( this);
//		mCheck.addActionListener( mParent);
//		mCheck.addActionListener( new ActionListener() {
//			public void actionPerformed( ActionEvent e) {
//				enableText();
//			}
//		});
		mButton.addActionListener( new PathSelectAction( mText));
	}

	public void initControl() {
		mText.setText( PreferenceManager.getString( mProperty));
//		mCheck.setSelected( PreferenceManager.getString( mProperty) != null);
	}

	private static class PathSelectAction implements ActionListener {
		private JTextField mField;

		PathSelectAction( JTextField field) {
			mField = field;
		}

		public void actionPerformed( ActionEvent event) {
			JFileChooser chooser = new JFileChooser( mField.getText());
			chooser.setLocale( Locale.getDefault());
			chooser.setControlButtonsAreShown( true);
			chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
			chooser.setMultiSelectionEnabled( false);
			if (chooser.showDialog( mField, null) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				if (file.exists()) {
					mField.setText( file.getAbsolutePath());
				}
				else {
					Utils.log( LOGGER, Level.FINER, "Can`t open path: {0}.", file.getAbsolutePath());
				}
			}
		}
	}
}
