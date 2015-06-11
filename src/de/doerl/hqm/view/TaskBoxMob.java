package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;

import de.doerl.hqm.base.FMob;
import de.doerl.hqm.base.FQuestTaskMob;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.utils.mods.ImageLoader;

class TaskBoxMob extends ATaskBox {
	private static final long serialVersionUID = 410081438393816262L;
	private FQuestTaskMob mTask;
	private LeafList<FMob> mList = new LeafList<>();

	public TaskBoxMob( FQuestTaskMob task) {
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
	public FQuestTaskMob getTask() {
		return mTask;
	}

	@Override
	public boolean onAction( Window owner) {
		return DialogListMobs.update( mTask, owner);
	}

	@Override
	public void removeClickListener( ActionListener l) {
		mList.removeClickListener( l);
	}

	@Override
	public void update() {
		MobFactory.get( mTask, mList.getModel());
	}

	private static class MobFactory extends AHQMWorker<Object, DefaultListModel<FMob>> {
		private static final MobFactory WORKER = new MobFactory();

		private MobFactory() {
		}

		public static void get( FQuestTaskMob task, DefaultListModel<FMob> model) {
			model.clear();
			task.forEachMob( WORKER, model);
		}

		@Override
		public Object forMob( FMob mob, DefaultListModel<FMob> model) {
			model.addElement( mob);
			return null;
		}
	}

	private final class Renderer extends AListCellRenderer<FMob> {
		private static final long serialVersionUID = 3374147415409104551L;
		private LeafIcon mIcon = new LeafIcon();
		private LeafLabel mName = new LeafLabel( "", true);
		private LeafLabel mKilled = new LeafLabel( "");
		private LeafLabel mTotal = new LeafLabel( "");

		public Renderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( false);
			setBorder( BorderFactory.createEmptyBorder( 2, 0, 2, 0));
			mName.setAlignmentY( TOP_ALIGNMENT);
			mIcon.setIcon( new StackIcon());
			add( mIcon);
			add( Box.createHorizontalStrut( AEntity.GAP));
			add( createBox());
		}

		private JComponent createBox() {
			JComponent box = AEntity.leafBox( BoxLayout.Y_AXIS);
			box.add( mName);
			box.add( mKilled);
			box.add( mTotal);
			return box;
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends FMob> list, FMob mob, int index, boolean isSelected, boolean cellHasFocus) {
			Image img = ImageLoader.getImage( mob.mIcon, createUpdater( list));
			mIcon.setIcon( new StackIcon( img));
			mName.setText( mob.mName);
			mTotal.setText( String.format( "Kill a total of %d", mob.mKills));
			return this;
		}
	}
}
