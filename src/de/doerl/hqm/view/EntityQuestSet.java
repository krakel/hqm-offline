package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.EditFrame;
import de.doerl.hqm.ui.WarnDialogs;
import de.doerl.hqm.utils.Utils;

public class EntityQuestSet extends AEntity<FQuestSet> {
	private static final long serialVersionUID = 4427035968994904913L;
	private static final Logger LOGGER = Logger.getLogger( EntityQuestSet.class.getName());
	private FQuestSet mSet;
	private LeafAbsolute mLeaf = new LeafAbsolute();
	private JToolBar mTool = EditFrame.createToolBar();
//	private ClickHandler mHandler = new ClickHandler();
	private AddQuestAction mAddAction = new AddQuestAction();
	private MoveQuestAction mMoveAction = new MoveQuestAction();
	private DeleteQuestAction mDeleteAction = new DeleteQuestAction();
	private JToggleButton mBtnAdd = createToggleButton( mAddAction);
	private JToggleButton mBtnMove = createToggleButton( mMoveAction);
	private IClickListener mLeafQuestHandler = new LeafQuestHandler();
	private LeafMoveHandler mLeafMoveHandler = new LeafMoveHandler();
	private volatile LeafQuest mActiv;

	public EntityQuestSet( EditView view, FQuestSet set) {
		super( view, new GridLayout( 1, 1));
		mSet = set;
		add( mLeaf);
		LineFactory.set( set, this);
		QuestFactory.set( set, this);
		mTool.add( mBtnAdd);
		mTool.add( mBtnMove);
		mTool.add( mDeleteAction);
		mTool.addSeparator();
		mLeaf.addClickListener( new AClickListener() {
			@Override
			public void onSingleClick( MouseEvent evt) {
				createQuest( evt);
			}
		});
		mBtnAdd.addItemListener( new ItemListener() {
			@Override
			public void itemStateChanged( ItemEvent evt) {
				updateBtnAdd();
			}
		});
	}

	private LeafLine addLine( FQuest from, FQuest to) {
		LeafLine comp = new LeafLine( from, to);
		mLeaf.add( comp);
		return comp;
	}

	private LeafQuest addQuest( FQuest quest) {
		LeafQuest comp = new LeafQuest( quest);
		comp.addClickListener( mLeafQuestHandler);
		comp.addMouseListener( mLeafMoveHandler);
		comp.addMouseMotionListener( mLeafMoveHandler);
		mLeaf.add( comp, 0);
		return comp;
	}

	@Override
	protected void createLeft( JPanel leaf) {
	}

	private void createQuest( MouseEvent evt) {
		if (mBtnAdd.isSelected()) {
			FQuest quest = mSet.mParentCategory.mParentHQM.createQuest( "");
			quest.mQuestSet = mSet;
			quest.mX.mValue = evt.getX() / AEntity.ZOOM - quest.getW() / 2;
			quest.mY.mValue = evt.getY() / AEntity.ZOOM - quest.getH() / 2;
			mActiv = addQuest( quest);
			mActiv.revalidate();
			mActiv.repaint();
		}
	}

	@Override
	protected void createRight( JPanel leaf) {
	}

	private JToggleButton createToggleButton( Action a) {
		JToggleButton btn = new JToggleButton();
		if (a != null && (a.getValue( Action.SMALL_ICON) != null || a.getValue( Action.LARGE_ICON_KEY) != null)) {
			btn.setHideActionText( true);
		}
		btn.setHorizontalTextPosition( SwingConstants.CENTER);
		btn.setVerticalTextPosition( SwingConstants.BOTTOM);
		btn.setAction( a);
		return btn;
	}

	@Override
	public FQuestSet getBase() {
		return mSet;
	}

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}

	private void updateActive( MouseEvent evt) {
		try {
			mActiv = (LeafQuest) evt.getSource();
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	private void updateBtnAdd() {
		boolean enabled = !mBtnAdd.isSelected();
		mMoveAction.setEnabled( enabled);
		mDeleteAction.setEnabled( enabled);
	}

	private void updateBtnDelete() {
		WarnDialogs.warnMissing( mView);
	}

	private void updateBtnMove() {
		boolean unsel = !mBtnMove.isSelected();
		mAddAction.setEnabled( unsel);
		mDeleteAction.setEnabled( unsel);
		if (mActiv != null) {
			if (unsel) {
				mActiv.removeMouseMotionListener( mLeafMoveHandler);
			}
			else {
				mActiv.addMouseMotionListener( mLeafMoveHandler);
			}
		}
	}

	private final class AddQuestAction extends ABundleAction {
		private static final long serialVersionUID = -8667850457674712679L;

		public AddQuestAction() {
			super( "entity.add");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
//			updateAddQuest();
		}
	}

	private final class DeleteQuestAction extends ABundleAction {
		private static final long serialVersionUID = -433615690858042140L;

		public DeleteQuestAction() {
			super( "entity.delete");
			setEnabled( false);
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			updateBtnDelete();
		}
	}

	private final class LeafMoveHandler extends MouseAdapter {
		private int mOldX, mOldY;

		@Override
		public void mouseDragged( MouseEvent evt) {
			try {
				if (mBtnMove.isSelected()) {
					LeafQuest leaf = (LeafQuest) evt.getSource();
					moveDrag( leaf, evt);
				}
			}
			catch (ClassCastException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}

		@Override
		public void mousePressed( MouseEvent evt) {
			try {
				if (mBtnMove.isSelected()) {
					LeafQuest leaf = (LeafQuest) evt.getSource();
					moveBegin( leaf, evt);
				}
			}
			catch (ClassCastException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}

		@Override
		public void mouseReleased( MouseEvent evt) {
			try {
				if (mBtnMove.isSelected()) {
					LeafQuest leaf = (LeafQuest) evt.getSource();
					moveDrag( leaf, evt);
					moveEnd( leaf, evt);
				}
			}
			catch (ClassCastException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}

		private void moveBegin( LeafQuest leaf, MouseEvent evt) {
			mOldX = leaf.getX() + evt.getX();
			mOldY = leaf.getY() + evt.getY();
		}

		private void moveDrag( LeafQuest leaf, MouseEvent evt) {
			int dx = leaf.getX() + evt.getX() - mOldX;
			int dy = leaf.getY() + evt.getY() - mOldY;
			FQuest quest = leaf.getQuest();
			int x = ZOOM * quest.getX() + dx;
			int y = ZOOM * quest.getY() + dy;
			leaf.setLocation( x, y);
			moveLines( quest, dx, dy);
		}

		private void moveEnd( LeafQuest leaf, MouseEvent evt) {
			int dx = leaf.getX() + evt.getX() - mOldX;
			int dy = leaf.getY() + evt.getY() - mOldY;
			FQuest quest = leaf.getQuest();
			quest.mX.mValue += dx / ZOOM;
			quest.mY.mValue += dy / ZOOM;
		}

		private void moveLines( FQuest quest, int dx, int dy) {
			for (Component cc : mLeaf.getComponents()) {
				if (cc instanceof LeafLine) {
					LeafLine ll = (LeafLine) cc;
					FQuest from = ll.getFrom();
					FQuest to = ll.getTo();
					if (Utils.equals( quest, from)) {
						int x1 = AEntity.ZOOM * from.getCenterX() + dx;
						int y1 = AEntity.ZOOM * from.getCenterY() + dy;
						int x2 = AEntity.ZOOM * to.getCenterX();
						int y2 = AEntity.ZOOM * to.getCenterY();
						ll.init( x1, y1, x2, y2);
					}
					if (Utils.equals( quest, to)) {
						int x1 = AEntity.ZOOM * from.getCenterX();
						int y1 = AEntity.ZOOM * from.getCenterY();
						int x2 = AEntity.ZOOM * to.getCenterX() + dx;
						int y2 = AEntity.ZOOM * to.getCenterY() + dy;
						ll.init( x1, y1, x2, y2);
					}
				}
			}
		}
	}

	private final class LeafQuestHandler implements IClickListener {
		@Override
		public void onDoubleClick( MouseEvent evt) {
//			updateName();
		}

		@Override
		public void onSingleClick( MouseEvent evt) {
			updateActive( evt);
		}
	}

	private static class LineFactory extends AHQMWorker<Object, Object> {
		private EntityQuestSet mEntity;
		private FQuestSet mSet;

		private LineFactory( FQuestSet qs, EntityQuestSet leaf) {
			mSet = qs;
			mEntity = leaf;
		}

		public static void set( FQuestSet qs, EntityQuestSet entity) {
			LineFactory worker = new LineFactory( qs, entity);
			qs.mParentCategory.mParentHQM.forEachQuest( worker, null);
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			if (Utils.equals( quest.mQuestSet, mSet)) {
				for (FQuest req : quest.mRequirements) {
					if (req != null && Utils.equals( req.mQuestSet, quest.mQuestSet)) {
						mEntity.addLine( req, quest);
					}
				}
			}
			return null;
		}
	}

	private final class MoveQuestAction extends ABundleAction {
		private static final long serialVersionUID = -3157466864218507113L;

		public MoveQuestAction() {
			super( "entity.move");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			updateBtnMove();
		}
	}

	private static class QuestFactory extends AHQMWorker<Object, Object> {
		private EntityQuestSet mEntity;
		private FQuestSet mSet;

		private QuestFactory( FQuestSet qs, EntityQuestSet entity) {
			mSet = qs;
			mEntity = entity;
		}

		public static void set( FQuestSet qs, EntityQuestSet leaf) {
			QuestFactory worker = new QuestFactory( qs, leaf);
			qs.mParentCategory.mParentHQM.forEachQuest( worker, null);
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			if (Utils.equals( quest.mQuestSet, mSet)) {
				mEntity.addQuest( quest);
			}
			return null;
		}
	}
}
