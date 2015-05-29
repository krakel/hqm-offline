package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

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
		return false;
	}

	@Override
	public void removeClickListener( ActionListener l) {
		mList.removeClickListener( l);
	}

	@Override
	public void update() {
		SettingFactory.get( mTask, mList.getModel());
	}

//		dataBox.add( new LeafReputation( rs));
//		dataBox.add( new LeafLabel( String.format( "    %s: %s (%d)", rs.mRep.mName, rs.mRep.mNeutral.mValue, 0)));
	private final class Renderer extends JPanel implements ListCellRenderer<FSetting> {
		private static final long serialVersionUID = 3374147415409104551L;
		private LeafReputation mImage = new LeafReputation( null);
		private LeafLabel mNeutral = new LeafLabel( "");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( false);
			setBorder( BorderFactory.createEmptyBorder( 2, 0, 2, 0));
			mImage.setIcon( new StackIcon( null, 0.6));
			add( mImage);
//			add( Box.createHorizontalStrut( AEntity.GAP));
			add( mNeutral);
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends FSetting> list, FSetting rs, int index, boolean isSelected, boolean cellHasFocus) {
//			Runnable cb = null;
//			AStack stk = value.mIcon.mValue;
//			Image img = ImageLoader.getImage( stk.getKey(), cb);
//			mIcon.setIcon( new StackIcon( img, 0.8, null));
			mNeutral.setText( String.format( "    %s: %s (%d)", rs.mRep.mName, rs.mRep.mNeutral.mValue, 0));
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
