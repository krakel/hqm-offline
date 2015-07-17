package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;

import de.doerl.hqm.base.FQuestTaskReputationTarget;
import de.doerl.hqm.base.FSetting;
import de.doerl.hqm.base.dispatch.AHQMWorker;

class TaskBoxReputationTarget extends ATaskBox {
	private static final long serialVersionUID = -7259046301859360457L;
	private FQuestTaskReputationTarget mTask;
	private LeafList<FSetting> mList = new LeafList<>();

	public TaskBoxReputationTarget( FQuestTaskReputationTarget task) {
		mTask = task;
		mList.setCellRenderer( new Renderer());
		mList.addMouseListener( new BorderAdapter( mList));
		add( AEntity.leafScoll( mList, HEIGHT));
	}

	@Override
	public void addClickListener( ActionListener l) {
		mList.addClickListener( l);
	}

	@Override
	public FQuestTaskReputationTarget getTask() {
		return mTask;
	}

	@Override
	public boolean onAction( Window owner) {
		return DialogListSettings.update( mTask, owner);
	}

	@Override
	public void removeClickListener( ActionListener l) {
		mList.removeClickListener( l);
	}

	@Override
	public void update() {
		SettingFactory.get( mTask, mList.getModel());
	}

	private final class Renderer extends AListCellRenderer<FSetting> {
		private static final long serialVersionUID = 3374147415409104551L;
		private static final double ICON_ZOOM = 0.6;
		private LeafIcon mIcon = new LeafIcon( new Dimension( 260, 22));
		private LeafLabel mNeutral = new LeafLabel( "");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( false);
			setBorder( BorderFactory.createEmptyBorder( 2, 0, 2, 0));
//			setBorder( BorderFactory.createLineBorder( Color.RED));
			mIcon.setIcon( new StackIcon( ICON_ZOOM));
			add( Box.createHorizontalStrut( 15));
			add( createBox());
			add( Box.createHorizontalStrut( 15));
		}

		private JComponent createBox() {
			JComponent box = AEntity.leafBox( BoxLayout.Y_AXIS);
			box.setAlignmentY( CENTER_ALIGNMENT);
			box.add( mIcon);
			box.add( mNeutral);
			return box;
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends FSetting> list, FSetting rs, int index, boolean isSelected, boolean cellHasFocus) {
			mIcon.setIcon( new ReputationIcon( rs.mRep));
			mNeutral.setText( String.format( "    %s: %s (%d)", rs.mRep.getName(), rs.mRep.getNeutral(), 0));
			return this;
		}
	}

	private static class SettingFactory extends AHQMWorker<Object, DefaultListModel<FSetting>> {
		private static final SettingFactory WORKER = new SettingFactory();

		private SettingFactory() {
		}

		public static void get( FQuestTaskReputationTarget task, DefaultListModel<FSetting> model) {
			model.clear();
			task.forEachSetting( WORKER, model);
		}

		@Override
		public Object forSetting( FSetting rs, DefaultListModel<FSetting> model) {
			model.addElement( rs);
			return null;
		}
	}
}
