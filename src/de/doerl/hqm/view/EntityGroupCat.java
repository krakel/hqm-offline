package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupCat;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.ui.ADialog.TextFieldInteger;
import de.doerl.hqm.utils.Utils;

class EntityGroupCat extends AEntityCat<FGroup> {
//	private static final Logger LOGGER = Logger.getLogger( EntityGroupCat.class.getName());
	private static final long serialVersionUID = 2046344393475287723L;
	private JComboBox<FGroupTier> mTier;
	private JTextField mLimit = new TextFieldInteger();
	private LeafStacks mStacks = new LeafStacks( ICON_SIZE, null);

	EntityGroupCat( FGroupCat cat, EditController ctrl) {
		super( cat, ctrl, new ListRenderer());
		mTier = new JComboBox<>( cat.mParentHQM.mGroupTierCat.mArr);
		mTier.setPreferredSize( new Dimension( 200, 2 * getFont().getSize()));
		mTier.setMaximumSize( new Dimension( Short.MAX_VALUE, 2 * getFont().getSize()));
		mTier.setRenderer( new TierRenderere());
		mTier.setAlignmentX( LEFT_ALIGNMENT);
		mLimit.setPreferredSize( new Dimension( 200, 2 * getFont().getSize()));
		mLimit.setMaximumSize( new Dimension( Short.MAX_VALUE, 2 * getFont().getSize()));
		mLimit.setAlignmentX( LEFT_ALIGNMENT);
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
		leaf.add( mTier);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( mLimit);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( mStacks);
		leaf.add( Box.createVerticalGlue());
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
		mDeleteAction.setEnabled( enabled);
	}

	@Override
	protected void updateActive( FGroup tier, boolean toggel) {
		if (tier == null) {
			mTier.setSelectedIndex( -1);
			mList.clearSelection();
			updateActions( false);
			updateControls( false);
			mActiv = null;
		}
		else if (toggel && Utils.equals( tier, mActiv)) {
			mTier.setSelectedIndex( -1);
			mList.clearSelection();
			updateActions( false);
			updateControls( false);
			mActiv = null;
		}
		else {
			mTier.setSelectedItem( tier.mTier);
			mLimit.setText( String.valueOf( tier.mLimit));
			mStacks.update( tier.mStacks);
			mList.setSelectedValue( tier, true);
			updateActions( true);
			updateControls( true);
			mActiv = tier;
		}
	}

	private void updateControls( boolean enabled) {
		mTier.setVisible( enabled);
		mLimit.setVisible( enabled);
		mStacks.setVisible( enabled);
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

	public class TierRenderere extends AListCellRenderer<FGroupTier> {
		private static final long serialVersionUID = 4392939516279598751L;
		private LeafLabel mTitle = new LeafLabel( UNSELECTED, "", false);

		private TierRenderere() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 0, 10, 10, 10));
			add( mTitle);
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends FGroupTier> list, FGroupTier value, int index, boolean isSelected, boolean cellHasFocus) {
			mTitle.setText( Utils.validString( value.mName) ? value.mName : "~missing~");
			return this;
		}
	}
}
