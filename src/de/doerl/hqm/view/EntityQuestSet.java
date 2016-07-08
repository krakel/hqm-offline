package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.IndexOf;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.AToggleAction;
import de.doerl.hqm.ui.LinkType;
import de.doerl.hqm.ui.WarnDialogs;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.dispatch.QuestSetOfName;
import de.doerl.hqm.view.leafs.LeafAbsolute;
import de.doerl.hqm.view.leafs.LeafLine;
import de.doerl.hqm.view.leafs.LeafQuest;

class EntityQuestSet extends AEntity<FQuestSet> {
	private static final long serialVersionUID = 4427035968994904913L;
	private static final Logger LOGGER = Logger.getLogger( EntityQuestSet.class.getName());
	private QuestFactory mQuestWorker = new QuestFactory();
	private LineFactory mLineWorker = new LineFactory();
	private FQuestSet mSet;
	private EditView mView;
	private LeafAbsolute mLeaf = new LeafAbsolute();
	private ABundleAction mNameAction = new NameAction();
	private AToggleAction mGroupAdd = new GroupAddAction();
	private AToggleAction mGroupMove = new GroupMoveAction();
	private AToggleAction mGroupLink = new GroupLinkAction();
	private AToggleAction mBigAction = new BigAction();
	private AToggleAction mGridAction = new GridAction();
	private ABundleAction mSetAction = new SetAction();
	private ABundleAction mCountAction = new CountAction();
	private ABundleAction mRepeatAction = new RepeatAction();
	private ABundleAction mTriggerAction = new TriggerAction();
	private ABundleAction mIconAction = new IconAction();
	private ABundleAction mDeleteAction = new DeleteAction();
	private ABundleAction mMoveUpAction = new MoveUpAction();
	private ABundleAction mMoveDownAction = new MoveDownAction();
	private MouseAdapter mQuestClickHandler = new QuestClickHandler();
	private MouseAdapter mQuestMoveHandler = new QuestMoveHandler();
	private volatile LeafQuest mActiv;
	private DialogTextField mEdit;

	public EntityQuestSet( FQuestSet set, EditView view) {
		super( view.getController(), new GridLayout( 1, 1));
		mSet = set;
		mView = view;
		mEdit = new DialogTextField( mCtrl.getFrame());
		add( mLeaf);
		mSet.forEachQuest( mQuestWorker, null);
		mSet.forEachQuest( mLineWorker, null);
		mTool.add( createToggleButton( mGroupAdd));
		mTool.add( createToggleButton( mGroupMove));
		mTool.add( createToggleButton( mGroupLink));
		mTool.addSeparator();
		mTool.add( mNameAction);
		mTool.add( createToggleButton( mBigAction));
		mTool.add( mSetAction);
		mTool.add( mRepeatAction);
		mTool.add( mTriggerAction);
		mTool.add( mCountAction);
		mTool.add( mIconAction);
		mTool.addSeparator();
		mTool.add( mMoveUpAction);
		mTool.add( mMoveDownAction);
		mTool.add( mDeleteAction);
		mTool.addSeparator();
		mTool.add( mGridAction);
		mTool.addSeparator();
		mLeaf.addMouseListener( new LeafAddHandler());
		updateCtrls( false, false);
		selectGroupNothing();
	}

	void activeClear() {
		if (mActiv != null) {
			LeafQuest old = mActiv;
			mActiv = null;
			mView.updateClear( this, old);
		}
	}

	void activeLeaf( FQuest base) {
		LeafQuest lq = getLeafQuest( base);
		if (lq != null) {
			SwingUtilities.invokeLater( new ActivateLeaf( lq));
		}
	}

	private void activeSet( LeafQuest activ, boolean enableQuest) {
		if (activ != null) {
			activ.update( activ.getQuest().containExt() ? LinkType.LINK : LinkType.BASE);
			mBigAction.setSelected( activ.getQuest().mBig);
			updateDepend( activ.getQuest());
		}
		else {
			mBigAction.setSelected( false);
		}
		updateCtrls( true, enableQuest);
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	@Override
	public void baseAdded( ModelEvent event) {
		try {
			ABase base = event.mBase;
			if (mSet.equals( base.getParent())) {
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
				set.forEachQuest( mQuestWorker, null);
				set.forEachQuest( mLineWorker, null);
				if (mActiv != null) {
					FQuest old = mActiv.getQuest();
					linkedChange( getLeafQuest( old), true);
				}
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
			if (base instanceof FQuest) {
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

	@Override
	public void baseTreeChange( ModelEvent event) {
	}

	private LeafLine createLeafLine( FQuest from, FQuest to) {
		LeafLine ll = getLeafLine( from, to);
		if (ll == null) {
			ll = new LeafLine( from, to);
			mLeaf.add( ll);
			ll.repaint();
		}
		return ll;
	}

	private void createLeafLines( FQuest quest) {
		for (FQuest req : quest.mRequirements) {
			if (req != null && Utils.equals( req.getParent(), mSet)) {
				createLeafLine( req, quest);
			}
		}
	}

	private LeafQuest createLeafQuest( FQuest quest) {
		LeafQuest leaf = getLeafQuest( quest);
		if (leaf == null) {
			leaf = new LeafQuest( quest);
			mLeaf.add( leaf, 0);
			leaf.update( LinkType.NORM);
			leaf.addMouseListener( mQuestClickHandler);
			leaf.addMouseListener( mQuestMoveHandler);
			leaf.addMouseMotionListener( mQuestMoveHandler);
		}
		return leaf;
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

	private LeafLine getLeafLine( FQuest from, FQuest to) {
		for (Component cc : mLeaf.getComponents()) {
			if (cc instanceof LeafLine) {
				LeafLine ll = (LeafLine) cc;
				if (ll.match( from, to)) {
					return ll;
				}
			}
		}
		return null;
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

	boolean handleClickFor( LeafQuest current, MouseEvent evt) {
		if (mActiv != null && mGroupLink.isSelected() && Utils.different( mActiv, current)) {
			if (evt.isShiftDown()) {
				if (notLoopRequire( mActiv.getQuest(), current.getQuest())) {
					FQuest quest = mActiv.getQuest();
					FQuest other = current.getQuest();
					if (quest.mPosts.contains( other)) {
						if (mSet.equals( other.getParent())) {
							removeLeafLine( quest, other);
						}
						quest.mPosts.remove( other);
						other.mRequirements.remove( quest);
						current.update( LinkType.NORM);
					}
					else if (!quest.mRequirements.contains( other)) {
						quest.mPosts.add( other);
						if (!other.mRequirements.contains( quest)) {
							other.mRequirements.add( quest);
						}
						if (mSet.equals( other.getParent())) {
							createLeafLine( quest, other);
						}
						current.update( LinkType.POST);
					}
					mCtrl.fireChanged( quest);
					mCtrl.fireChanged( other);
				}
			}
			else {
				if (notLoopPost( mActiv.getQuest(), current.getQuest())) {
					FQuest quest = mActiv.getQuest();
					FQuest other = current.getQuest();
					if (quest.mRequirements.contains( other)) {
						if (mSet.equals( other.getParent())) {
							removeLeafLine( other, quest);
						}
						quest.mRequirements.remove( other);
						other.mPosts.remove( quest);
						current.update( LinkType.NORM);
					}
					else if (!quest.mPosts.contains( other)) {
						quest.mRequirements.add( other);
						if (!other.mPosts.contains( quest)) {
							other.mPosts.add( quest);
						}
						if (mSet.equals( other.getParent())) {
							createLeafLine( other, quest);
						}
						current.update( LinkType.PREF);
					}
					mCtrl.fireChanged( quest);
					mCtrl.fireChanged( other);
				}
			}
			return false;
		}
		else if (mActiv != null && Utils.equals( mActiv, current)) {
			selectGroupMove();
			return false;
		}
		else {
			return true;
		}
	}

	private void linkedChange( LeafQuest activ, boolean enableQuest) {
		mView.updateActiveSetClear();
		mActiv = activ;
		if (activ != null) {
			mView.updateLinked( this, activ);
		}
		activeSet( activ, enableQuest);
	}

	private void linkedRemove() {
		mView.updateActiveSetClear();
		updateCtrls( false, false);
		selectGroupNothing();
	}

	private boolean notLoopPost( FQuest src, FQuest quest) {
		for (FQuest post : src.mPosts) {
			if (Utils.equals( post, quest)) {
				WarnDialogs.warnLoop( mCtrl.getFrame());
				return false;
			}
			if (!notLoopPost( post, quest)) {
				return false;
			}
		}
		return true;
	}

	private boolean notLoopRequire( FQuest dst, FQuest quest) {
		for (FQuest req : dst.mRequirements) {
			if (Utils.equals( req, quest)) {
				WarnDialogs.warnLoop( mCtrl.getFrame());
				return false;
			}
			if (!notLoopRequire( req, quest)) {
				return false;
			}
		}
		return true;
	}

	private void removeLeafLine( FQuest from, FQuest to) {
		for (Component cc : mLeaf.getComponents()) {
			if (cc instanceof LeafLine) {
				LeafLine ll = (LeafLine) cc;
				if (ll.match( from, to)) {
					ll.setVisible( false);
					mLeaf.remove( ll);
					break;
				}
			}
		}
	}

	private void removeLeafLines( FQuest quest) {
		for (Component cc : mLeaf.getComponents()) {
			if (cc instanceof LeafLine) {
				LeafLine ll = (LeafLine) cc;
				if (ll.match( quest)) {
					ll.setVisible( false);
					mLeaf.remove( ll);
				}
			}
		}
	}

	private void removeLeafQuest( LeafQuest lq) {
		mLeaf.remove( lq);
		lq.removeMouseListener( mQuestClickHandler);
		lq.removeMouseListener( mQuestMoveHandler);
		lq.removeMouseMotionListener( mQuestMoveHandler);
		lq.setVisible( false);
		removeLeafLines( lq.getQuest());
		if (Utils.equals( mActiv, lq)) {
			mActiv.setVisible( false);
			linkedRemove();
		}
	}

	private void removeMissingQuests() {
		for (Component cc : mLeaf.getComponents()) {
			if (cc instanceof LeafQuest) {
				LeafQuest lq = (LeafQuest) cc;
				if (IndexOf.getQuest( lq.getQuest()) < 0) {
					removeLeafQuest( lq);
				}
			}
		}
	}

	private void selectGroupMove() {
		setCursor( Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR));
		mGroupAdd.setSelected( false);
		mGroupMove.setSelected( true);
		mGroupLink.setSelected( false);
		updateActions( false);
	}

	private void selectGroupNothing() {
		setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR));
		mGroupAdd.setSelected( false);
		mGroupMove.setSelected( false);
		mGroupLink.setSelected( false);
	}

	private void updateActions( boolean value) {
		mNameAction.setEnabled( value);
		mBigAction.setEnabled( value);
		mSetAction.setEnabled( value);
		mCountAction.setEnabled( value);
		mRepeatAction.setEnabled( value);
		mTriggerAction.setEnabled( value);
		mIconAction.setEnabled( value);
		mDeleteAction.setEnabled( value);
	}

	private void updateCtrls( boolean enableGroups, boolean enableQuest) {
		mGroupAdd.setEnabled( true);
		mGroupMove.setEnabled( enableGroups);
		mGroupLink.setEnabled( enableGroups);
		if (mActiv == null) {
			mMoveUpAction.setEnabled( false);
			mMoveDownAction.setEnabled( false);
		}
		else {
			FQuest quest = mActiv.getQuest();
			mMoveUpAction.setEnabled( !quest.isFirst());
			mMoveDownAction.setEnabled( !quest.isLast());
		}
		updateActions( enableQuest);
	}

	private void updateDepend( FQuest quest) {
		for (FQuest post : quest.mPosts) {
			if (mSet.equals( post.getParent())) {
				updateDepend( post);
				updateLeaf( post, null);
			}
		}
	}

	void updateLeaf( FQuest quest, LinkType type) {
		LeafQuest lq = getLeafQuest( quest);
		if (lq != null) {
			lq.update( type);
		}
	}

	private final class ActivateLeaf implements Runnable {
		private LeafQuest mCurrent;

		private ActivateLeaf( LeafQuest current) {
			mCurrent = current;
		}

		@Override
		public void run() {
			linkedChange( mCurrent, true);
			selectGroupNothing();
		}
	}

	private final class BigAction extends AToggleAction {
		private static final long serialVersionUID = -3157466864218507113L;

		public BigAction() {
			super( "entity.set.big");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				FQuest quest = mActiv.getQuest();
				quest.mBig = !quest.mBig;
				mBigAction.setSelected( quest.mBig);
				mActiv.update( LinkType.BASE);
				mCtrl.fireChanged( quest);
			}
		}
	}

	private final class CountAction extends ABundleAction {
		private static final long serialVersionUID = -5452726397504503732L;

		public CountAction() {
			super( "entity.set.count");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				FQuest quest = mActiv.getQuest();
				if (DialogCount.update( quest, mCtrl.getFrame())) {
					activeSet( getLeafQuest( quest), true);
					mCtrl.fireChanged( quest);
				}
			}
		}
	}

	private final class DeleteAction extends ABundleAction {
		private static final long serialVersionUID = -433615690858042140L;

		public DeleteAction() {
			super( "entity.set.delete");
			setEnabled( false);
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null && WarnDialogs.askDelete( mCtrl.getFrame())) {
				mActiv.setVisible( false);
				FQuest old = mActiv.getQuest();
				linkedRemove();
				mCtrl.questDelete( old);
			}
		}
	}

	private final class GridAction extends AToggleAction {
		private static final long serialVersionUID = 4609401482942817956L;

		public GridAction() {
			super( "entity.set.grid");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			boolean sel = mGridAction.isSelected();
			mLeaf.setGrid( sel);
			mLeaf.repaint();
			mGridAction.setSelected( !sel);
		}
	}

	private final class GroupAddAction extends AToggleAction {
		private static final long serialVersionUID = -8667850457674712679L;

		public GroupAddAction() {
			super( "entity.set.add");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mGroupAdd.isSelected()) {
				setCursor( Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR));
				mGroupMove.setSelected( false);
				mGroupLink.setSelected( false);
				updateActions( false);
			}
			else {
				setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR));
				updateActions( mActiv != null);
			}
		}
	}

	private final class GroupLinkAction extends AToggleAction {
		private static final long serialVersionUID = 4085273836792762289L;

		public GroupLinkAction() {
			super( "entity.set.link");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mGroupLink.isSelected()) {
				setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR));
				mGroupAdd.setSelected( false);
				mGroupMove.setSelected( false);
				updateActions( false);
			}
			else {
				setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR));
				updateActions( mActiv != null);
			}
		}
	}

	private final class GroupMoveAction extends AToggleAction {
		private static final long serialVersionUID = -3157466864218507113L;

		public GroupMoveAction() {
			super( "entity.set.move");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mGroupMove.isSelected()) {
				selectGroupMove();
			}
			else {
				setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR));
				updateActions( mActiv != null);
			}
		}
	}

	private final class IconAction extends ABundleAction {
		private static final long serialVersionUID = -4857945141280262728L;

		public IconAction() {
			super( "entity.set.icon");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				FQuest quest = mActiv.getQuest();
				FItemStack result = DialogStack.update( quest.mIcon, mCtrl.getFrame());
				if (result != null) {
					quest.mIcon = result;
					mActiv.update( LinkType.BASE);
					mCtrl.fireChanged( quest);
				}
			}
		}
	}

	private final class LeafAddHandler extends MouseAdapter {
		@Override
		public void mouseClicked( MouseEvent evt) {
			if (mGroupAdd.isSelected()) {
				String result = mEdit.change( "Unnamed", DataBitHelper.QUEST_NAME_LENGTH);
				if (result != null) {
					int x = mLeaf.stepX( evt.getX()) / AEntity.ZOOM - ResourceManager.getW5( false);
					int y = mLeaf.stepY( evt.getY()) / AEntity.ZOOM - ResourceManager.getH5( false);
					FQuest quest = mCtrl.questCreate( mSet, result, x, y);
					quest.setDescr( "Unnamed quest");
					linkedChange( createLeafQuest( quest), false);
					mCtrl.fireAdded( quest);
				}
			}
			else {
				linkedRemove();
			}
		}
	}

	private final class LineFactory extends AHQMWorker<Object, Object> {
		@Override
		public Object forQuest( FQuest quest, Object p) {
			createLeafLines( quest);
			return null;
		}
	}

	private final class MoveDownAction extends ABundleAction {
		private static final long serialVersionUID = -132595603876180464L;

		public MoveDownAction() {
			super( "entity.set.moveDown");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				mActiv.getQuest().moveDown();
				mCtrl.fireChanged( mActiv.getQuest().getParent());
			}
		}
	}

	private final class MoveUpAction extends ABundleAction {
		private static final long serialVersionUID = 176343661597363424L;

		public MoveUpAction() {
			super( "entity.set.moveUp");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				mActiv.getQuest().moveUp();
				mCtrl.fireChanged( mActiv.getQuest().getParent());
			}
		}
	}

	private final class NameAction extends ABundleAction {
		private static final long serialVersionUID = -4857945141280262728L;

		public NameAction() {
			super( "entity.set.title");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				FQuest quest = mActiv.getQuest();
				String result = mEdit.change( quest.getName(), DataBitHelper.QUEST_NAME_LENGTH);
				if (result != null) {
					quest.setName( result);
					mCtrl.fireChanged( quest);
				}
			}
		}
	}

	private final class QuestClickHandler extends MouseAdapter {
		@Override
		public void mouseClicked( MouseEvent evt) {
			try {
				LeafQuest current = (LeafQuest) evt.getSource();
				if (mView.handleClickFor( current, evt)) {
					SwingUtilities.invokeLater( new ActivateLeaf( current));
				}
			}
			catch (ClassCastException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
	}

	private final class QuestFactory extends AHQMWorker<Object, Object> {
		@Override
		public Object forQuest( FQuest quest, Object p) {
			createLeafQuest( quest);
			return null;
		}
	}

	private final class QuestMoveHandler extends MouseAdapter {
		private int mOldX, mOldY;

		@Override
		public void mouseDragged( MouseEvent evt) {
			try {
				if (mGroupMove.isSelected()) {
					LeafQuest lq = (LeafQuest) evt.getSource();
					moveDrag( lq, evt);
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
					LeafQuest lq = (LeafQuest) evt.getSource();
					moveBegin( lq, evt);
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
					LeafQuest lq = (LeafQuest) evt.getSource();
					moveEnd( lq, evt);
				}
			}
			catch (ClassCastException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}

		private void moveBegin( LeafQuest lq, MouseEvent evt) {
			mOldX = lq.getX() + evt.getX();
			mOldY = lq.getY() + evt.getY();
		}

		private void moveDrag( LeafQuest lq, MouseEvent evt) {
			FQuest quest = lq.getQuest();
			int x = mLeaf.stepX( ZOOM * quest.mX + lq.getX() + evt.getX() - mOldX);
			int y = mLeaf.stepY( ZOOM * quest.mY + lq.getY() + evt.getY() - mOldY);
			x /= ZOOM;
			x *= ZOOM;
			y /= ZOOM;
			y *= ZOOM;
			moveLines( quest, x, y);
			lq.setLocation( x, y);
		}

		private void moveEnd( LeafQuest lq, MouseEvent evt) {
			FQuest quest = lq.getQuest();
			int x = mLeaf.stepX( ZOOM * quest.mX + lq.getX() + evt.getX() - mOldX);
			int y = mLeaf.stepY( ZOOM * quest.mY + lq.getY() + evt.getY() - mOldY);
			x /= ZOOM;
			x *= ZOOM;
			y /= ZOOM;
			y *= ZOOM;
			moveLines( quest, x, y);
			quest.mX = x / ZOOM;
			quest.mY = y / ZOOM;
			lq.updateLocation();
			mCtrl.fireChanged( quest);
		}

		private void moveLines( FQuest quest, int x, int y) {
			for (Component cc : mLeaf.getComponents()) {
				if (cc instanceof LeafLine) {
					LeafLine ll = (LeafLine) cc;
					ll.updateBounds( quest, x, y);
				}
			}
		}
	}

	private static class QuestSetNames extends AHQMWorker<Object, ArrayList<String>> {
		private static final QuestSetNames WORKER = new QuestSetNames();

		private QuestSetNames() {
		}

		public static ArrayList<String> get( FHqm hqm) {
			ArrayList<String> arr = new ArrayList<>();
			hqm.mQuestSetCat.forEachMember( WORKER, arr);
			return arr;
		}

		@Override
		public Object forQuestSet( FQuestSet set, ArrayList<String> arr) {
			arr.add( set.getName());
			return null;
		}
	}

	private final class RepeatAction extends ABundleAction {
		private static final long serialVersionUID = -7789181879988427447L;

		public RepeatAction() {
			super( "entity.set.repeat");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				FQuest quest = mActiv.getQuest();
				if (DialogRepeat.update( quest.mRepeatInfo, mCtrl.getFrame())) {
					activeSet( getLeafQuest( quest), true);
					mCtrl.fireChanged( quest);
				}
			}
		}
	}

	private final class SetAction extends ABundleAction {
		private static final long serialVersionUID = -2650359366196052333L;

		public SetAction() {
			super( "entity.set.down");
			setEnabled( false);
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				FQuest quest = mActiv.getQuest();
				FHqm hqm = quest.getHqm();
				ArrayList<String> names = QuestSetNames.get( hqm);
				String result = DialogListNames.update( names, mSet.getName(), mCtrl.getFrame());
				if (result != null) {
					FQuestSet set = QuestSetOfName.get( hqm, result);
					if (set != null && Utils.different( mSet, set)) {
						mCtrl.questMoveTo( quest, set);
					}
				}
			}
		}
	}

	private final class TriggerAction extends ABundleAction {
		private static final long serialVersionUID = 5835438582658183075L;

		public TriggerAction() {
			super( "entity.set.trigger");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				FQuest quest = mActiv.getQuest();
				if (DialogTrigger.update( quest, mCtrl.getFrame())) {
					activeSet( getLeafQuest( quest), true);
					mCtrl.fireChanged( quest);
				}
			}
		}
	}
}
