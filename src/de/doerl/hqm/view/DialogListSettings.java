package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.base.dispatch.IndexOf;
import de.doerl.hqm.base.dispatch.MarkerOfIdx;
import de.doerl.hqm.base.dispatch.ReputationOfIdx;

public class DialogListSettings extends ADialogList<FSetting> {
	private static final long serialVersionUID = -5897552996020524162L;

	public DialogListSettings( Window owner) {
		super( owner, new Renderer(), new Editor( owner));
		setTheme( "edit.setting.theme");
	}

	public static boolean update( FQuestTaskReputationTarget task, Window owner) {
		DialogListSettings dlg = new DialogListSettings( owner);
		dlg.createMain( new Creator( task));
		dlg.updateMain( task);
		if (dlg.showDialog() == DialogResult.APPROVE) {
			dlg.updateResult( task);
			return true;
		}
		else {
			return false;
		}
	}

	private void updateMain( FQuestTaskReputationTarget task) {
		mModel.clear();
		for (FSetting set : task.mSettings) {
			mModel.addElement( set);
		}
	}

	private void updateResult( FQuestTaskReputationTarget task) {
		task.mSettings.clear();
		for (int i = 0; i < mModel.size(); ++i) {
			FSetting e = mModel.get( i);
			task.mSettings.add( e);
		}
	}

	private static class Creator implements ICreator<FSetting> {
		private FQuestTaskReputationTarget mTask;

		private Creator( FQuestTaskReputationTarget task) {
			mTask = task;
		}

		@Override
		public FSetting addElement() {
			return mTask.createSetting();
		}

		@Override
		public ABase getBase() {
			return mTask;
		}
	}

	private static class Editor extends ADialogEdit<FSetting> {
		private static final long serialVersionUID = 7720930197206098500L;
		private JComboBox<FReputation> mRep = new JComboBox<>();
		private JComboBox<FMarker> mLower = new JComboBox<>();
		private JComboBox<FMarker> mUpper = new JComboBox<>();
		private JCheckBox mInverted = new JCheckBox();

		public Editor( Window owner) {
			super( owner);
			setTheme( "edit.item.theme");
			addAction( BTN_CANCEL, DialogResult.CANCEL);
			addAction( BTN_OK, DialogResult.APPROVE);
			addEscapeAction();
			mInverted.setOpaque( false);
			Insets in = mInverted.getInsets();
			mInverted.setBorder( BorderFactory.createEmptyBorder( in.top, 0, in.bottom, in.right));
			mRep.addActionListener( new RepAction());
			createMain();
		}

		@Override
		public FSetting addElement( ICreator<FSetting> creator) {
			mRep.setModel( new RepModel( creator.getBase().getHqm()));
			mRep.setSelectedIndex( -1);
			mLower.setSelectedIndex( -1);
			mUpper.setSelectedIndex( -1);
			mInverted.setSelected( false);
			if (showDialog() == DialogResult.APPROVE) {
				FSetting entry = creator.addElement();
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
		public FSetting changeElement( FSetting entry) {
			mRep.setModel( new RepModel( entry.getHqm()));
			mRep.setSelectedIndex( IndexOf.getMember( entry.mRep));
			mLower.setSelectedIndex( IndexOf.getMarker( entry.mLower));
			mUpper.setSelectedIndex( IndexOf.getMarker( entry.mUpper));
			mInverted.setSelected( entry.mInverted);
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
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Lower", mLower));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Upper", mUpper));
			vert.addGroup( addLine( layout, leftGrp, rightGrp, "Inverted", mInverted));
			hori.addGroup( leftGrp);
			hori.addGroup( rightGrp);
			layout.setHorizontalGroup( hori);
			layout.setVerticalGroup( vert);
			mMain.add( box);
		}

		private void updateResult( FSetting entry) {
			FReputation rep = ReputationOfIdx.get( entry.getParent(), mRep.getSelectedIndex());
			entry.mRep = rep;
			entry.mUpper = MarkerOfIdx.get( rep, mUpper.getSelectedIndex());
			entry.mLower = MarkerOfIdx.get( rep, mLower.getSelectedIndex());
			entry.mInverted = mInverted.isSelected();
		}

		private final class RepAction implements ActionListener {
			@Override
			public void actionPerformed( ActionEvent evt) {
				int idx = mRep.getSelectedIndex();
				if (idx >= 0) {
					try {
						RepModel model = (RepModel) mRep.getModel();
						FReputation rep = ReputationOfIdx.get( model.mHqm, idx);
						mLower.setModel( new DefaultComboBoxModel<FMarker>( rep.mMarker));
						mUpper.setModel( new DefaultComboBoxModel<FMarker>( rep.mMarker));
						int size = rep.mMarker.size();
						if (size > 0) {
							mLower.setSelectedIndex( 0);
							mUpper.setSelectedIndex( size - 1);
						}
					}
					catch (ClassCastException ex) {
					}
				}
			}
		}

		private static final class RepModel extends DefaultComboBoxModel<FReputation> {
			private static final long serialVersionUID = 3363962404627333681L;
			FHqm mHqm;

			private RepModel( FHqm hqm) {
				super( hqm.mReputationCat.getArr());
				mHqm = hqm;
			}
		}
	}

	private static class Renderer extends JPanel implements ListCellRenderer<FSetting> {
		private static final long serialVersionUID = -430644712741965086L;
		private LeafLabel mName = new LeafLabel( "Unknown");
		private LeafLabel mInfo = new LeafLabel( "");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 1, 0, 1, 0));
			mName.setFont( AEntity.FONT_STACK);
			mInfo.setFont( AEntity.FONT_SMALL);
			mName.setAlignmentX( LEFT_ALIGNMENT);
			mInfo.setAlignmentX( LEFT_ALIGNMENT);
			add( mName);
			add( mInfo);
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends FSetting> list, FSetting value, int index, boolean isSelected, boolean cellHasFocus) {
			mName.setText( value.mRep.mName);
			mInfo.setText( String.format( "%c [%s %s]", value.mInverted ? '-' : '+', String.valueOf( value.mLower), String.valueOf( value.mUpper)));
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
