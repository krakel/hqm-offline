package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.ADialog;
import de.doerl.hqm.utils.Utils;

class EntityReputationCat extends AEntityCat<FReputation> {
//	private static final Logger LOGGER = Logger.getLogger( EntityReputationCat.class.getName());
	private static final long serialVersionUID = -2408903593673459841L;
	private ABundleAction mNeutralAction = new NeutralAction();
	private ABundleAction mMarkerAction = new MarkerAction();
	private LeafIcon mIcon = new LeafIcon( new Dimension( 260, 22));
	private LeafTextField mNeutral = new LeafTextField( false);
	private LeafList<FMarker> mMarker = new LeafList<>();

	EntityReputationCat( FReputationCat cat, EditController ctrl) {
		super( cat, ctrl, new ListRenderer());
		mMarker.setCellRenderer( new MarkerRenderer());
		init();
		mMarker.addMouseListener( new BorderAdapter( mMarker));
		mNeutral.addClickListener( mNeutralAction);
		mMarker.addClickListener( mMarkerAction);
		initTools();
		mTool.add( mNeutralAction);
		mTool.add( mMarkerAction);
		mTool.addSeparator();
		updateMoveActions();
	}

	@Override
	protected void createRight( JPanel leaf) {
		leaf.add( mIcon);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( mNeutral);
		leaf.add( leafScoll( mMarker, 50));
	}

	@Override
	protected void doDelete() {
		mCtrl.reputationDelete( mActiv);
	}

	@Override
	protected FReputation getFirst() {
		return ReputationSetFirst.get( mCategory);
	}

	@Override
	protected void update() {
		ReputationUpdate.get( mCategory, mList.getModel());
	}

	private void updateActions( boolean enabled) {
		mNameAction.setEnabled( enabled);
		mNeutralAction.setEnabled( enabled);
		mMarkerAction.setEnabled( enabled);
		mDeleteAction.setEnabled( enabled);
	}

	@Override
	protected void updateActive( FReputation rep, boolean toggel) {
		if (rep == null) {
			mNeutral.setText( null);
			mMarker.getModel().clear();
			mList.clearSelection();
			updateActions( false);
			updateControls( false);
			mActiv = null;
		}
		else if (toggel && Utils.equals( rep, mActiv)) {
			mNeutral.setText( null);
			mMarker.getModel().clear();
			mList.clearSelection();
			updateActions( false);
			updateControls( false);
			mActiv = null;
		}
		else {
			mIcon.setIcon( new ReputationIcon( rep));
			mNeutral.setText( rep.getNeutral());
			MarkerUpdate.get( rep, mCtrl, mMarker.getModel());
			mList.setSelectedValue( rep, true);
			updateActions( true);
			updateControls( true);
			mActiv = rep;
		}
	}

	private void updateControls( boolean enabled) {
		mIcon.setVisible( enabled);
		mNeutral.setVisible( enabled);
		mMarker.setVisible( enabled);
	}

	private static class ListRenderer extends AListCellRenderer<FReputation> {
		private static final long serialVersionUID = 5722100642992864392L;
		private LeafLabel mTitle = new LeafLabel( UNSELECTED, "", true);

		public ListRenderer() {
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
			setOpaque( false);
			setBorder( BorderFactory.createEmptyBorder( 0, 10, 10, 10));
			add( mTitle);
		}

		public Component getListCellRendererComponent( JList<? extends FReputation> list, FReputation qs, int index, boolean isSelected, boolean cellHasFocus) {
			mTitle.setText( qs.getName());
			mTitle.setForeground( isSelected ? SELECTED : UNSELECTED);
			return this;
		}
	}

	private final class MarkerAction extends ABundleAction {
		private static final long serialVersionUID = -9132294384775100399L;

		public MarkerAction() {
			super( "entity.repcat.marker");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (DialogListMarker.update( mActiv, mCtrl.getFrame())) {
				mCtrl.fireChanged( mActiv);
			}
		}
	}

	static class MarkerRenderer extends AListCellRenderer<FMarker> {
		private static final long serialVersionUID = -5514545387219031325L;
		private JLabel mMark = new LeafLabel( "");
		private JLabel mName = new LeafLabel( "");

		public MarkerRenderer() {
			setLayout( new GridLayout( 1, 2, 2, 2));
			setOpaque( false);
			AEntity.setSizes( this, ADialog.FONT_NORMAL.getSize());
			mMark.setHorizontalTextPosition( SwingConstants.RIGHT);
			mName.setHorizontalTextPosition( SwingConstants.LEFT);
			add( mName);
			add( mMark);
		}

		public Component getListCellRendererComponent( JList<? extends FMarker> list, FMarker mark, int index, boolean isSelected, boolean cellHasFocus) {
			mName.setText( mark.getName());
			mMark.setText( String.format( "%6d", mark.mMark));
			return this;
		}
	}

	private static class MarkerUpdate extends AHQMWorker<Object, DefaultListModel<FMarker>> {
		private static final MarkerUpdate WORKER = new MarkerUpdate();

		private MarkerUpdate() {
		}

		public static void get( FReputation rep, EditController ctrl, DefaultListModel<FMarker> model) {
			model.clear();
			if (rep != null) {
				rep.forEachMarker( WORKER, model);
			}
		}

		@Override
		public Object forMarker( FMarker mark, DefaultListModel<FMarker> model) {
			model.addElement( mark);
			return null;
		}
	}

	public class NeutralAction extends ABundleAction {
		private static final long serialVersionUID = -6600823639407037564L;

		public NeutralAction() {
			super( "entity.repcat.neutral");
		}

		@Override
		public void actionPerformed( ActionEvent e) {
			String result = DialogTextField.update( mActiv.getNeutral(), mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
			if (result != null) {
				mActiv.setNeutral( result);
				mCtrl.fireChanged( mActiv);
			}
		}
	}

	private static class ReputationSetFirst extends AHQMWorker<FReputation, Object> {
		private static final ReputationSetFirst WORKER = new ReputationSetFirst();

		private ReputationSetFirst() {
		}

		public static FReputation get( ACategory<FReputation> cat) {
			return cat.forEachMember( WORKER, null);
		}

		@Override
		public FReputation forReputation( FReputation rep, Object p) {
			return rep;
		}
	}

	private static class ReputationUpdate extends AHQMWorker<Object, DefaultListModel<FReputation>> {
		private static final ReputationUpdate WORKER = new ReputationUpdate();

		private ReputationUpdate() {
		}

		public static void get( ACategory<FReputation> set, DefaultListModel<FReputation> model) {
			model.clear();
			set.forEachMember( WORKER, model);
		}

		@Override
		protected Object doBase( ABase base, DefaultListModel<FReputation> model) {
			return null;
		}

		@Override
		public Object forReputation( FReputation rep, DefaultListModel<FReputation> model) {
			model.addElement( rep);
			return null;
		}
	}
}
