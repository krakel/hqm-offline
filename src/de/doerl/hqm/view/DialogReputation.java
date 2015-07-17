package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationReward;
import de.doerl.hqm.base.dispatch.IndexOf;
import de.doerl.hqm.base.dispatch.ReputationOfIdx;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;

class DialogReputation extends ADialogList<FReputationReward> {
	private static final long serialVersionUID = 6811579336495948455L;

	public DialogReputation( Window owner) {
		super( owner, new Renderer(), new Editor( owner), 6);
		setTheme( "edit.reputation.theme");
	}

	public static boolean update( FQuest quest, Window owner) {
		DialogReputation dlg = new DialogReputation( owner);
		dlg.createMain( new Creator( quest));
		dlg.updateMain( quest);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.updateResult( quest);
			return true;
		}
		else {
			return false;
		}
	}

	private void updateMain( FQuest quest) {
		mModel.clear();
		for (FReputationReward set : quest.mRepRewards) {
			mModel.addElement( set);
		}
		updateBtn();
	}

	private void updateResult( FQuest quest) {
		quest.mRepRewards.clear();
		for (int i = 0; i < mModel.size(); ++i) {
			FReputationReward e = mModel.get( i);
			quest.mRepRewards.add( e);
		}
	}

	private static class Creator implements ICreator<FReputationReward> {
		private FQuest mQuest;

		private Creator( FQuest quest) {
			mQuest = quest;
		}

		@Override
		public FReputationReward addElement() {
			return mQuest.createRepReward();
		}

		@Override
		public ABase getBase() {
			return mQuest;
		}
	}

	private static class Editor extends ADialogEdit<FReputationReward> {
		private static final long serialVersionUID = 7720930197206098500L;
		private JComboBox<FReputation> mRep = new JComboBox<>();
		private JTextField mValue = new TextFieldInteger();

		public Editor( Window owner) {
			super( owner);
			setTheme( "edit.item.theme");
			addAction( BTN_CANCEL, DialogResult.CANCEL);
			addAction( BTN_OK, DialogResult.APPROVE);
			addEscapeAction();
			createMain();
		}

		@Override
		public FReputationReward addElement( ICreator<FReputationReward> creator) {
			mRep.setModel( new RepModel( creator.getBase().getHqm()));
			mRep.setSelectedIndex( mRep.getModel().getSize() - 1);
			mValue.setText( "0");
			if (showDialog() == DialogResult.APPROVE) {
				FReputationReward entry = creator.addElement();
				updateResult( entry);
				return entry;
			}
			else {
				return null;
			}
		}

		private ParallelGroup addLine( GroupLayout layout, ParallelGroup leftGrp, ParallelGroup rightGrp, String descr, JComponent comp) {
			JLabel lbl = new JLabel( descr);
			leftGrp.addComponent( lbl);
			rightGrp.addComponent( comp);
			return layout.createParallelGroup( Alignment.BASELINE).addComponent( lbl).addComponent( comp);
		}

		@Override
		public FReputationReward changeElement( FReputationReward entry) {
			mRep.setModel( new RepModel( entry.getHqm()));
			mRep.setSelectedIndex( IndexOf.getMember( entry.mRep));
			mValue.setText( String.valueOf( entry.mValue));
			if (showDialog() == DialogResult.APPROVE) {
				updateResult( entry);
				return entry;
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
			SequentialGroup hori = layout.createSequentialGroup();
			SequentialGroup vert = layout.createSequentialGroup();
			ParallelGroup leftGrp = layout.createParallelGroup();
			ParallelGroup rightGrp = layout.createParallelGroup();
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Reputation", mRep));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Value", mValue));
			hori.addGroup( leftGrp);
			hori.addGroup( rightGrp);
			layout.setHorizontalGroup( hori);
			layout.setVerticalGroup( vert);
			mMain.add( box);
		}

		private void updateResult( FReputationReward entry) {
			FReputation rep = ReputationOfIdx.get( entry.getHqm(), mRep.getSelectedIndex());
			entry.mRep = rep;
			entry.mValue = Utils.parseInteger( mValue.getText(), 0);
		}

		private static final class RepModel extends DefaultComboBoxModel<FReputation> {
			private static final long serialVersionUID = 3363962404627333681L;

			private RepModel( FHqm hqm) {
				super( hqm.mReputationCat.getArr());
			}
		}
	}

	private static class Renderer extends AListCellRenderer<FReputationReward> {
		private static final long serialVersionUID = -430644712741965086L;
		private LeafLabel mName = new LeafLabel( "Unknown");
		private LeafLabel mInfo = new LeafLabel( "");

		public Renderer() {
			setLayout( new GridLayout( 1, 2, 2, 2));
			setOpaque( true);
			AEntity.setSizes( this, ADialog.FONT_NORMAL.getSize());
			add( mName);
			add( mInfo);
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends FReputationReward> list, FReputationReward value, int index, boolean isSelected, boolean cellHasFocus) {
			mName.setText( value.mRep.getName());
			mInfo.setText( String.valueOf( value.mValue));
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
