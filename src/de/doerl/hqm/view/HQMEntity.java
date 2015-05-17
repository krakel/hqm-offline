package de.doerl.hqm.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.utils.ResourceManager;

class HQMEntity extends AEntity<FHqm> {
	private static final long serialVersionUID = 4033642877403597083L;
	private static final BufferedImage FRONT = ResourceManager.getImage( "front.png").getSubimage( 0, 0, 280, 360);
	private FHqm mHQM;
	private JPanel mLeafLeft = leafPanel( true);
	private JPanel mLeafRight = leafPanel( false);
	private JLabel mLogo = leafImage( 280, 360, FRONT);
	private JTextArea mDesc = leafTextArea();
	private JButton mBtnCancel = createToolButton( ResourceManager.getIcon( "cancel.gif"));
	private JButton mBtnUndo = createToolButton( ResourceManager.getIcon( "undo.gif"));
	private JButton mBtnOk = createToolButton( ResourceManager.getIcon( "ok.gif"));
	private JToolBar mTools;
	private String mOldText;

	public HQMEntity( EditView view, FHqm hqm) {
		super( view, new GridLayout( 1, 2));
		mHQM = hqm;
		mTools = createToolBar();
		createLeft( mLeafLeft);
		createRight( mLeafRight);
		add( mLeafLeft);
		add( mLeafRight);
		mOldText = hqm.mDesc.mValue;
		mDesc.setText( mOldText);
		mDesc.addFocusListener( new FocusListener() {
			@Override
			public void focusGained( FocusEvent e) {
				mOldText = mDesc.getText();
				mDesc.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));
				mBtnOk.setEnabled( true);
			}

			@Override
			public void focusLost( FocusEvent e) {
			}
		});
		mBtnCancel.setAction( new AbstractAction( null, ResourceManager.getIcon( "cancel.gif")) {
			private static final long serialVersionUID = 4342077619245237347L;

			@Override
			public void actionPerformed( ActionEvent e) {
				mHQM.mDesc.mValue = mOldText;
				clearTextFocus();
			}
		});
		mBtnUndo.setAction( new AbstractAction( null, ResourceManager.getIcon( "undo.gif")) {
			private static final long serialVersionUID = 4342077619245237347L;

			@Override
			public void actionPerformed( ActionEvent e) {
				mDesc.setText( mOldText);
				setEnableBtns( false);
			}
		});
		mBtnOk.setAction( new AbstractAction( null, ResourceManager.getIcon( "ok.gif")) {
			private static final long serialVersionUID = 1278703013229143730L;

			@Override
			public void actionPerformed( ActionEvent e) {
				mHQM.mDesc.mValue = mDesc.getText();
				clearTextFocus();
			}
		});
		mDesc.getDocument().addDocumentListener( new DocumentListener() {
			@Override
			public void changedUpdate( DocumentEvent e) {
				setEnableBtns( true);
			}

			@Override
			public void insertUpdate( DocumentEvent e) {
				setEnableBtns( true);
			}

			@Override
			public void removeUpdate( DocumentEvent e) {
				setEnableBtns( true);
			}
		});
	}

	private void clearTextFocus() {
		mDesc.setFocusable( false);
		mDesc.setBorder( null);
		setEnableBtns( false);
		mDesc.setFocusable( true);
	}

	private void createLeft( JPanel leaf) {
		leaf.add( mLogo);
		leaf.add( Box.createVerticalGlue());
	}

	private void createRight( JPanel leaf) {
		leaf.add( leafScoll( mDesc, 160));
//		leaf.add( Box.createVerticalGlue());
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
	public FHqm getBase() {
		return mHQM;
	}

	@Override
	protected JComponent getLeftTool() {
		return null;
	}

	@Override
	protected JComponent getRightTool() {
		setEnableBtns( false);
		return mTools;
	}

	private void setEnableBtns( boolean value) {
		mBtnCancel.setEnabled( value);
		mBtnUndo.setEnabled( value);
		mBtnOk.setEnabled( value);
	}
}
