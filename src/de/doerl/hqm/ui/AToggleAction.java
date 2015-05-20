package de.doerl.hqm.ui;

public abstract class AToggleAction extends ABundleAction {
	private static final long serialVersionUID = 5512356409334370478L;

	public AToggleAction( String name) {
		super( name);
		putValue( SELECTED_KEY, Boolean.FALSE);
	}

	public boolean isSelected() {
		return Boolean.TRUE.equals( getValue( SELECTED_KEY));
	}

	public void setSelected( boolean value) {
		putValue( SELECTED_KEY, Boolean.valueOf( value));
	}
}
