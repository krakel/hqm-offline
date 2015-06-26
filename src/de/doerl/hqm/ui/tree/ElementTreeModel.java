package de.doerl.hqm.ui.tree;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.AMember;
import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.AQuestTask;
import de.doerl.hqm.base.FGroupTier;
import de.doerl.hqm.base.FGroupTierCat;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.FQuestSet;
import de.doerl.hqm.base.FQuestSetCat;
import de.doerl.hqm.base.dispatch.AHQMWorker;
import de.doerl.hqm.model.IModelListener;
import de.doerl.hqm.model.ModelEvent;
import de.doerl.hqm.utils.Utils;

public class ElementTreeModel extends DefaultTreeModel implements IModelListener {
	private static final long serialVersionUID = -3566772435718046914L;
	private static final Logger LOGGER = Logger.getLogger( ElementTreeModel.class.getName());

	public ElementTreeModel() {
		super( new DefaultMutableTreeNode( "root"), true);
	}

	@Override
	public void baseActivate( ModelEvent event) {
	}

	@Override
	public void baseAdded( ModelEvent event) {
		ABase base = event.mBase;
		if (base != null) {
			TreeFactory.get( base, this);
		}
	}

	@Override
	public void baseChanged( ModelEvent event) {
		ABase base = event.mBase;
		if (base != null) {
			ChangeFactory.get( base, this);
		}
	}

	@Override
	public void baseRemoved( ModelEvent event) {
		MutableTreeNode node = getNode( event.mBase);
		if (node != null) {
			removeNodeFromParent( node);
		}
	}

	MutableTreeNode createNode( MutableTreeNode parent, ANode elem) {
		MutableTreeNode node = new ElementTreeNode( elem);
		insertNodeInto( node, parent, parent.getChildCount());
		return node;
	}

	MutableTreeNode createNode( MutableTreeNode parent, ANode elem, boolean allowsChildren) {
		MutableTreeNode node = new ElementTreeNode( elem, allowsChildren);
		insertNodeInto( node, parent, parent.getChildCount());
		return node;
	}

	private DefaultMutableTreeNode getChild( MutableTreeNode parent, ABase base) {
		if (base != null) {
			for (int i = 0; i < parent.getChildCount(); ++i) {
				try {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt( i);
					ANode user = (ANode) node.getUserObject();
					if (Utils.match( base, user.getBase())) {
						return node;
					}
				}
				catch (ClassCastException ex) {
					Utils.logThrows( LOGGER, Level.WARNING, ex);
				}
			}
		}
		return null;
	}

	DefaultMutableTreeNode getNode( ABase base) {
		MutableTreeNode parent = getParentNode( base);
		if (parent != null && parent.getChildCount() > 0) {
			return getChild( parent, base);
		}
		return null;
	}

	private MutableTreeNode getParentNode( ABase base) {
		ABase parent = base != null ? base.getParent() : null;
		if (parent != null) {
			return getNode( parent);
		}
		else {
			return getRoot();
		}
	}

	@Override
	public DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode) super.getRoot();
	}

	private static class ChangeFactory extends AHQMWorker<Object, ElementTreeModel> {
		private static final ChangeFactory WORKER = new ChangeFactory();

		public ChangeFactory() {
		}

		public static void get( ABase base, ElementTreeModel model) {
			base.accept( WORKER, model);
		}

		@Override
		protected Object doBase( ABase base, ElementTreeModel model) {
			DefaultMutableTreeNode node = model.getNode( base);
			if (node != null) {
				model.fireTreeNodesChanged( this, node.getPath(), null, null);
			}
			return null;
		}

		@Override
		protected Object doCategory( ACategory<? extends ANamed> cat, ElementTreeModel model) {
			DefaultMutableTreeNode node = model.getNode( cat);
			if (node != null) {
				model.fireTreeNodesChanged( this, node.getPath(), null, null);
				node.removeAllChildren();
				TreeFactory.get( cat, model);
				model.fireTreeStructureChanged( this, node.getPath(), null, null);
			}
			return null;
		}

		@Override
		public Object forQuestSet( FQuestSet set, ElementTreeModel model) {
			DefaultMutableTreeNode node = model.getNode( set);
			if (node != null) {
				model.fireTreeNodesChanged( this, node.getPath(), null, null);
				node.removeAllChildren();
				TreeFactory.get( set, model);
				model.fireTreeStructureChanged( this, node.getPath(), null, null);
			}
			return null;
		}
	}

	private static class NodeFactory extends AHQMWorker<MutableTreeNode, ElementTreeModel> {
		private static final NodeFactory WORKER = new NodeFactory();

		public NodeFactory() {
		}

		public static void get( ABase base, ElementTreeModel model) {
			base.accept( WORKER, model);
		}

		@Override
		protected MutableTreeNode doBase( ABase base, ElementTreeModel model) {
			MutableTreeNode node = model.getNode( base);
			if (node == null) {
				MutableTreeNode parent = base.getParent().accept( this, model);
				node = model.createNode( parent, new BaseNode( base));
			}
			return node;
		}

		@Override
		protected MutableTreeNode doCategory( ACategory<? extends ANamed> cat, ElementTreeModel model) {
			return doCategory( cat, model, false);
		}

		private MutableTreeNode doCategory( ACategory<? extends ANamed> cat, ElementTreeModel model, boolean allowsChildren) {
			MutableTreeNode node = model.getNode( cat);
			if (node == null) {
				MutableTreeNode parent = cat.getParent().accept( this, model);
				node = model.createNode( parent, new CatNode( cat), allowsChildren);
			}
			return node;
		}

		@Override
		protected MutableTreeNode doMember( AMember member, ElementTreeModel model) {
			return doNamed( member, model, false);
		}

		@Override
		protected MutableTreeNode doNamed( ANamed named, ElementTreeModel model) {
			return doNamed( named, model, true);
		}

		private MutableTreeNode doNamed( ANamed named, ElementTreeModel model, boolean allowsChildren) {
			MutableTreeNode node = model.getNode( named);
			if (node == null) {
				MutableTreeNode parent = named.getParent().accept( this, model);
				node = model.createNode( parent, new NamedNode( named), allowsChildren);
			}
			return node;
		}

		@Override
		public MutableTreeNode forGroupTier( FGroupTier tier, ElementTreeModel model) {
			return doNamed( tier, model, false);
		}

		@Override
		public MutableTreeNode forGroupTierCat( FGroupTierCat cat, ElementTreeModel model) {
			return doCategory( cat, model, true);
		}

		@Override
		public MutableTreeNode forHQM( FHqm hqm, ElementTreeModel model) {
			MutableTreeNode node = model.getNode( hqm);
			if (node == null) {
				MutableTreeNode root = model.getRoot();
				node = model.createNode( root, new MainNode( hqm));
			}
			return node;
		}

		@Override
		public MutableTreeNode forQuest( FQuest quest, ElementTreeModel model) {
			return doNamed( quest, model, false);
		}

		@Override
		public MutableTreeNode forQuestSet( FQuestSet set, ElementTreeModel model) {
			return doNamed( set, model, true);
		}

		@Override
		public MutableTreeNode forQuestSetCat( FQuestSetCat cat, ElementTreeModel model) {
			return doCategory( cat, model, true);
		}
	}

	private static class TreeFactory extends AHQMWorker<Object, ElementTreeModel> {
		private static final TreeFactory WORKER = new TreeFactory();

		private TreeFactory() {
		}

		public static void get( ABase base, ElementTreeModel model) {
			base.accept( WORKER, model);
		}

		public static void get( ACategory<? extends ANamed> cat, ElementTreeModel model) {
			cat.forEachMember( WORKER, model);
		}

		@Override
		protected Object doCategory( ACategory<? extends ANamed> cat, ElementTreeModel model) {
			NodeFactory.get( cat, model);
			return null;
		}

		@Override
		protected Object doMember( AMember member, ElementTreeModel p) {
			return null;
		}

		@Override
		protected Object doTask( AQuestTask task, ElementTreeModel p) {
			return null;
		}

		@Override
		public Object forGroupTier( FGroupTier tier, ElementTreeModel model) {
			NodeFactory.get( tier, model);
			return null;
		}

		@Override
		public Object forGroupTierCat( FGroupTierCat cat, ElementTreeModel model) {
			NodeFactory.get( cat, model);
			cat.forEachMember( this, model);
			return null;
		}

		@Override
		public Object forHQM( FHqm hqm, ElementTreeModel model) {
			NodeFactory.get( hqm, model);
			hqm.mReputationCat.accept( this, model);
			hqm.mQuestSetCat.accept( this, model);
			hqm.mGroupTierCat.accept( this, model);
			return null;
		}

		@Override
		public Object forQuest( FQuest quest, ElementTreeModel model) {
			NodeFactory.get( quest, model);
			return null;
		}

		@Override
		public Object forQuestSet( FQuestSet set, ElementTreeModel model) {
			NodeFactory.get( set, model);
			set.forEachQuest( this, model);
			return null;
		}

		@Override
		public Object forQuestSetCat( FQuestSetCat cat, ElementTreeModel model) {
			NodeFactory.get( cat, model);
			cat.forEachMember( this, model);
			return null;
		}
	}
}
