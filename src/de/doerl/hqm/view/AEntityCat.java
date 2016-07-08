package de.doerl.hqm.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.AMember;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.WarnDialogs;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.leafs.LeafList;

abstract class AEntityCat<T extends AMember> extends AEntity<ACategory<T>> {
	private static final long serialVersionUID = 577651171001039679L;
	private static final Logger LOGGER = Logger.getLogger( AEntityCat.class.getName());
	protected final ACategory<T> mCategory;
	protected ABundleAction mNameAction = new NameAction();
	protected ABundleAction mAddAction = new AddAction();
	protected ABundleAction mMoveUpAction = new MoveUpAction();
	protected ABundleAction mMoveDownAction = new MoveDownAction();
	protected ABundleAction mDeleteAction = new DeleteAction();
	protected LeafList<T> mList = new LeafList<>();
	protected volatile T mActiv;
	private DialogTextField mEdit;

	public AEntityCat( ACategory<T> cat, EditController ctrl, AListCellRenderer<T> renderer) {
		super( ctrl, new GridLayout( 1, 2));
		mCategory = cat;
		mEdit = new DialogTextField( ctrl.getFrame());
		mList.setCellRenderer( renderer);
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	@SuppressWarnings( "unchecked")
	@Override
	public void baseAdded( ModelEvent event) {
		try {
			ABase base = event.mBase;
			if (mCategory.equals( base.getParent())) {
				update();
				updateActive( (T) base, true);
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
			updateActive( getFirst(), true);
			updateMoveActions();
		}
	}

	@Override
	public void baseTreeChange( ModelEvent event) {
	}

	@Override
	protected void createLeft( JPanel leaf) {
		leaf.add( leafScoll( mList, 10000));
	}

	protected abstract void doDelete();

	@Override
	public ACategory<T> getBase() {
		return mCategory;
	}

	protected abstract T getFirst();

	protected void init() {
		createLeafs();
		update();
		updateActive( getFirst(), true);
		mList.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent evt) {
				T member = mList.getSelectedValue();
				if (member != null) {
					SwingUtilities.invokeLater( new ListMouseAction( member));
				}
			}
		});
		mList.addClickListener( mNameAction);
	}

	protected void initTools() {
		mTool.add( mNameAction);
		mTool.addSeparator();
		mTool.add( mAddAction);
		mTool.add( mMoveUpAction);
		mTool.add( mMoveDownAction);
		mTool.add( mDeleteAction);
		mTool.addSeparator();
	}

	protected abstract void update();

	protected abstract void updateActive( T tier, boolean toggel);

	protected void updateMoveActions() {
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
			String result = mEdit.change( "new", DataBitHelper.QUEST_NAME_LENGTH);
			if (result != null) {
				mCtrl.memberCreate( mCategory, result);
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
				doDelete();
			}
		}
	}

	private final class ListMouseAction implements Runnable {
		private T mTier;

		public ListMouseAction( T tier) {
			mTier = tier;
		}

		@Override
		public void run() {
			updateActive( mTier, true);
			updateMoveActions();
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
				String result = mEdit.change( mActiv.getName(), DataBitHelper.QUEST_NAME_LENGTH);
				if (result != null) {
					mActiv.setName( result);
					mCtrl.fireChanged( mCategory);
				}
			}
		}
	}
}
