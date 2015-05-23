package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;

import de.doerl.hqm.base.AStack;
import de.doerl.hqm.base.FParameterStack;
import de.doerl.hqm.ui.ADialog;

public class DialogListStacks extends ADialog {
	private static final long serialVersionUID = 7121230392342882985L;
	private DefaultListModel<AStack> mModel = new DefaultListModel<AStack>();
	private JList<AStack> mList;

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

	@Override
	protected void createMain() {
		mList = new JList<AStack>( mModel);
		mList.setAlignmentY( TOP_ALIGNMENT);
		mList.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED));
		mList.setCellRenderer( new Renderer());
		mList.setPreferredSize( new Dimension( 400, 200));
		mList.setMaximumSize( new Dimension( Short.MAX_VALUE, Short.MAX_VALUE));
		mMain.add( mList);
	}

	private Vector<AStack> getResult() {
		Vector<AStack> result = new Vector<AStack>();
		for (int i = 0; i < mModel.size(); ++i) {
			result.add( mModel.get( i));
		}
		return result;
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
