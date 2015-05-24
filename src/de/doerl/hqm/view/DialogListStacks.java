package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FParameterStack;
import de.doerl.hqm.ui.ADialog;

public class DialogListStacks extends ADialog {
	private static final long serialVersionUID = 7121230392342882985L;
	private DefaultListModel<AStack> mModel = new DefaultListModel<>();
	private JList<AStack> mList;
	private JTextField mField = new JTextField();
	private JButton mBtnDelete = new JButton( "Delete");
	private JButton mBtnChange = new JButton( "Change");
	private JButton mBtnAdd = new JButton( "Add");

	public DialogListStacks( Window owner) {
		super( owner);
		setThema( "edit.stack.thema");
		addAction( BTN_CANCEL, DialogResult.CANCEL);
		addAction( BTN_OK, DialogResult.APPROVE);
		addEscapeAction();
	}

	public static Vector<AStack> update( Vector<FParameterStack> value, Window owner) {
		DialogListStacks dlg = new DialogListStacks( owner);
		dlg.createMain();
		dlg.updateMain( value);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			return dlg.getResult();
		}
		else {
			return null;
		}
	}

	private Box createButtons() {
		updateBtn( false);
		Box result = Box.createHorizontalBox();
		result.setAlignmentX( LEFT_ALIGNMENT);
		result.add( mBtnDelete);
		result.add( Box.createHorizontalGlue());
		result.add( mBtnChange);
		result.add( Box.createHorizontalGlue());
		result.add( mBtnAdd);
		return result;
	}

	private Box createEdit() {
		Box result = Box.createVerticalBox();
		result.setAlignmentY( TOP_ALIGNMENT);
		result.setPreferredSize( new Dimension( 230, 100));
		mField.setFont( AEntity.FONT_NORMAL);
		mField.setPreferredSize( new Dimension( 200, 2 * getFont().getSize()));
		mField.setMaximumSize( new Dimension( Short.MAX_VALUE, 2 * getFont().getSize()));
		mField.setAlignmentX( LEFT_ALIGNMENT);
		result.add( mField);
		result.add( Box.createVerticalStrut( GAP));
		result.add( createButtons());
		result.add( Box.createVerticalGlue());
		return result;
	}

	@Override
	protected void createMain() {
		mList = new JList<AStack>( mModel);
		mList.setAlignmentY( TOP_ALIGNMENT);
		mList.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));
		mList.setCellRenderer( new Renderer());
		mList.setPreferredSize( new Dimension( 200, 200));
		mList.setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		mMain.add( mList);
		mMain.add( Box.createHorizontalStrut( GAP));
		mMain.add( createEdit());
		mList.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent evt) {
				AStack stk = mList.getSelectedValue();
				if (stk != null) {
					mField.setText( stk.getName());
					updateBtn( true);
				}
			}
		});
		mBtnDelete.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent evt) {
				AStack stk = mList.getSelectedValue();
				if (stk != null) {
					mModel.removeElement( stk);
					updateBtn( false);
				}
			}
		});
		mBtnChange.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e) {
				AStack stk = mList.getSelectedValue();
				String text = mField.getText();
				if (stk != null && text != null && !text.equals( "")) {
					int idx = mList.getSelectedIndex();
					mModel.setElementAt( new FItemStack( null, text, 1, 0), idx);
				}
			}
		});
		mBtnAdd.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e) {
				String text = mField.getText();
				if (text != null && !text.equals( "")) {
					mModel.addElement( new FItemStack( null, text, 1, 0));
				}
			}
		});
	}

	private Vector<AStack> getResult() {
		Vector<AStack> result = new Vector<>();
		for (int i = 0; i < mModel.size(); ++i) {
			result.add( mModel.get( i));
		}
		return result;
	}

	private void updateBtn( boolean enabled) {
		mBtnDelete.setEnabled( enabled);
		mBtnChange.setEnabled( enabled);
	}

	private void updateMain( Vector<FParameterStack> value) {
		mModel.clear();
		for (FParameterStack stk : value) {
			mModel.addElement( stk.mValue);
		}
	}

	private final class Renderer extends JPanel implements ListCellRenderer<AStack> {
		private static final long serialVersionUID = 5239073494468176719L;
		private LeafIcon mIcon = new LeafIcon( null);
		private LeafLabel mName = new LeafLabel( "Unknown");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 1, 0, 1, 0));
			mName.setAlignmentY( TOP_ALIGNMENT);
			add( mIcon);
			add( Box.createHorizontalStrut( 5));
			add( mName);
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends AStack> list, AStack value, int index, boolean isSelected, boolean cellHasFocus) {
			mIcon.setIcon( value);
			mName.setText( value.getName());
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
