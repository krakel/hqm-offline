package de.doerl.hqm.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.quest.BagTier;
import de.doerl.hqm.quest.GuiColor;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.leafs.LeafLabel;
import de.doerl.hqm.view.leafs.LeafList;

class EntityGroupTierCat extends AEntityCat<FGroupTier> {
//	private static final Logger LOGGER = Logger.getLogger( EntityGroupTierCat.class.getName());
	private static final long serialVersionUID = -1554359878811465741L;
	private ABundleAction mColorAction = new ColorAction();
	private ABundleAction mWeightAction = new WeightAction();
	private JComboBox<GuiColor> mColor = new JComboBox<>( GuiColor.values());
	private LeafList<Integer> mWeights = new LeafList<>();

	EntityGroupTierCat( FGroupTierCat cat, EditController ctrl) {
		super( cat, ctrl, new ListRenderer());
		mColor.setPreferredSize( new Dimension( 200, 2 * getFont().getSize()));
		mColor.setMaximumSize( new Dimension( Short.MAX_VALUE, 2 * getFont().getSize()));
		mColor.setRenderer( new ColorRenderere());
		mColor.setAlignmentX( LEFT_ALIGNMENT);
		mColor.addActionListener( mColorAction);
		mWeights.setCellRenderer( new WeightRenderer());
		mWeights.setAlignmentX( LEFT_ALIGNMENT);
		init();
		mWeights.addMouseListener( new BorderAdapter( mWeights));
		mWeights.addClickListener( mWeightAction);
		initTools();
//		mTool.add( mColorAction);
		mTool.add( mWeightAction);
		mTool.addSeparator();
		updateMoveActions();
	}

	@Override
	protected void createRight( JPanel leaf) {
		leaf.add( mColor);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( leafScoll( mWeights, 50));
	}

	@Override
	protected void doDelete() {
		mCtrl.grpTierDelete( mActiv);
	}

	@Override
	protected FGroupTier getFirst() {
		return GroupTierSetFirst.get( mCategory);
	}

	@Override
	protected void update() {
		GroupTierUpdate.get( mCategory, mList.getModel());
	}

	private void updateActions( boolean enabled) {
		mNameAction.setEnabled( enabled);
		mColorAction.setEnabled( enabled);
		mDeleteAction.setEnabled( enabled);
	}

	@Override
	protected void updateActive( FGroupTier tier, boolean toggel) {
		DefaultListModel<Integer> model = mWeights.getModel();
		if (tier == null) {
			mColor.setSelectedIndex( -1);
			model.clear();
			mList.clearSelection();
			updateActions( false);
			updateControls( false);
			mActiv = null;
		}
		else if (toggel && Utils.equals( tier, mActiv)) {
			mColor.setSelectedIndex( -1);
			model.clear();
			mList.clearSelection();
			updateActions( false);
			updateControls( false);
			mActiv = null;
		}
		else {
			model.clear();
			for (int i = 0; i < tier.mWeights.length; ++i) {
				model.addElement( Integer.valueOf( tier.mWeights[i]));
			}
			mList.setSelectedValue( tier, true);
			updateActions( true);
			updateControls( true);
			mActiv = tier;
			mColor.setSelectedIndex( tier.mColorID);
		}
	}

	private void updateControls( boolean enabled) {
		mColor.setVisible( enabled);
		mWeights.setVisible( enabled);
	}

	public class ColorAction extends ABundleAction {
		private static final long serialVersionUID = -6600823639407037564L;

		public ColorAction() {
			super( "entity.tier.color");
		}

		@Override
		public void actionPerformed( ActionEvent e) {
			if (mActiv != null) {
				GuiColor color = (GuiColor) mColor.getSelectedItem();
				if (color != null) {
					mColor.setBackground( color.getColor());
					if (mActiv.mColorID != color.ordinal()) {
						mActiv.mColorID = color.ordinal();
						mCtrl.fireChanged( mActiv);
					}
				}
			}
		}
	}

	public class ColorRenderere extends AListCellRenderer<GuiColor> {
		private static final long serialVersionUID = 3359484147315792999L;
		private LeafLabel mTitle = new LeafLabel( UNSELECTED, "", false);

		private ColorRenderere() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
			setOpaque( true);
			setBorder( BorderFactory.createEmptyBorder( 0, 10, 10, 10));
			mTitle.setForeground( Color.WHITE);
			add( mTitle);
		}

		@Override
		public Component getListCellRendererComponent( JList<? extends GuiColor> list, GuiColor value, int index, boolean isSelected, boolean cellHasFocus) {
			mTitle.setText( value.name());
			super.setBackground( value.getColor());
			return this;
		}

		@Override
		public void setBackground( Color bg) {
		}
	}

	private static class GroupTierSetFirst extends AHQMWorker<FGroupTier, Object> {
		private static final GroupTierSetFirst WORKER = new GroupTierSetFirst();

		private GroupTierSetFirst() {
		}

		public static FGroupTier get( ACategory<FGroupTier> cat) {
			return cat.forEachMember( WORKER, null);
		}

		@Override
		public FGroupTier forGroupTier( FGroupTier tier, Object p) {
			return tier;
		}
	}

	private static class GroupTierUpdate extends AHQMWorker<Object, DefaultListModel<FGroupTier>> {
		private static final GroupTierUpdate WORKER = new GroupTierUpdate();

		private GroupTierUpdate() {
		}

		public static void get( ACategory<FGroupTier> set, DefaultListModel<FGroupTier> model) {
			model.clear();
			set.forEachMember( WORKER, model);
		}

		@Override
		protected Object doBase( ABase base, DefaultListModel<FGroupTier> model) {
			return null;
		}

		@Override
		public Object forGroupTier( FGroupTier tier, DefaultListModel<FGroupTier> model) {
			model.addElement( tier);
			return null;
		}
	}

	private static class ListRenderer extends AListCellRenderer<FGroupTier> {
		private static final long serialVersionUID = -525448181901664849L;
		private LeafLabel mTitle = new LeafLabel( UNSELECTED, "", true);

		public ListRenderer() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
			setOpaque( false);
			setBorder( BorderFactory.createEmptyBorder( 0, 10, 10, 10));
			add( mTitle);
		}

		public Component getListCellRendererComponent( JList<? extends FGroupTier> list, FGroupTier tier, int index, boolean isSelected, boolean cellHasFocus) {
			mTitle.setText( tier.getName());
			mTitle.setForeground( isSelected ? SELECTED : UNSELECTED);
			return this;
		}
	}

	private final class WeightAction extends ABundleAction {
		private static final long serialVersionUID = 400069140338049930L;

		public WeightAction() {
			super( "entity.tier.weight");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (DialogListWeights.update( mActiv, mCtrl.getFrame())) {
				mCtrl.fireChanged( mActiv);
			}
		}
	}

	private static class WeightRenderer extends AListCellRenderer<Integer> {
		private static final long serialVersionUID = -8131645092948557749L;
		private JLabel mBag = new LeafLabel( "");
		private JLabel mName = new LeafLabel( "");

		public WeightRenderer() {
			setLayout( new GridLayout( 1, 2, 2, 2));
			setOpaque( false);
			AEntity.setSizes( this, ADialog.FONT_NORMAL.getSize());
			add( mBag);
			add( mName);
		}

		public Component getListCellRendererComponent( JList<? extends Integer> list, Integer value, int index, boolean isSelected, boolean cellHasFocus) {
			mBag.setText( BagTier.get( index).toString());
			mName.setText( String.format( "%6d", value));
			return this;
		}
	}
}
