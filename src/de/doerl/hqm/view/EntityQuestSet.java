package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.QuestSetOfName;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.AToggleAction;
import de.doerl.hqm.ui.EditFrame;
import de.doerl.hqm.ui.WarnDialogs;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.LeafQuest.Type;

public class EntityQuestSet extends AEntity<FQuestSet> {
	private static final long serialVersionUID = 4427035968994904913L;
	private static final Logger LOGGER = Logger.getLogger( EntityQuestSet.class.getName());
	private FQuestSet mSet;
	private LeafAbsolute mLeaf = new LeafAbsolute();
	private JToolBar mTool = EditFrame.createToolBar();
	private AToggleAction mGroupAdd = new GroupAddAction();
	private AToggleAction mGroupMove = new GroupMoveAction();
	private AToggleAction mGroupLink = new GroupLinkAction();
	private AToggleAction mBigAction = new BigAction();
	private ABundleAction mSetAction = new SetAction();
	private ABundleAction mDeleteAction = new DeleteAction();
	private MouseAdapter mLeafQuestHandler = new LeafQuestHandler();
	private MouseAdapter mLeafMouseHandler = new LeafMouseHandler();
	private volatile LeafQuest mActiv;

	public EntityQuestSet( FQuestSet set, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 1));
		mSet = set;
		add( mLeaf);
		QuestFactory.add( mSet, this);
		LineFactory.add( mSet, this);
		mTool.add( createToggleButton( mGroupAdd));
		mTool.add( createToggleButton( mGroupMove));
		mTool.add( createToggleButton( mGroupLink));
		mTool.addSeparator();
		mTool.add( createToggleButton( mBigAction));
		mTool.add( mSetAction);
		mTool.add( mDeleteAction);
		mTool.addSeparator();
		mLeaf.addMouseListener( new AddHandler());
		updateGroup( false);
		updateQuest( false);
		mGroupAdd.setSelected( true);
	}

	private void activeUpdate( Type type) {
		if (mActiv != null) {
			mActiv.update( type);
			FQuest quest = mActiv.getQuest();
			for (FQuest req : quest.mRequirements) {
				LeafQuest lq = getLeafQuest( req);
				if (lq != null) {
					lq.update( type == Type.BASE ? Type.PREF : Type.NORM);
				}
			}
			for (FQuest req : quest.mPosts) {
				LeafQuest lq = getLeafQuest( req);
				if (lq != null) {
					lq.update( type == Type.BASE ? Type.POST : Type.NORM);
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
		mGroupMove.setSelected( false);
		mGroupLink.setSelected( false);
	}

	private void activSet( LeafQuest activ, boolean enableQuest) {
		activeUpdate( Type.NORM);
		mActiv = activ;
		activeUpdate( Type.BASE);
		updateGroup( true);
		updateQuest( enableQuest);
		mBigAction.setSelected( activ.getQuest().isBig());
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	@Override
	public void baseAdded( ModelEvent event) {
		try {
			ABase base = event.mBase;
			if (mSet.equals( base.getHierarchy())) {
				FQuest quest = (FQuest) base;
				createLeafQuest( quest);
				createLeafLines( quest);
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	@Override
	public void baseChanged( ModelEvent event) {
		try {
			ABase base = event.mBase;
			if (mSet.equals( base)) {
				FQuestSet set = (FQuestSet) base;
				removeMissingQuests();
				QuestFactory.add( set, this);
				LineFactory.add( set, this);
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	@Override
	public void baseRemoved( ModelEvent event) {
		try {
			ABase base = event.mBase;
			if (mSet.equals( base.getHierarchy())) {
				LeafQuest lq = getLeafQuest( (FQuest) base);
				if (lq != null) {
					removeLeafQuest( lq);
				}
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	private boolean containLeafLine( FQuest req, FQuest quest) {
		for (Component cc : mLeaf.getComponents()) {
			if (cc instanceof LeafLine) {
				LeafLine ll = (LeafLine) cc;
				if (Utils.equals( req, ll.getFrom()) && Utils.equals( quest, ll.getTo())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean containLeafQuest( FQuest quest) {
		for (Component cc : mLeaf.getComponents()) {
			if (cc instanceof LeafQuest) {
				LeafQuest lq = (LeafQuest) cc;
				if (Utils.equals( lq.getQuest(), quest)) {
					return true;
				}
			}
		}
		return false;
	}

	private void createLeafLine( FQuest req, FQuest quest) {
		if (!containLeafLine( req, quest)) {
			LeafLine ll = new LeafLine( req, quest);
			mLeaf.add( ll);
			ll.repaint();
		}
	}

	private void createLeafLines( FQuest quest) {
		for (FQuest req : quest.mRequirements) {
			if (req != null && Utils.equals( req.mQuestSet, quest.mQuestSet)) {
				createLeafLine( req, quest);
			}
		}
	}

	private void createLeafQuest( FQuest quest) {
		if (!containLeafQuest( quest)) {
			LeafQuest comp = new LeafQuest( quest);
			mLeaf.add( comp, 0);
			comp.update( Type.NORM);
			comp.addMouseListener( mLeafQuestHandler);
			comp.addMouseListener( mLeafMouseHandler);
			comp.addMouseMotionListener( mLeafMouseHandler);
		}
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

	private LeafQuest getLeafQuest( FQuest quest) {
		for (Component cc : mLeaf.getComponents()) {
			if (cc instanceof LeafQuest) {
				LeafQuest lq = (LeafQuest) cc;
				if (Utils.equals( lq.getQuest(), quest)) {
					return lq;
				}
			}
		}
		return null;
	}

	@Override
	public JToolBar getToolBar() {
		return mTool;
	}

	private void removeLeafLine( FQuest req, FQuest quest) {
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

	private void removeLeafLines( FQuest quest) {
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

	private void removeLeafQuest( LeafQuest lq) {
		mLeaf.remove( lq);
		lq.removeMouseListener( mLeafQuestHandler);
		lq.removeMouseListener( mLeafMouseHandler);
		lq.removeMouseMotionListener( mLeafMouseHandler);
		removeLeafLines( lq.getQuest());
		if (Utils.equals( mActiv, lq)) {
			mActiv.setVisible( false);
			activRemove();
		}
	}

	private void removeMissingQuests() {
		for (Component cc : mLeaf.getComponents()) {
			if (cc instanceof LeafQuest) {
				LeafQuest lq = (LeafQuest) cc;
				if (Utils.equals( lq.getQuest().mQuestSet, mSet)) {
					removeLeafQuest( lq);
				}
			}
		}
	}

	private void selectGroupAdd() {
		mGroupAdd.setSelected( true);
		mGroupMove.setSelected( false);
		mGroupLink.setSelected( false);
		updateQuest( false);
	}

	private void selectGroupLink() {
		mGroupAdd.setSelected( false);
		mGroupMove.setSelected( false);
		mGroupLink.setSelected( true);
		updateQuest( false);
	}

	private void selectGroupMove() {
		mGroupAdd.setSelected( false);
		mGroupMove.setSelected( true);
		mGroupLink.setSelected( false);
		updateQuest( false);
	}

	private void updateGroup( boolean value) {
		mGroupAdd.setEnabled( true);
		mGroupMove.setEnabled( value);
		mGroupLink.setEnabled( value);
	}

	private void updateQuest( boolean value) {
		mBigAction.setEnabled( value);
		mSetAction.setEnabled( value);
		mDeleteAction.setEnabled( value);
	}

	private final class AddHandler extends MouseAdapter {
		@Override
		public void mouseClicked( MouseEvent evt) {
			if (mGroupAdd.isSelected()) {
				int x = evt.getX() / AEntity.ZOOM - ResourceManager.getW( false) / 2;
				int y = evt.getY() / AEntity.ZOOM - ResourceManager.getH( false) / 2;
				FQuest quest = mCtrl.questCreate( mSet, "Unnamed", x, y);
				createLeafQuest( quest);
				activSet( getLeafQuest( quest), false);
				mCtrl.fireAdded( quest);
			}
			else {
				activRemove();
			}
		}
	}

	private final class BigAction extends AToggleAction {
		private static final long serialVersionUID = -3157466864218507113L;

		public BigAction() {
			super( "entity.big");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				FQuest quest = mActiv.getQuest();
				quest.mBig.mValue = !quest.isBig();
				mBigAction.setSelected( quest.isBig());
				mActiv.update( Type.BASE);
				mCtrl.fireChanged( quest);
			}
		}
	}

	private final class DeleteAction extends ABundleAction {
		private static final long serialVersionUID = -433615690858042140L;

		public DeleteAction() {
			super( "entity.delete");
			setEnabled( false);
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null && WarnDialogs.askDelete( mCtrl.getFrame())) {
				mActiv.setVisible( false);
				FQuest quest = mActiv.getQuest();
				mCtrl.questDelete( quest);
				mCtrl.fireRemoved( quest);
			}
		}
	}

	private final class GroupAddAction extends AToggleAction {
		private static final long serialVersionUID = -8667850457674712679L;

		public GroupAddAction() {
			super( "entity.add");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mGroupAdd.isSelected()) {
				selectGroupAdd();
			}
			else {
				updateQuest( mActiv != null);
			}
		}
	}

	private final class GroupLinkAction extends AToggleAction {
		private static final long serialVersionUID = 4085273836792762289L;

		public GroupLinkAction() {
			super( "entity.link");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mGroupLink.isSelected()) {
				selectGroupLink();
			}
			else {
				updateQuest( mActiv != null);
			}
		}
	}

	private final class GroupMoveAction extends AToggleAction {
		private static final long serialVersionUID = -3157466864218507113L;

		public GroupMoveAction() {
			super( "entity.move");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mGroupMove.isSelected()) {
				selectGroupMove();
			}
			else {
				updateQuest( mActiv != null);
			}
		}
	}

	private final class LeafMouseHandler extends MouseAdapter {
		private int mOldX, mOldY;

		@Override
		public void mouseDragged( MouseEvent evt) {
			try {
				if (mGroupMove.isSelected()) {
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
				if (mGroupMove.isSelected()) {
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
				if (mGroupMove.isSelected()) {
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
			mCtrl.fireChanged( quest);
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

	private final class LeafQuestHandler extends MouseAdapter {
		@Override
		public void mouseClicked( MouseEvent evt) {
			try {
				LeafQuest dst = (LeafQuest) evt.getSource();
				if (mActiv != null && mGroupLink.isSelected()) {
					if (Utils.different( mActiv, dst)) {
						FQuest quest = mActiv.getQuest();
						FQuest req = dst.getQuest();
						Vector<FQuest> prevs = quest.mRequirements;
						if (prevs.contains( req)) {
							dst.update( Type.NORM);
							removeLeafLine( req, quest);
							prevs.remove( req);
						}
						else {
							prevs.add( req);
							dst.update( Type.PREF);
							createLeafLine( req, quest);
						}
						mCtrl.fireChanged( quest);
						mCtrl.fireChanged( req);
					}
				}
				else if (mActiv != null && Utils.equals( mActiv, dst)) {
					selectGroupMove();
				}
				else {
					activSet( dst, true);
					mGroupAdd.setSelected( false);
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

		public static void add( FQuestSet qs, EntityQuestSet entity) {
			LineFactory worker = new LineFactory( qs, entity);
			qs.mParentCategory.mParentHQM.forEachQuest( worker, null);
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			if (Utils.equals( quest.mQuestSet, mSet)) {
				mEntity.createLeafLines( quest);
			}
			return null;
		}
	}

	private static class QuestFactory extends AHQMWorker<Object, Object> {
		private EntityQuestSet mEntity;
		private FQuestSet mSet;

		private QuestFactory( FQuestSet qs, EntityQuestSet entity) {
			mSet = qs;
			mEntity = entity;
		}

		public static void add( FQuestSet qs, EntityQuestSet leaf) {
			QuestFactory worker = new QuestFactory( qs, leaf);
			qs.mParentCategory.mParentHQM.forEachQuest( worker, null);
		}

		@Override
		public Object forQuest( FQuest quest, Object p) {
			if (Utils.equals( quest.mQuestSet, mSet)) {
				mEntity.createLeafQuest( quest);
			}
			return null;
		}
	}

	private static class QuestSetNames extends AHQMWorker<Object, Vector<String>> {
		private static final QuestSetNames WORKER = new QuestSetNames();

		private QuestSetNames() {
		}

		public static Vector<String> get( FHqm hqm) {
			Vector<String> arr = new Vector<>();
			hqm.mQuestSetCat.forEachMember( WORKER, arr);
			return arr;
		}

		@Override
		public Object forQuestSet( FQuestSet set, Vector<String> arr) {
			arr.add( set.mName.mValue);
			return null;
		}
	}

	private final class SetAction extends ABundleAction {
		private static final long serialVersionUID = -2650359366196052333L;

		public SetAction() {
			super( "entity.questSet");
			setEnabled( false);
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				FQuest quest = mActiv.getQuest();
				Vector<String> names = QuestSetNames.get( quest.mParentHQM);
				String result = DialogListNames.update( names, quest.mQuestSet.mName.mValue, mCtrl.getFrame());
				if (result != null) {
					FQuestSet set = QuestSetOfName.get( quest.mParentHQM, result);
					if (set != null && Utils.different( quest.mQuestSet, set)) {
						mCtrl.fireRemoved( quest);
						quest.mQuestSet = set;
						mCtrl.fireAdded( quest);
					}
				}
			}
		}
	}
}
