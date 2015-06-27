package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.quest.BagTier;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.ADialogList.ICreator;

class DialogListWeights extends ADialog {
	private static final long serialVersionUID = 7903951948404166751L;
	protected DefaultListModel<Integer> mModel = new DefaultListModel<>();
	private JList<Integer> mList;
	private JButton mBtnChange = new JButton( "Change");
	private JButton mBtnUp = new JButton( "Up");
	private JButton mBtnDown = new JButton( "Down");
	private ListCellRenderer<Integer> mRenderer = new Renderer();
	private ADialogEdit<Integer> mEdit;

	public DialogListWeights( Window owner) {
		super( owner);
		mEdit = new Editor( owner);
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
		updateSize( mBtnChange);
		updateSize( mBtnUp);
		updateSize( mBtnDown);
		setTheme( "edit.weight.theme");
	}

	public static boolean update( FGroupTier tier, Window owner) {
		DialogListWeights dlg = new DialogListWeights( owner);
		dlg.createMain();
		dlg.updateMain( tier);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.updateResult( tier);
			return true;
		}
		else {
			return false;
		}
	}

	private Box createEdit() {
		Box result = Box.createVerticalBox();
		result.setAlignmentY( TOP_ALIGNMENT);
		result.setMaximumSize( new Dimension( 50, Short.MAX_VALUE));
		result.add( mBtnChange);
		result.add( Box.createVerticalStrut( 20));
		result.add( mBtnUp);
		result.add( mBtnDown);
		result.add( Box.createVerticalGlue());
		return result;
	}

	@Override
	protected final void createMain() {
		mMain.setPreferredSize( new Dimension( 450, 200));
		mList = new JList<Integer>( mModel);
		mList.setAlignmentY( TOP_ALIGNMENT);
		mList.setCellRenderer( mRenderer);
		mList.setVisibleRowCount( 5);
		JScrollPane scroll = new JScrollPane( mList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));
		mMain.add( scroll);
		mMain.add( Box.createHorizontalStrut( GAP));
		mMain.add( createEdit());
		ClickHandler dbl = new ClickHandler();
		mList.addMouseListener( new ButtonUpdateHandler());
		mList.addMouseListener( dbl);
		mBtnUp.addActionListener( new EntryUpHandler());
		mBtnDown.addActionListener( new EntryDownHandler());
		ActionListener edit = new EntryChangeHandler();
		mBtnChange.addActionListener( edit);
		dbl.addClickListener( edit);
	}

	protected void updateBtn() {
		int idx = mList.getSelectedIndex() + 1;
		int size = mModel.getSize();
		mBtnChange.setEnabled( idx > 0);
		mBtnUp.setEnabled( idx > 1);
		mBtnDown.setEnabled( idx > 0 && idx < size);
	};

	private void updateMain( FGroupTier tier) {
		mModel.clear();
		for (int i = 0; i < tier.mWeights.length; ++i) {
			mModel.addElement( tier.mWeights[i]);
		}
		updateBtn();
	}

	private void updateResult( FGroupTier tier) {
		tier.mWeights = BagTier.newArray();
		for (int i = 0; i < tier.mWeights.length; ++i) {
			tier.mWeights[i] = mModel.get( i);
		}
	}

	private void updateSize( JComponent comp) {
		comp.setMaximumSize( new Dimension( Short.MAX_VALUE, comp.getMinimumSize().height));
	}

	private final class ButtonUpdateHandler extends MouseAdapter {
		@Override
		public void mouseClicked( MouseEvent evt) {
			updateBtn();
		}
	}

	private static class Editor extends ADialogEdit<Integer> {
		private static final long serialVersionUID = 7720930197206098500L;
		private JTextField mIconDmg = new TextFieldInteger();

		public Editor( Window owner) {
			super( owner);
			setTheme( "edit.item.theme");
			addAction( BTN_CANCEL, DialogResult.CANCEL);
			addAction( BTN_OK, DialogResult.APPROVE);
			addEscapeAction();
			createMain();
		}

		@Override
		public Integer addElement( ICreator<Integer> creator) {
			mIconDmg.setText( "0");
			if (showDialog() == DialogResult.APPROVE) {
				return updateResult();
			}
			else {
				return null;
			}
		}

		@Override
		public Integer changeElement( Integer entry) {
			mIconDmg.setText( String.valueOf( entry));
			if (showDialog() == DialogResult.APPROVE) {
				return updateResult();
			}
			else {
				return null;
			}
		}

		@Override
		protected void createMain() {
			JPanel box = new JPanel();
			GroupLayout layout = new GroupLayout( box);
			box.setLayout( layout);
			box.setOpaque( false);
//			box.setBorder( BorderFactory.createLineBorder( Color.RED));
			layout.setAutoCreateGaps( true);
			Group hori = layout.createSequentialGroup();
			Group vert = layout.createSequentialGroup();
			Group leftGrp = layout.createParallelGroup();
			Group rightGrp = layout.createParallelGroup();
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Weight", mIconDmg));
			hori.addGroup( leftGrp);
			hori.addGroup( rightGrp);
			layout.setHorizontalGroup( hori);
			layout.setVerticalGroup( vert);
			mMain.add( box);
		}

		private Integer updateResult() {
			return Utils.parseInteger( mIconDmg.getText(), 0);
		}
	}

	private final class EntryChangeHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent evt) {
			int idx = mList.getSelectedIndex();
			if (idx >= 0) {
				Integer old = mModel.getElementAt( idx);
				Integer entry = mEdit.changeElement( old);
				if (entry != null) {
					mModel.setElementAt( entry, idx);
				}
			}
		}
	}

	private final class EntryDownHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent e) {
			int idx = mList.getSelectedIndex() + 1;
			if (idx > 0 && idx < mModel.getSize()) {
				Integer old = mModel.remove( idx - 1);
				mModel.insertElementAt( old, idx);
				mList.setSelectedIndex( idx);
				updateBtn();
			}
		}
	}

	private final class EntryUpHandler implements ActionListener {
		@Override
		public void actionPerformed( ActionEvent e) {
			int idx = mList.getSelectedIndex();
			if (idx > 0) {
				Integer old = mModel.remove( idx);
				mModel.insertElementAt( old, idx - 1);
				mList.setSelectedIndex( idx - 1);
				updateBtn();
			}
		}
	}

	private static class Renderer extends AListCellRenderer<Integer> {
		private static final long serialVersionUID = -430644712741965086L;
		private LeafLabel mBag = new LeafLabel( "");
		private LeafLabel mName = new LeafLabel( "");

		public Renderer() {
			setLayout( new GridLayout( 1, 2, 2, 2));
			setOpaque( false);
			AEntity.setSizes( this, ADialog.FONT_NORMAL.getSize());
			add( mBag);
			add( mName);
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends Integer> list, Integer value, int index, boolean isSelected, boolean cellHasFocus) {
			mBag.setText( BagTier.get( index).toString());
			mName.setText( String.format( "%6d", value));
			if (isSelected) {
				setBackground( list.getSelectionBackground());
			}
			else {
				setBackground( list.getBackground());
			}
			return this;
		}
	}
}
