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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FGroup;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.ADialog.TextFieldInteger;
import de.doerl.hqm.ui.WarnDialogs;
import de.doerl.hqm.utils.Utils;

class EntityGroupTier extends AEntity<FGroupTier> {
	private static final long serialVersionUID = 2046344393475287723L;
	private static final Logger LOGGER = Logger.getLogger( EntityGroupTier.class.getName());
	private FGroupTier mTier;
	protected ABundleAction mNameAction = new NameAction();
	protected ABundleAction mAddAction = new AddAction();
	protected ABundleAction mMoveUpAction = new MoveUpAction();
	protected ABundleAction mMoveDownAction = new MoveDownAction();
	protected ABundleAction mDeleteAction = new DeleteAction();
	protected LeafList<FGroup> mList = new LeafList<>();
	private JTextField mLimit = new TextFieldInteger();
	private LeafStacks mStacks = new LeafStacks( ICON_SIZE, null);
	protected volatile FGroup mActiv;

	EntityGroupTier( FGroupTier tier, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 2));
		mTier = tier;
		mList.setCellRenderer( new ListRenderer());
		mLimit.setPreferredSize( new Dimension( 200, 2 * getFont().getSize()));
		mLimit.setMaximumSize( new Dimension( Short.MAX_VALUE, 2 * getFont().getSize()));
		mLimit.setAlignmentX( LEFT_ALIGNMENT);
		createLeafs();
		GroupUpdate.get( mTier, mList.getModel());
		updateActive( GroupSetFirst.get( mTier), true);
		mList.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent evt) {
				FGroup member = mList.getSelectedValue();
				if (member != null) {
					SwingUtilities.invokeLater( new ListMouseAction( member));
				}
			}
		});
		mList.addClickListener( mNameAction);
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
	public void baseActivate( ModelEvent event) {
	}

	@Override
	public void baseAdded( ModelEvent event) {
		try {
			ABase base = event.mBase;
			if (mTier.equals( base.getParent())) {
				GroupUpdate.get( mTier, mList.getModel());
				updateActive( (FGroup) base, true);
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
		if (mTier.equals( base) || mTier.equals( base.getParent())) {
			GroupUpdate.get( mTier, mList.getModel());
			updateActive( mActiv, false);
			updateMoveActions();
		}
	}

	@Override
	public void baseRemoved( ModelEvent event) {
		ABase base = event.mBase;
		if (mTier.equals( base.getParent())) {
			GroupUpdate.get( mTier, mList.getModel());
			updateActive( GroupSetFirst.get( mTier), true);
			updateMoveActions();
		}
	}

	@Override
	protected void createLeft( JPanel leaf) {
		leaf.add( leafScoll( mList, 10000));
	}

	@Override
	protected void createRight( JPanel leaf) {
		leaf.add( mLimit);
		leaf.add( Box.createVerticalStrut( GAP));
		leaf.add( mStacks);
		leaf.add( Box.createVerticalGlue());
	}

	@Override
	public FGroupTier getBase() {
		return mTier;
	}

	private void initTools() {
		mTool.add( mNameAction);
		mTool.addSeparator();
		mTool.add( mAddAction);
		mTool.add( mMoveUpAction);
		mTool.add( mMoveDownAction);
		mTool.add( mDeleteAction);
		mTool.addSeparator();
	}

	private void updateActions( boolean enabled) {
		mNameAction.setEnabled( enabled);
		mDeleteAction.setEnabled( enabled);
	}

	private void updateActive( FGroup tier, boolean toggel) {
		if (tier == null) {
			mList.clearSelection();
			updateActions( false);
			updateControls( false);
			mActiv = null;
		}
		else if (toggel && Utils.equals( tier, mActiv)) {
			mList.clearSelection();
			updateActions( false);
			updateControls( false);
			mActiv = null;
		}
		else {
			mLimit.setText( String.valueOf( tier.mLimit));
			mStacks.update( tier.mStacks);
			mList.setSelectedValue( tier, true);
			updateActions( true);
			updateControls( true);
			mActiv = tier;
		}
	}

	private void updateControls( boolean enabled) {
		mLimit.setVisible( enabled);
		mStacks.setVisible( enabled);
	}

	private void updateMoveActions() {
		if (mActiv == null) {
			mMoveUpAction.setEnabled( false);
			mMoveDownAction.setEnabled( false);
		}
		else {
			mMoveUpAction.setEnabled( !mActiv.isFirst());
			mMoveDownAction.setEnabled( !mActiv.isLast());
		}
	}

	private final class AddAction extends ABundleAction {
		private static final long serialVersionUID = 1423646654288791995L;

		public AddAction() {
			super( "entity.cat.add");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			String result = DialogTextField.update( "new", mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
			if (result != null) {
				mCtrl.groupCreate( mTier, result);
			}
		}
	}

	private final class DeleteAction extends ABundleAction {
		private static final long serialVersionUID = 4944191855639178206L;

		public DeleteAction() {
			super( "entity.cat.delete");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (WarnDialogs.askDelete( mCtrl.getFrame())) {
				mCtrl.groupDelete( mActiv);
			}
		}
	}

	private static class GroupSetFirst extends AHQMWorker<FGroup, Object> {
		private static final GroupSetFirst WORKER = new GroupSetFirst();

		private GroupSetFirst() {
		}

		public static FGroup get( FGroupTier tier) {
			return tier.forEachGroup( WORKER, null);
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

		public static void get( FGroupTier tier, DefaultListModel<FGroup> model) {
			model.clear();
			tier.forEachGroup( WORKER, model);
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

	private final class ListMouseAction implements Runnable {
		private FGroup mGroup;

		public ListMouseAction( FGroup grp) {
			mGroup = grp;
		}

		@Override
		public void run() {
			updateActive( mGroup, false);
			updateMoveActions();
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

	private final class MoveDownAction extends ABundleAction {
		private static final long serialVersionUID = 3097362354463293566L;

		public MoveDownAction() {
			super( "entity.cat.moveDown");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				mActiv.moveDown();
				mCtrl.fireChanged( mActiv.getParent());
			}
		}
	}

	private final class MoveUpAction extends ABundleAction {
		private static final long serialVersionUID = -101318734375981823L;

		public MoveUpAction() {
			super( "entity.cat.moveUp");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				mActiv.moveUp();
				mCtrl.fireChanged( mActiv.getParent());
			}
		}
	}

	private final class NameAction extends ABundleAction {
		private static final long serialVersionUID = 1241947016335382154L;

		public NameAction() {
			super( "entity.cat.name");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				String result = DialogTextField.update( mActiv.mName, mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
				if (result != null) {
					mActiv.mName = result;
					mCtrl.fireChanged( mTier);
				}
			}
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
