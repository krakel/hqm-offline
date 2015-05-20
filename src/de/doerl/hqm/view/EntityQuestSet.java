package de.doerl.hqm.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;

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
	private FQuestSet mSet;
	private JPanel mLeaf = new LeafAbsolute();
	private JToolBar mTool = EditFrame.createToolBar();
	private ClickHandler mHandler = new ClickHandler();
	private AddQuestAction mAddAction = new AddQuestAction();
	private MoveQuestAction mMoveAction = new MoveQuestAction();
	private DeleteQuestAction mDeleteAction = new DeleteQuestAction();
	private JToggleButton mBtnAdd = createToggleButton( mAddAction);
	private JToggleButton mBtnMove = createToggleButton( mMoveAction);
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
		mLeaf.addMouseListener( mHandler);
		mHandler.addClickListener( new IClickListener() {
			@Override
			public void onDoubleClick( MouseEvent evt) {
//				updateName();
			}

			@Override
			public void onSingleClick( MouseEvent evt) {
				updateActive( evt);
			}
		});
		mBtnAdd.addItemListener( new ItemListener() {
			@Override
			public void itemStateChanged( ItemEvent evt) {
				updateAddQuest();
			}
		});
	}

	private void addLine( FQuest from, FQuest to) {
		mLeaf.add( new LeafLine( from, to));
	}

	private void addQuest( FQuest quest) {
		mLeaf.add( new LeafQuest( quest), 0);
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

	private void updateActive( MouseEvent evt) {
		Object src = evt.getSource();
		if (src instanceof LeafQuest) {
			mActiv = (LeafQuest) src;
		}
		else if (src instanceof LeafLine) {
		}
		else {
			FQuest quest = mSet.mParentCategory.mParentHQM.createQuest( "");
			quest.mQuestSet = mSet;
			quest.mX.mValue = evt.getX();
			quest.mY.mValue = evt.getY();
		}
	}

	private void updateAddQuest() {
		boolean enabled = !mBtnAdd.isSelected();
		mMoveAction.setEnabled( enabled);
		mDeleteAction.setEnabled( enabled);
	}

	private void updateDeleteQuest() {
		WarnDialogs.warnMissing( mView);
	}

	private void updateMoveQuest() {
		boolean enabled = !mBtnMove.isSelected();
		mAddAction.setEnabled( enabled);
		mDeleteAction.setEnabled( enabled);
	}

	private class AddQuestAction extends ABundleAction {
		private static final long serialVersionUID = -8667850457674712679L;

		public AddQuestAction() {
			super( "entity.add");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
//			updateAddQuest();
		}
	}

	private class DeleteQuestAction extends ABundleAction {
		private static final long serialVersionUID = -433615690858042140L;

		public DeleteQuestAction() {
			super( "entity.delete");
			setEnabled( false);
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			updateDeleteQuest();
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
					if (req != null && Utils.equals( quest.mQuestSet, req.mQuestSet)) {
						mEntity.addLine( quest, req);
					}
				}
			}
			return null;
		}
	}

	private class MoveQuestAction extends ABundleAction {
		private static final long serialVersionUID = -3157466864218507113L;

		public MoveQuestAction() {
			super( "entity.move");
		}

		@Override
		public void actionPerformed( ActionEvent evt) {
			updateMoveQuest();
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
