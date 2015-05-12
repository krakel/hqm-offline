package de.doerl.hqm.base;

public abstract class AParameter extends ABase {
	public final ABase mParentNamed;
	private String mName;

	AParameter( ABase parent, String name) {
		mName = name;
		mParentNamed = parent;
	}

	public String getName() {
		return mName;
	}

	@Override
	public ABase getParent() {
		return mParentNamed;
	}
}
