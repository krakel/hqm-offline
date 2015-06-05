package de.doerl.hqm.base;

public interface IElement {
	boolean isFirst();

	boolean isLast();

	void moveDown();

	void moveUp();

	void remove();
}
