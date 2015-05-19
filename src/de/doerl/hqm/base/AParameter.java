package de.doerl.hqm.base;

public abstract class AParameter extends ABase {
	public final ABase mParentNamed;

	AParameter( ABase parent) {
		mParentNamed = parent;
	}

	@Override
	public ABase getParent() {
		return mParentNamed;
	}
}
