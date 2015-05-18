package de.doerl.hqm.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import de.doerl.hqm.utils.ResourceManager;

public abstract class ADialog extends JDialog {
	private static final long serialVersionUID = -8555679578310005324L;
	public static final String BTN_OK = "btn.ok";
	public static final String BTN_CANCEL = "btn.cancel";
	public static final String BTN_YES = "btn.yes";
	public static final String BTN_NO = "btn.no";
	public static final String BTN_NEW = "btn.new";
	public static final String BTN_EDIT = "btn.edit";
	public static final String BTN_DELETE = "btn.delete";
	public static final int GAP = 10;
	protected final Action ESC_ACTION = new DefaultAction( "esc pressed", DialogResult.CANCEL);
	protected DialogResult mReturnValue = DialogResult.CANCEL;
	protected Box mMain = Box.createHorizontalBox();
	protected Box mSelect = Box.createHorizontalBox();

	public ADialog( Window owner) {
		this( owner, ModalityType.DOCUMENT_MODAL);
	}

	public ADialog( Window owner, ModalityType type) {
		super( owner, type);
		rootPane.setOpaque( true);
		rootPane.setBackground( UIManager.getColor( "Panel.background"));
		rootPane.setBorder( BorderFactory.createEmptyBorder( GAP, GAP, GAP, GAP));
		Container content = rootPane.getContentPane();
		content.setLayout( new BoxLayout( content, BoxLayout.Y_AXIS));
		mMain.setAlignmentX( LEFT_ALIGNMENT);
		createSelect();
		createDialog( content);
	}

	protected static void addComponent( Group hori, Group verti, Component c) {
		hori.addComponent( c);
		verti.addComponent( c);
	}

	public static void addKeyAction( JComponent comp, int cond, String key, Action a) {
		Object o = new Object();
		comp.getInputMap( cond).put( KeyStroke.getKeyStroke( key), o);
		comp.getActionMap().put( o, a);
	}

	public static Window getParentFrame( Container source) {
		if (source == null) {
			return null;
		}
		if (source instanceof Frame) {
			return (Window) source;
		}
		if (source instanceof Dialog) {
			return (Window) source;
		}
		return getParentFrame( source.getParent());
	}

	public void addAction( ABundleAction action) {
		mSelect.add( Box.createHorizontalStrut( GAP));
		mSelect.add( new JButton( action));
	}

	public void addAction( String name, DialogResult value) {
		addAction( new DefaultAction( name, value));
	}

	protected void addEscapeAction() {
		addKeyAction( getRootPane(), JComponent.WHEN_IN_FOCUSED_WINDOW, "ESCAPE", ESC_ACTION);
	}

	private void createDialog( Container content) {
		JPanel line = new JSinkLine();
		content.add( mMain);
		content.add( Box.createVerticalStrut( GAP));
		content.add( line);
		content.add( mSelect);
	}

	protected abstract void createMain();

	private void createSelect() {
		mSelect.setAlignmentX( LEFT_ALIGNMENT);
		mSelect.add( Box.createHorizontalGlue());
	}

	public void setThema( String thema) {
		if (thema != null) {
			setTitle( ResourceManager.getString( thema));
		}
	}

	public DialogResult showDialog() {
		pack();
		setLocationRelativeTo( getParent());
		setVisible( true);
		return mReturnValue;
	}

	protected class DefaultAction extends ABundleAction {
		private static final long serialVersionUID = 4540418976827421589L;
		private DialogResult mValue;

		public DefaultAction( String name, DialogResult value) {
			super( name);
			mValue = value;
		}

		public void actionPerformed( ActionEvent event) {
			mReturnValue = mValue;
			setVisible( false);
		}
	}

	public static enum DialogResult {
		APPROVE,
		CANCEL,
		ERROR,
		NO,
		SAVE;
	}

	protected static class JSingleLine extends JPanel {
		private static final long serialVersionUID = -2148404323729982876L;
		private static final Dimension MAX_SIZE = new Dimension( Integer.MAX_VALUE, 1);
		private static final Dimension PREF_SIZE = new Dimension( 0, 1);

		public JSingleLine() {
			setAlignmentX( LEFT_ALIGNMENT);
		}

		@Override
		public Dimension getMaximumSize() {
			return MAX_SIZE;
		}

		@Override
		public Dimension getPreferredSize() {
			return PREF_SIZE;
		}

		@Override
		protected void paintComponent( Graphics g) {
			super.paintComponent( g);
			g.setColor( getBackground().darker());
			g.drawLine( 0, 0, getWidth(), 0);
		}
	}

	protected static class JSinkLine extends JPanel {
		private static final long serialVersionUID = -2143279745769033698L;
		private static final Dimension MAX_SIZE = new Dimension( Integer.MAX_VALUE, 2);
		private static final Dimension PREF_SIZE = new Dimension( 0, 2);

		public JSinkLine() {
			setAlignmentX( LEFT_ALIGNMENT);
		}

		@Override
		public Dimension getMaximumSize() {
			return MAX_SIZE;
		}

		@Override
		public Dimension getPreferredSize() {
			return PREF_SIZE;
		}

		@Override
		protected void paintComponent( Graphics g) {
			super.paintComponent( g);
			g.setColor( getBackground().darker());
			g.drawLine( 0, 0, getWidth(), 0);
			g.setColor( getBackground().brighter());
			g.drawLine( 0, 1, getWidth(), 1);
		}
	}
}
