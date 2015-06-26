package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FMarker;
import de.doerl.hqm.base.FReputation;
import de.doerl.hqm.base.FReputationCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.EditFrame;
import de.doerl.hqm.ui.WarnDialogs;
import de.doerl.hqm.utils.Utils;

class EntityReputationCat extends AEntity<FReputationCat> {
	private static final long serialVersionUID = -2408903593673459841L;
	private static final Logger LOGGER = Logger.getLogger( EntityReputationCat.class.getName());
	private final FReputationCat mCategory;
	private JToolBar mTool = EditFrame.createToolBar();
	private ABundleAction mNameAction = new NameAction();
	private ABundleAction mNeutralAction = new NeutralAction();
	private ABundleAction mMarkerAction = new MarkerAction();
	private ABundleAction mAddAction = new AddAction();
	private ABundleAction mDeleteAction = new DeleteAction();
	private ABundleAction mMoveUpAction = new MoveUpAction();
	private ABundleAction mMoveDownAction = new MoveDownAction();
	private LeafList<FReputation> mList = new LeafList<>();
	private LeafIcon mIcon = new LeafIcon( new Dimension( 260, 22));
	private LeafTextField mNeutral = new LeafTextField( false);
	private LeafList<FMarker> mMarker = new LeafList<>();
	private volatile FReputation mActiv;

	EntityReputationCat( FReputationCat cat, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 2));
		mCategory = cat;
		mList.setCellRenderer( new ListRenderer());
		mMarker.setCellRenderer( new MarkerRenderer());
		createLeafs();
		update();
		updateActive( ReputationSetFirst.get( mCategory), true);
		mList.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent evt) {
				FReputation rep = mList.getSelectedValue();
				if (rep != null) {
					SwingUtilities.invokeLater( new ListMouseAction( rep));
				}
			}
		});
		mMarker.addMouseListener( new BorderAdapter( mMarker));
		mList.addClickListener( mNameAction);
		mNeutral.addClickListener( mNeutralAction);
		mMarker.addClickListener( mMarkerAction);
		mTool.add( mNameAction);
		mTool.addSeparator();
		mTool.add( mAddAction);
		mTool.add( mMoveUpAction);
		mTool.add( mMoveDownAction);
		mTool.add( mDeleteAction);
		mTool.addSeparator();
		mTool.add( mNeutralAction);
		mTool.add( mMarkerAction);
		mTool.addSeparator();
		updateMoveActions();
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	@Override
	public void baseAdded( ModelEvent event) {
		try {
			ABase base = event.mBase;
			if (mCategory.equals( base.getParent())) {
				update();
				updateActive( (FReputation) base, true);
				updateMoveActions();
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	@Override
	public void baseChanged( ModelEvent event) {
		ABase base = event.mBase;
		if (mCategory.equals( base) || mCategory.equals( base.getParent())) {
			update();
			updateActive( mActiv, false);
			updateMoveActions();
		}
	}

	@Override
	public void baseRemoved( ModelEvent event) {
		ABase base = event.mBase;
		if (mCategory.equals( base.getParent())) {
			update();
			updateActive( ReputationSetFirst.get( mCategory), true);
			updateMoveActions();
		}
	}

	@Override
	protected void createLeft( JPanel leaf) {
		leaf.add( leafScoll( mList, 10000));
	}

	@Override
	protected void createRight( JPanel leaf) {
		leaf.add( mIcon);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( mNeutral);
		leaf.add( leafScoll( mMarker, 50));
	}

	@Override
	public FReputationCat getBase() {
		return mCategory;
	}

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}

	private void update() {
		ReputationUpdate.get( mCategory, mList.getModel());
	}

	private void updateActions( boolean enabled) {
		mNameAction.setEnabled( enabled);
		mNeutralAction.setEnabled( enabled);
		mDeleteAction.setEnabled( enabled);
	}

	private void updateActive( FReputation rep, boolean toggel) {
		if (rep == null) {
			mIcon.setVisible( false);
			mNeutral.setText( null);
			mMarker.getModel().clear();
			mMarker.setVisible( false);
			mList.clearSelection();
			updateActions( false);
			mActiv = null;
		}
		else if (toggel && Utils.equals( rep, mActiv)) {
			mIcon.setVisible( false);
			mNeutral.setText( null);
			mMarker.getModel().clear();
			mMarker.setVisible( false);
			mList.clearSelection();
			updateActions( false);
			mActiv = null;
		}
		else {
			mIcon.setIcon( new ReputationIcon( rep));
			mIcon.setVisible( true);
			mNeutral.setText( rep.mNeutral);
			MarkerUpdate.get( rep, mCtrl, mMarker.getModel());
			mMarker.setVisible( true);
			mList.setSelectedValue( rep, true);
			updateActions( true);
			mActiv = rep;
		}
	}

	private void updateMoveActions() {
		if (mActiv == null) {
			mMoveUpAction.setEnabled( false);
			mMoveDownAction.setEnabled( false);
		}
		else if (mActiv.isFirst()) {
			mMoveUpAction.setEnabled( false);
			mMoveDownAction.setEnabled( true);
		}
		else if (mActiv.isLast()) {
			mMoveUpAction.setEnabled( true);
			mMoveDownAction.setEnabled( false);
		}
		else {
			mMoveUpAction.setEnabled( true);
			mMoveDownAction.setEnabled( true);
		}
	}

	private final class AddAction extends ABundleAction {
		private static final long serialVersionUID = -2052561675206500030L;

		public AddAction() {
			super( "entity.setcat.add");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			String result = DialogTextField.update( "new", mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
			if (result != null) {
				mCtrl.reputationCreate( mCategory, result);
			}
		}
	}

	private final class DeleteAction extends ABundleAction {
		private static final long serialVersionUID = 4944191855639178206L;

		public DeleteAction() {
			super( "entity.setcat.delete");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (WarnDialogs.askDelete( mCtrl.getFrame())) {
				mCtrl.reputationDelete( mActiv);
			}
		}
	}

	private final class ListMouseAction implements Runnable {
		private FReputation mRep;

		public ListMouseAction( FReputation rep) {
			mRep = rep;
		}

		@Override
		public void run() {
			updateActive( mRep, true);
			updateMoveActions();
		}
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
			mTitle.setText( qs.mName);
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

	private static class MarkerRenderer extends AListCellRenderer<FMarker> {
		private static final long serialVersionUID = -5514545387219031325L;
		private JLabel mTitle = new LeafLabel( UNSELECTED, "", false);

		public MarkerRenderer() {
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
			setOpaque( false);
			add( mTitle);
		}

		public Component getListCellRendererComponent( JList<? extends FMarker> list, FMarker mark, int index, boolean isSelected, boolean cellHasFocus) {
			mTitle.setText( String.format( "%6d %s", mark.mMark, mark.mName));
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

	private final class MoveDownAction extends ABundleAction {
		private static final long serialVersionUID = 3097362354463293566L;

		public MoveDownAction() {
			super( "entity.setcat.moveDown");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				mActiv.moveDown();
				mCtrl.fireChanged( mActiv.mParentCategory);
			}
		}
	}

	private final class MoveUpAction extends ABundleAction {
		private static final long serialVersionUID = -101318734375981823L;

		public MoveUpAction() {
			super( "entity.setcat.moveUp");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				mActiv.moveUp();
				mCtrl.fireChanged( mActiv.mParentCategory);
			}
		}
	}

	private final class NameAction extends ABundleAction {
		private static final long serialVersionUID = 3786489461268817721L;

		public NameAction() {
			super( "entity.setcat.title");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				String result = DialogTextField.update( mActiv.mName, mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
				if (result != null) {
					mActiv.mName = result;
					mCtrl.fireChanged( mCategory);
				}
			}
		}
	}

	public class NeutralAction extends ABundleAction {
		private static final long serialVersionUID = -6600823639407037564L;

		public NeutralAction() {
			super( "entity.repcat.neutral");
		}

		@Override
		public void actionPerformed( ActionEvent e) {
			String result = DialogTextField.update( mActiv.mNeutral, mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
			if (result != null) {
				mActiv.mNeutral = result;
				mCtrl.fireChanged( mActiv);
			}
		}
	}

	private static class ReputationSetFirst extends AHQMWorker<FReputation, Object> {
		private static final ReputationSetFirst WORKER = new ReputationSetFirst();

		private ReputationSetFirst() {
		}

		public static FReputation get( FReputationCat cat) {
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

		public static void get( FReputationCat set, DefaultListModel<FReputation> model) {
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
