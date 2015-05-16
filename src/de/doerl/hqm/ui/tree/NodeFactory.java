package de.doerl.hqm.ui.tree;

import javax.swing.tree.MutableTreeNode;

import de.doerl.hqm.base.ABase;
import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.ANamed;
import de.doerl.hqm.base.FHqm;
import de.doerl.hqm.base.FQuest;
import de.doerl.hqm.base.dispatch.AHQMWorker;

public class NodeFactory extends AHQMWorker<MutableTreeNode, ElementTreeModel> {
	private static final NodeFactory WORKER = new NodeFactory();

	public NodeFactory() {
	}

	public static MutableTreeNode get( ABase base, ElementTreeModel model) {
		return base.accept( WORKER, model);
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
	protected MutableTreeNode doNamed( ANamed named, ElementTreeModel model) {
		MutableTreeNode node = model.getNode( named);
		if (node == null) {
			MutableTreeNode parent = named.getParent().accept( this, model);
			node = model.createNode( parent, new NamedNode( named));
		}
		return node;
	}

	@Override
	protected MutableTreeNode doSet( ACategory<? extends ANamed> cat, ElementTreeModel model) {
		MutableTreeNode node = model.getNode( cat);
		if (node == null) {
			MutableTreeNode parent = cat.getParent().accept( this, model);
			node = model.createNode( parent, new SetNode( cat));
		}
		return node;
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
		MutableTreeNode node = model.getNode( quest);
		if (node == null) {
			MutableTreeNode parent = quest.mSet.accept( this, model);
			node = model.createNode( parent, new NamedNode( quest));
		}
		return node;
	}
}
