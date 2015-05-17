package de.doerl.hqm.view;

import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;

import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.utils.ResourceManager;

class HQMEntity extends AEntity<FHqm> {
	private static final long serialVersionUID = 4033642877403597083L;
	private static final BufferedImage FRONT = ResourceManager.getImage( "front.png").getSubimage( 0, 0, 280, 360);
	private FHqm mBase;
	private JPanel mLeafLeft = leafPanel( true);
	private JPanel mLeafRight = leafPanel( false);
	private JLabel mLogo = leafImage( 280, 360, FRONT);
	private JTextArea mDesc = leafTextArea();
	private JToolBar mTools = createToolBar();

	public HQMEntity( EditView view, FHqm base) {
		super( view, new GridLayout( 1, 2));
		mBase = base;
		createLeft( mLeafLeft);
		createRight( mLeafRight);
		add( mLeafLeft);
		add( mLeafRight);
		mDesc.setText( base.mDesc.mValue);
		mDesc.addFocusListener( new FocusListener() {
			@Override
			public void focusGained( FocusEvent e) {
				mDesc.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));
			}

			@Override
			public void focusLost( FocusEvent e) {
				mDesc.setBorder( null);
			}
		});
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
		tool.add( new JButton( ResourceManager.getIcon( "close.gif")));
		return tool;
	}

	@Override
	public FHqm getBase() {
		return mBase;
	}

	@Override
	protected JComponent getLeftTool() {
		return null;
	}

	@Override
	protected JComponent getRightTool() {
		return mTools;
	}
}
