package de.doerl.hqm.view;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.doerl.hqm.base.FParameterString;
import de.doerl.hqm.utils.ResourceManager;

class LeafTextBox extends JTextArea implements FocusListener, DocumentListener {
	private static final long serialVersionUID = 535359109100667359L;
	private JButton mBtnCancel = AEntity.createToolButton( null);
	private JButton mBtnUndo = AEntity.createToolButton( null);
	private JButton mBtnOk = AEntity.createToolButton( null);
	private JToolBar mTools;
	private String mOldText;
	private FParameterString mDesc;

	public LeafTextBox() {
		setLineWrap( true);
		setWrapStyleWord( true);
		setAlignmentX( LEFT_ALIGNMENT);
		setOpaque( false);
		setBorder( null);
//		setBorder( BorderFactory.createLineBorder( Color.LIGHT_GRAY));
		setFont( AEntity.FONT_NORMAL);
		mTools = createToolBar();
		addFocusListener( this);
		getDocument().addDocumentListener( this);
		mBtnCancel.setAction( new ABtnAction( "cancel.gif") {
			private static final long serialVersionUID = 4342077619245237347L;

			@Override
			public void actionPerformed( ActionEvent e) {
				if (mDesc != null) {
					mDesc.mValue = mOldText;
				}
				clearTextFocus();
			}
		});
		mBtnUndo.setAction( new ABtnAction( "undo.gif") {
			private static final long serialVersionUID = 4342077619245237347L;

			@Override
			public void actionPerformed( ActionEvent e) {
				setText( mOldText);
				setBtnsEnabled( false);
			}
		});
		mBtnOk.setAction( new ABtnAction( "ok.gif") {
			private static final long serialVersionUID = 1278703013229143730L;

			@Override
			public void actionPerformed( ActionEvent e) {
				if (mDesc != null) {
					mDesc.mValue = getText();
				}
				clearTextFocus();
			}
		});
	}

	@Override
	public void changedUpdate( DocumentEvent e) {
		setBtnsEnabled( true);
	}

	private void clearTextFocus() {
		setFocusable( false);
		setBorder( null);
		setBtnsEnabled( false);
		setFocusable( true);
	}

	void connectTo( FParameterString desc) {
		mDesc = desc;
		setBtnsEnabled( false);
		if (desc != null) {
			mOldText = desc.mValue;
			setText( mOldText);
			mTools.setVisible( true);
		}
		else {
			setText( "unknown description");
			mTools.setVisible( false);
		}
	}

	private JToolBar createToolBar() {
		JToolBar tool = new JToolBar();
		tool.setFloatable( false);
		tool.add( mBtnCancel);
		tool.add( mBtnUndo);
		tool.add( mBtnOk);
		return tool;
	}

	@Override
	public void focusGained( FocusEvent e) {
		mOldText = getText();
		setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));
		mBtnOk.setEnabled( true);
	}

	@Override
	public void focusLost( FocusEvent e) {
	}

	public JToolBar getToolBar() {
		return mTools;
	}

	@Override
	public void insertUpdate( DocumentEvent e) {
		setBtnsEnabled( true);
	}

	@Override
	public void removeUpdate( DocumentEvent e) {
		setBtnsEnabled( true);
	}

	private void setBtnsEnabled( boolean value) {
		mBtnCancel.setEnabled( value);
		mBtnUndo.setEnabled( value);
		mBtnOk.setEnabled( value);
	}

	private abstract class ABtnAction extends AbstractAction {
		private static final long serialVersionUID = 4594454317850021607L;

		public ABtnAction( String icon) {
			super( null, ResourceManager.getIcon( icon));
			setEnabled( false);
//			putValue( "enabled", Boolean.FALSE);
		}
	}
}
