package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;
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
import de.doerl.hqm.ui.AToggleAction;
import de.doerl.hqm.ui.EditFrame;
import de.doerl.hqm.ui.WarnDialogs;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.LeafQuest.Type;

public class EntityQuestSet extends AEntity<FQuestSet> {
	private static final long serialVersionUID = 4427035968994904913L;
	private static final Logger LOGGER = Logger.getLogger( EntityQuestSet.class.getName());
	private FQuestSet mSet;
	private LeafAbsolute mLeaf = new LeafAbsolute();
	private JToolBar mTool = EditFrame.createToolBar();
	private AToggleAction mAddAction = new QuestAddAction();
	private AToggleAction mMoveAction = new QuestMoveAction();
	private AToggleAction mLinkAction = new QuestLinkAction();
	private AToggleAction mBigAction = new QuestBigAction();
	private ABundleAction mDeleteAction = new QuestDeleteAction();
	private LeafQuestHandler mLeafQuestHandler = new LeafQuestHandler();
	private LeafMouseHandler mLeafMouseHandler = new LeafMouseHandler();
	private HashMap<FQuest, LeafQuest> mMap = new HashMap<FQuest, LeafQuest>();
	private volatile LeafQuest mActiv;

	public EntityQuestSet( EditView view, FQuestSet set) {
		super( view, new GridLayout( 1, 1));
		mSet = set;
		add( mLeaf);
		LineFactory.set( set, this);
		QuestFactory.set( set, this);
		mTool.add( createToggleButton( mAddAction));
		mTool.add( createToggleButton( mMoveAction));
		mTool.add( createToggleButton( mLinkAction));
		mTool.addSeparator();
		mTool.add( createToggleButton( mBigAction));
		mTool.add( mDeleteAction);
		mTool.addSeparator();
		mLeaf.addClickListener( new LeafHandler());
		updateGroup( false);
		updateQuest( false);
		mAddAction.setSelected( true);
	}

	private void activeUpdate( Type type) {
		if (mActiv != null) {
			mActiv.update( type);
//			mActiv.revalidate();
//			mActiv.repaint();
			FQuest quest = mActiv.getQuest();
			for (FQuest req : quest.mRequirements) {
				LeafQuest lq = mMap.get( req);
				if (lq != null) {
					lq.update( type == Type.ACTIV ? Type.PREF : Type.NORM);
				}
			}
			for (FQuest req : quest.mPosts) {
				LeafQuest lq = mMap.get( req);
				if (lq != null) {
					lq.update( type == Type.ACTIV ? Type.POST : Type.NORM);
				}
			}
		}
	}

	private void activRemove() {
		activeUpdate( Type.NORM);
		mActiv = null;
		updateGroup( true);
		updateQuest( false);
		mBigAction.setSelected( false);
		mMoveAction.setSelected( false);
		mLinkAction.setSelected( false);
	}

	private void activSet( LeafQuest activ, boolean enableQuest) {
		activeUpdate( Type.NORM);
		mActiv = activ;
		activeUpdate( Type.ACTIV);
		updateGroup( true);
		updateQuest( enableQuest);
		mBigAction.setSelected( activ.getQuest().isBig());
	}

	private LeafLine addLine( FQuest req, FQuest quest) {
		LeafLine comp = new LeafLine( req, quest);
		mLeaf.add( comp);
		return comp;
	}

	private LeafQuest addQuest( FQuest quest) {
		LeafQuest comp = new LeafQuest( quest);
		mLeaf.add( comp, 0);
		mMap.put( quest, comp);
		comp.addClickListener( mLeafQuestHandler);
		comp.addMouseListener( mLeafMouseHandler);
		comp.addMouseMotionListener( mLeafMouseHandler);
		return comp;
	}

	@Override
	protected void createLeft( JPanel leaf) {
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

	private void removeAllLines( FQuest quest) {
		for (Component cc : mLeaf.getComponents()) {
			if (cc instanceof LeafLine) {
				LeafLine ll = (LeafLine) cc;
				if (Utils.equals( quest, ll.getFrom()) || Utils.equals( quest, ll.getTo())) {
					ll.setVisible( false);
					mLeaf.remove( ll);
				}
			}
		}
	}

	private void removeLine( FQuest req, FQuest quest) {
		for (Component cc : mLeaf.getComponents()) {
			if (cc instanceof LeafLine) {
				LeafLine ll = (LeafLine) cc;
				if (Utils.equals( req, ll.getFrom()) && Utils.equals( quest, ll.getTo())) {
					ll.setVisible( false);
					mLeaf.remove( ll);
				}
			}
		}
	}

	private void selectAdd() {
		mAddAction.setSelected( true);
		mMoveAction.setSelected( false);
		mLinkAction.setSelected( false);
		updateQuest( false);
	}

	private void selectLink() {
		mAddAction.setSelected( false);
		mMoveAction.setSelected( false);
		mLinkAction.setSelected( true);
		updateQuest( false);
	}

	private void selectMove() {
		mAddAction.setSelected( false);
		mMoveAction.setSelected( true);
		mLinkAction.setSelected( false);
		updateQuest( false);
	}

	private void updateGroup( boolean value) {
		mAddAction.setEnabled( true);
		mMoveAction.setEnabled( value);
		mLinkAction.setEnabled( value);
	}

	private void updateQuest( boolean value) {
		mBigAction.setEnabled( value);
		mDeleteAction.setEnabled( value);
	}

	private final class LeafHandler extends AClickListener {
		@Override
		public void onSingleClick( MouseEvent evt) {
			if (mAddAction.isSelected()) {
				FQuest quest = mSet.mParentCategory.mParentHQM.createQuest( "");
				quest.mQuestSet = mSet;
				quest.mX.mValue = evt.getX() / AEntity.ZOOM - quest.getW() / 2;
				quest.mY.mValue = evt.getY() / AEntity.ZOOM - quest.getH() / 2;
				activSet( addQuest( quest), false);
			}
			else {
				activRemove();
			}
		}
	}

	private final class LeafMouseHandler extends MouseAdapter {
		private int mOldX, mOldY;

		@Override
		public void mouseDragged( MouseEvent evt) {
			try {
				if (mMoveAction.isSelected()) {
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
				if (mMoveAction.isSelected()) {
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
				if (mMoveAction.isSelected()) {
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
			try {
				LeafQuest dst = (LeafQuest) evt.getSource();
				if (mActiv != null && mLinkAction.isSelected()) {
					if (Utils.different( mActiv, dst)) {
						FQuest quest = mActiv.getQuest();
						FQuest req = dst.getQuest();
						Vector<FQuest> prevs = quest.mRequirements;
						if (prevs.contains( req)) {
							prevs.remove( req);
							dst.update( Type.NORM);
							removeLine( req, quest);
						}
						else {
							prevs.add( req);
							dst.update( Type.PREF);
							LeafLine ll = addLine( req, quest);
							ll.repaint();
						}
					}
				}
				else if (mActiv != null && Utils.equals( mActiv, dst)) {
					selectMove();
				}
				else {
					activSet( dst, true);
					mAddAction.setSelected( false);
				}
			}
			catch (ClassCastException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
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

	private final class QuestAddAction extends AToggleAction {
		private static final long serialVersionUID = -8667850457674712679L;

		public QuestAddAction() {
			super( "entity.add");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mAddAction.isSelected()) {
				selectAdd();
			}
			else {
				updateQuest( mActiv != null);
			}
		}
	}

	private final class QuestBigAction extends AToggleAction {
		private static final long serialVersionUID = -3157466864218507113L;

		public QuestBigAction() {
			super( "entity.big");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				FQuest quest = mActiv.getQuest();
				quest.mBig.mValue = !quest.isBig();
				mBigAction.setSelected( quest.isBig());
				mActiv.update( Type.ACTIV);
			}
		}
	}

	private final class QuestDeleteAction extends ABundleAction {
		private static final long serialVersionUID = -433615690858042140L;

		public QuestDeleteAction() {
			super( "entity.delete");
			setEnabled( false);
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null && WarnDialogs.askDelete( mView)) {
				mActiv.setVisible( false);
				mLeaf.remove( mActiv);
				FQuest quest = mActiv.getQuest();
				mMap.remove( quest);
				removeAllLines( quest);
				quest.remove();
				QuestRemoveDepent.get( quest);
				activRemove();
			}
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

	private final class QuestLinkAction extends AToggleAction {
		private static final long serialVersionUID = 4085273836792762289L;

		public QuestLinkAction() {
			super( "entity.link");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mLinkAction.isSelected()) {
				selectLink();
			}
			else {
				updateQuest( mActiv != null);
			}
		}
	}

	private final class QuestMoveAction extends AToggleAction {
		private static final long serialVersionUID = -3157466864218507113L;

		public QuestMoveAction() {
			super( "entity.move");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mMoveAction.isSelected()) {
				selectMove();
			}
			else {
				updateQuest( mActiv != null);
			}
		}
	}
}
