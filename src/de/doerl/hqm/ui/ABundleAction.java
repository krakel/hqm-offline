package de.doerl.hqm.ui;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import de.doerl.hqm.utils.ResourceManager;

public abstract class ABundleAction extends AbstractAction {
	public static final String DIALOGKEY = "DialogKey";
	public static final String ENABLED = "Enabled";
	public static final String EXECUTE = "Execute";
	public static final String SELECTED = "Selected";
	public static final String SHORTNAME = "ShortName";
	private static final String PAR = "->";
	private static final char SEP = ';';
	private static final long serialVersionUID = -1893382903657392840L;

	public ABundleAction( String name) {
		setValues( name);
	}

	protected void setValues( String name) {
		putValue( DIALOGKEY, name);
		String value = ResourceManager.getString( name, null);
		if (value != null && value.length() > 0 && (value.indexOf( SEP) >= 0 || value.indexOf( PAR) >= 0)) {
			int pos = 0;
			String key = null;
			String param = null;
			do {
				int next = value.indexOf( SEP, pos);
				int sel = value.indexOf( PAR, pos);
				if (next < 0) {
					if (sel < 0) {
						key = value.substring( pos).trim();
						param = "";
					}
					else {
						key = value.substring( pos, sel).trim();
						param = value.substring( sel + 2);
					}
				}
				else if (sel < 0 || sel > next) {
					key = value.substring( pos, next).trim();
					param = "";
				}
				else {
					key = value.substring( pos, sel).trim();
					param = value.substring( sel + 2, next);
				}
				if (param.length() == 0) {
					putValue( key, null);
				}
				else if (SMALL_ICON.equals( key)) {
					putValue( SMALL_ICON, ResourceManager.getIcon( param));
				}
				else if (ENABLED.equals( key)) {
					setEnabled( "true".equalsIgnoreCase( param));
					putValue( "enabled", "true".equalsIgnoreCase( param));
				}
				else if (MNEMONIC_KEY.equals( key)) {
					if (!param.startsWith( "VK_")) {
						param = "VK_" + param;
					}
					try {
						Field fld = KeyEvent.class.getField( param);
						putValue( MNEMONIC_KEY, fld.get( null));
					}
					catch (RuntimeException ex) {
					}
					catch (Exception ex) {
					}
				}
				else if (ACCELERATOR_KEY.equals( key)) {
					StringTokenizer pars = new StringTokenizer( param, "(,)");
					try {
						String mask = pars.nextToken();
						String tast = pars.nextToken();
						Field fld1 = KeyEvent.class.getField( mask);
						Field fld2 = KeyEvent.class.getField( tast);
						Integer taste = (Integer) fld2.get( null);
						Integer modi = (Integer) fld1.get( null);
						KeyStroke keyStr = KeyStroke.getKeyStroke( taste.intValue(), modi.intValue());
						putValue( ACCELERATOR_KEY, keyStr);
					}
					catch (Exception ex) {
					}
				}
				else {
					putValue( key, param);
				}
				pos = next + 1;
			}
			while (pos > 0);
		}
	}
}
