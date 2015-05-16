package de.doerl.hqm.ui.tree;

import javax.swing.tree.DefaultMutableTreeNode;

class ElementTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 4557996058935640332L;
	protected boolean mEnabled;

	public ElementTreeNode( ANode element) {
		this( element, true, true);
	}

	public ElementTreeNode( ANode element, boolean allowsChildren) {
		this( element, allowsChildren, true);
	}

	public ElementTreeNode( ANode element, boolean allowsChildren, boolean enabled) {
		super( element, allowsChildren);
		mEnabled = enabled;
	}

	@Override
	public int getChildCount() {
		if (mEnabled) {
			return super.getChildCount();
		}
		return 0;
	}

	public ANode getElementObject() {
		return (ANode) getUserObject();
	}

	public boolean isEnabled() {
		return mEnabled;
	}

	public void setEnabled( boolean enabled) {
		mEnabled = enabled;
	}
}
