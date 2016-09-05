package de.doerl.hqm.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.event.EventListenerList;

import de.doerl.hqm.utils.PreferenceManager;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;

class ConfigBoolean extends AConfigControl implements ActionListener {
	private static final long serialVersionUID = 3030343336685063263L;
	private static final Logger LOGGER = Logger.getLogger( ConfigBoolean.class.getName());
	private JCheckBox mCheck;
	private String mProperty;
	private EventListenerList mListener = new EventListenerList();

	public ConfigBoolean( String prop, String lbl) {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
		setAlignmentX( LEFT_ALIGNMENT);
		setAlignmentY( TOP_ALIGNMENT);
		mProperty = prop;
		mCheck = new JCheckBox( ResourceManager.getString( lbl));
		mCheck.setSelected( false);
		add( mCheck);
//		mCheck.addActionListener( mParent);
		mCheck.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent event) {
				fireAction();
			}
		});
	}

	@Override
	public void actionPerformed( ActionEvent e) {
	}

	@Override
	public void applyChange() {
		PreferenceManager.setBool( mProperty, mCheck.isSelected());
	}

	protected void fireAction() {
		String cmd = mCheck.isSelected() ? "select" : "unselect";
		Object[] listeners = mListener.getListenerList();
		ActionEvent event = new ActionEvent( this, 0, cmd);
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == ActionListener.class) {
				try {
					((ActionListener) listeners[i + 1]).actionPerformed( event);
				}
				catch (ClassCastException ex) {
					Utils.logThrows( LOGGER, Level.WARNING, ex);
				}
			}
		}
	}

	@Override
	public void initControl() {
		mCheck.setSelected( PreferenceManager.getBool( mProperty));
	}

	public void registerChangeListener( ActionListener l) {
		mListener.add( ActionListener.class, new WeakActionListener( mListener, l));
	}
}
