package de.doerl.hqm.view;

import java.awt.Component;
import java.awt.Cursor;
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
import javax.swing.SwingConstants;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FItemStack;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.base.dispatch.QuestSetOfName;
import de.doerl.hqm.controller.EditController;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.quest.DataBitHelper;
import de.doerl.hqm.ui.ABundleAction;
import de.doerl.hqm.ui.AToggleAction;
import de.doerl.hqm.ui.WarnDialogs;
import de.doerl.hqm.utils.ResourceManager;
import de.doerl.hqm.utils.Utils;
import de.doerl.hqm.view.LeafQuest.Type;

public class EntityQuestSet extends AEntity<FQuestSet> {
	private static final long serialVersionUID = 4427035968994904913L;
	private static final Logger LOGGER = Logger.getLogger( EntityQuestSet.class.getName());
	private QuestFactory mQuestWorker = new QuestFactory();
	private LineFactory mLineWorker = new LineFactory();
	private FQuestSet mSet;
	private LeafAbsolute mLeaf = new LeafAbsolute();
	private ABundleAction mNameAction = new NameAction();
	private AToggleAction mGroupAdd = new GroupAddAction();
	private AToggleAction mGroupMove = new GroupMoveAction();
	private AToggleAction mGroupLink = new GroupLinkAction();
	private AToggleAction mBigAction = new BigAction();
	private AToggleAction mGridAction = new GridAction();
	private ABundleAction mSetAction = new SetAction();
	private ABundleAction mRepeatAction = new RepeatAction();
	private ABundleAction mTriggerAction = new TriggerAction();
	private ABundleAction mIconAction = new IconAction();
	private ABundleAction mDeleteAction = new DeleteAction();
	private ABundleAction mMoveUpAction = new MoveUpAction();
	private ABundleAction mMoveDownAction = new MoveDownAction();
	private MouseAdapter mLeafQuestHandler = new LeafQuestHandler();
	private MouseAdapter mLeafMouseHandler = new LeafMouseHandler();
	private volatile LeafQuest mActiv;

	public EntityQuestSet( FQuestSet set, EditController ctrl) {
		super( ctrl, new GridLayout( 1, 1));
		mSet = set;
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
		mTool.add( mIconAction);
		mTool.addSeparator();
		mTool.add( mMoveUpAction);
		mTool.add( mMoveDownAction);
		mTool.add( mDeleteAction);
		mTool.addSeparator();
		mTool.add( mGridAction);
		mTool.addSeparator();
		mLeaf.addMouseListener( new AddHandler());
		updateGroup( false);
		updateActions( false);
		updateMoveActions();
		selectGroupNothing();
	}

	private void activeUpdate( Type type) {
		if (mActiv != null) {
			FQuest quest = mActiv.getQuest();
			boolean link = false;
			for (FQuest req : quest.mRequirements) {
				if (Utils.different( quest.getParent(), req.getParent())) {
					link = true;
				}
				LeafQuest lq = getLeafQuest( req);
				if (lq != null) {
					lq.update( type == Type.BASE ? Type.PREF : Type.NORM);
				}
			}
			for (FQuest req : quest.mPosts) {
				if (Utils.different( quest.getParent(), req.getParent())) {
					link = true;
				}
				LeafQuest lq = getLeafQuest( req);
				if (lq != null) {
					lq.update( type == Type.BASE ? Type.POST : Type.NORM);
				}
			}
			if (type == Type.BASE) {
				mActiv.update( link ? Type.LINK : type);
			}
			else {
				mActiv.update( type);
			}
		}
	}

	private void activRemove() {
		updateCurrent( false);
		activeUpdate( Type.NORM);
		mActiv = null;
		updateGroup( false);
		updateActions( false);
		updateMoveActions();
		selectGroupNothing();
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
					LeafQuest newActiv = getLeafQuest( old);
					updateActive( newActiv, true);
				}
			}
		}
		catch (ClassCastException ex) {
			Utils.logThrows( LOGGER, Level.WARNING, ex);
		}
	}

	@Override
	public void baseModified( ModelEvent event) {
		try {
			ABase base = event.mBase;
			if (mSet.equals( base.getParent()) && (mActiv == null || Utils.different( base, mActiv.getQuest()))) {
				LeafQuest newActiv = getLeafQuest( (FQuest) base);
				updateActive( newActiv, true);
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
			if (req != null && Utils.equals( req.getParent(), quest.getParent())) {
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
				if (Utils.equals( lq.getQuest().getParent(), mSet)) {
					removeLeafQuest( lq);
				}
			}
		}
	}

	private void selectGroupAdd() {
		setCursor( Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR));
		mGroupAdd.setSelected( true);
		mGroupMove.setSelected( false);
		mGroupLink.setSelected( false);
		updateActions( false);
	}

	private void selectGroupLink() {
		setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR));
		mGroupAdd.setSelected( false);
		mGroupMove.setSelected( false);
		mGroupLink.setSelected( true);
		updateActions( false);
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
		mRepeatAction.setEnabled( value);
		mTriggerAction.setEnabled( value);
		mIconAction.setEnabled( value);
		mDeleteAction.setEnabled( value);
	}

	private void updateActive( LeafQuest activ, boolean enableQuest) {
		updateCurrent( false);
		updateActive0( activ, enableQuest);
		updateCurrent( true);
	}

	private void updateActive0( LeafQuest activ, boolean enableQuest) {
		activeUpdate( Type.NORM);
		mActiv = activ;
		activeUpdate( Type.BASE);
		updateGroup( true);
		updateActions( enableQuest);
		mBigAction.setSelected( activ.getQuest().mBig);
		updateMoveActions();
	}

	private void updateCurrent( boolean info) {
		if (mActiv != null) {
			FQuest quest = mActiv.getQuest();
			quest.setInformation( info);
			mCtrl.fireModified( quest);
		}
	}

	private void updateGroup( boolean value) {
		mGroupAdd.setEnabled( true);
		mGroupMove.setEnabled( value);
		mGroupLink.setEnabled( value);
	}

	private void updateMoveActions() {
		if (mActiv == null) {
			mMoveUpAction.setEnabled( false);
			mMoveDownAction.setEnabled( false);
		}
		else {
			FQuest quest = mActiv.getQuest();
			mMoveUpAction.setEnabled( !quest.isFirst());
			mMoveDownAction.setEnabled( !quest.isLast());
		}
	}

	private final class AddHandler extends MouseAdapter {
		@Override
		public void mouseClicked( MouseEvent evt) {
			if (mGroupAdd.isSelected()) {
				String result = DialogTextField.update( "Unnamed", mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
				if (result != null) {
					int x = mLeaf.stepX( evt.getX()) / AEntity.ZOOM - ResourceManager.getW5( false);
					int y = mLeaf.stepY( evt.getY()) / AEntity.ZOOM - ResourceManager.getH5( false);
					FQuest quest = mCtrl.questCreate( mSet, result, x, y);
					quest.mDescr = "Unnamed quest";
					createLeafQuest( quest);
					updateActive( getLeafQuest( quest), false);
					mCtrl.fireAdded( quest);
				}
			}
			else {
				activRemove();
			}
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
				mActiv.update( Type.BASE);
				mCtrl.fireChanged( quest);
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
				FQuest quest = mActiv.getQuest();
				activRemove();
				mCtrl.questDelete( quest);
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
				selectGroupAdd();
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
				selectGroupLink();
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
					mActiv.update( Type.BASE);
					mCtrl.fireChanged( quest);
				}
			}
		}
	}

	private final class LeafMouseHandler extends MouseAdapter {
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
					FQuest from = ll.getFrom();
					FQuest to = ll.getTo();
					if (Utils.equals( quest, from)) {
						int x1 = x + ZOOM * ResourceManager.getW5( quest.mBig);
						int y1 = y + ZOOM * ResourceManager.getH5( quest.mBig);
						int x2 = ZOOM * to.getCenterX();
						int y2 = ZOOM * to.getCenterY();
						ll.updateBounds( x1, y1, x2, y2);
					}
					if (Utils.equals( quest, to)) {
						int x1 = ZOOM * from.getCenterX();
						int y1 = ZOOM * from.getCenterY();
						int x2 = x + ZOOM * ResourceManager.getW5( quest.mBig);
						int y2 = y + ZOOM * ResourceManager.getH5( quest.mBig);
						ll.updateBounds( x1, y1, x2, y2);
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
						FQuest other = dst.getQuest();
						Vector<FQuest> prefs = quest.mRequirements;
						Vector<FQuest> posts = other.mPosts;
						if (prefs.contains( other)) {
							dst.update( Type.NORM);
							removeLeafLine( other, quest);
							posts.remove( quest);
							prefs.remove( other);
						}
						else {
							prefs.add( other);
							if (!posts.contains( quest)) {
								posts.add( quest);
							}
							dst.update( Type.PREF);
							createLeafLine( other, quest);
						}
						mCtrl.fireChanged( quest);
						mCtrl.fireChanged( other);
					}
				}
				else if (mActiv != null && Utils.equals( mActiv, dst)) {
					selectGroupMove();
				}
				else {
					updateActive( dst, true);
					selectGroupNothing();
				}
			}
			catch (ClassCastException ex) {
				Utils.logThrows( LOGGER, Level.WARNING, ex);
			}
		}
	}

	private class LineFactory extends AHQMWorker<Object, Object> {
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
				String result = DialogTextField.update( quest.mName, mCtrl.getFrame(), DataBitHelper.QUEST_NAME_LENGTH);
				if (result != null) {
					quest.mName = result;
					mCtrl.fireChanged( quest);
				}
			}
		}
	}

	private class QuestFactory extends AHQMWorker<Object, Object> {
		@Override
		public Object forQuest( FQuest quest, Object p) {
			createLeafQuest( quest);
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
			arr.add( set.mName);
			return null;
		}
	}

	public class RepeatAction extends ABundleAction {
		private static final long serialVersionUID = -7789181879988427447L;

		public RepeatAction() {
			super( "entity.set.repeat");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			if (mActiv != null) {
				FQuest quest = mActiv.getQuest();
				if (DialogRepeat.update( quest.mRepeatInfo, mCtrl.getFrame())) {
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
				Vector<String> names = QuestSetNames.get( hqm);
				String result = DialogListNames.update( names, quest.getParent().mName, mCtrl.getFrame());
				if (result != null) {
					FQuestSet set = QuestSetOfName.get( hqm, result);
					if (set != null && Utils.different( quest.getParent(), set)) {
						activRemove();
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
					mCtrl.fireChanged( quest);
				}
			}
		}
	}
}
