package de.doerl.hqm.ui.tree;

import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.ANamed;

class CatNode extends ANode {
	private ACategory<? extends ANamed> mCat;

	public CatNode( ACategory<? extends ANamed> cat) {
		mCat = cat;
	}

	@Override
	public ACategory<? extends ANamed> getBase() {
		return mCat;
	}
}
