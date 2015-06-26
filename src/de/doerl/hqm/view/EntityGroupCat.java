package de.doerl.hqm.view;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.utils.Utils;

class EntityGroupCat extends AEntityCat<FGroup> {
//	private static final Logger LOGGER = Logger.getLogger( EntityGroupCat.class.getName());
	private static final long serialVersionUID = 2046344393475287723L;

	EntityGroupCat( FGroupCat cat, EditController ctrl) {
		super( cat, ctrl, new ListRenderer());
		init();
//		mMarker.addMouseListener( new BorderAdapter( mMarker));
//		mNeutral.addClickListener( mNeutralAction);
//		mMarker.addClickListener( mMarkerAction);
		initTools();
//		mTool.add( mNeutralAction);
//		mTool.add( mMarkerAction);
//		mTool.addSeparator();
		updateMoveActions();
	}

	@Override
	protected void createRight( JPanel leaf) {
	}

	@Override
	protected void doDelete() {
		mCtrl.groupDelete( mActiv);
	}

	@Override
	protected FGroup getFirst() {
		return GroupSetFirst.get( mCategory);
	}

	@Override
	protected void update() {
		GroupUpdate.get( mCategory, mList.getModel());
	}

	private void updateActions( boolean enabled) {
		mNameAction.setEnabled( enabled);
//		mNeutralAction.setEnabled( enabled);
		mDeleteAction.setEnabled( enabled);
	}

	@Override
	protected void updateActive( FGroup tier, boolean toggel) {
		if (tier == null) {
//			mIcon.setVisible( false);
//			mNeutral.setText( null);
//			mMarker.getModel().clear();
//			mMarker.setVisible( false);
			mList.clearSelection();
			updateActions( false);
			mActiv = null;
		}
		else if (toggel && Utils.equals( tier, mActiv)) {
//			mIcon.setVisible( false);
//			mNeutral.setText( null);
//			mMarker.getModel().clear();
//			mMarker.setVisible( false);
			mList.clearSelection();
			updateActions( false);
			mActiv = null;
		}
		else {
//			mIcon.setIcon( new ReputationIcon( tier));
//			mIcon.setVisible( true);
//			mNeutral.setText( tier.mNeutral);
//			MarkerUpdate.get( tier, mCtrl, mMarker.getModel());
//			mMarker.setVisible( true);
			mList.setSelectedValue( tier, true);
			updateActions( true);
			mActiv = tier;
		}
	}

	private static class GroupSetFirst extends AHQMWorker<FGroup, Object> {
		private static final GroupSetFirst WORKER = new GroupSetFirst();

		private GroupSetFirst() {
		}

		public static FGroup get( ACategory<FGroup> cat) {
			return cat.forEachMember( WORKER, null);
		}

		@Override
		public FGroup forGroup( FGroup grp, Object p) {
			return grp;
		}
	}

	private static class GroupUpdate extends AHQMWorker<Object, DefaultListModel<FGroup>> {
		private static final GroupUpdate WORKER = new GroupUpdate();

		private GroupUpdate() {
		}

		public static void get( ACategory<FGroup> set, DefaultListModel<FGroup> model) {
			model.clear();
			set.forEachMember( WORKER, model);
		}

		@Override
		protected Object doBase( ABase base, DefaultListModel<FGroup> model) {
			return null;
		}

		@Override
		public Object forGroup( FGroup grp, DefaultListModel<FGroup> model) {
			model.addElement( grp);
			return null;
		}
	}

	private static class ListRenderer extends AListCellRenderer<FGroup> {
		private static final long serialVersionUID = -525448181901664849L;
		private LeafLabel mTitle = new LeafLabel( UNSELECTED, "", true);

		public ListRenderer() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
			setOpaque( false);
			setBorder( BorderFactory.createEmptyBorder( 0, 10, 10, 10));
			add( mTitle);
		}

		public Component getListCellRendererComponent( JList<? extends FGroup> list, FGroup grp, int index, boolean isSelected, boolean cellHasFocus) {
			mTitle.setText( Utils.validString( grp.mName) ? grp.mName : "~missing~");
			mTitle.setForeground( isSelected ? SELECTED : UNSELECTED);
			return this;
		}
	}
}
