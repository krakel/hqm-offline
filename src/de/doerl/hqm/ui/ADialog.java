package de.doerl.hqm.ui;

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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
		mSelect.setAlignmentX( LEFT_ALIGNMENT);
		mSelect.setPreferredSize( new Dimension( 300, 32));
		mSelect.setMaximumSize( new Dimension( Short.MAX_VALUE, 32));
		mSelect.add( Box.createHorizontalGlue());
		content.add( mMain);
		content.add( Box.createVerticalGlue());
		content.add( Box.createVerticalStrut( GAP));
		content.add( new SinkLine());
		content.add( mSelect);
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

	protected Group addLine( GroupLayout layout, Group left, Group right, String descr, JComponent comp) {
		JLabel lbl = new JLabel( descr);
		left.addComponent( lbl);
		right.addComponent( comp);
		return layout.createParallelGroup( Alignment.BASELINE).addComponent( lbl).addComponent( comp);
	}

	protected abstract void createMain();

	public void setTheme( String theme) {
		if (theme != null) {
			setTitle( ResourceManager.getString( theme));
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

	protected static class SingleLine extends JPanel {
		private static final long serialVersionUID = -2148404323729982876L;
		private static final Dimension MAX_SIZE = new Dimension( Integer.MAX_VALUE, 1);
		private static final Dimension PREF_SIZE = new Dimension( 0, 1);

		public SingleLine() {
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

	protected static class SinkLine extends JPanel {
		private static final long serialVersionUID = -2143279745769033698L;
		private static final Dimension MAX_SIZE = new Dimension( Integer.MAX_VALUE, 2);
		private static final Dimension PREF_SIZE = new Dimension( 0, 2);

		public SinkLine() {
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

	public static class TextAreaAscii extends JTextArea {
		private static final long serialVersionUID = 124973359051465828L;

		public TextAreaAscii() {
			addKeyListener( new KeyAdaptorASCII());
		}
	}

	public static class TextFieldAscii extends JTextField {
		private static final long serialVersionUID = 124973359051465828L;

		public TextFieldAscii() {
			addKeyListener( new KeyAdaptorASCII());
		}
	}

	public static class TextFieldInteger extends JTextField {
		private static final long serialVersionUID = 124973359051465828L;

		public TextFieldInteger() {
			addKeyListener( new KeyAdaptorInterger());
		}
	}
}
