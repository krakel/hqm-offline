package de.doerl.hqm.ui.tree;

import de.doerl.hqm.base.ACategory;
import de.doerl.hqm.base.ANamed;

class SetNode extends ANode {
	private ACategory<? extends ANamed> mCat;

	public SetNode( ACategory<? extends ANamed> cat) {
		mCat = cat;
	}

	@Override
	public ACategory<? extends ANamed> getBase() {
		return mCat;
	}

	@Override
	public String toString() {
		return String.valueOf( mCat.getElementTyp().getToken());
	}
}
