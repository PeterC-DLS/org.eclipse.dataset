/*-
 * Copyright 2015 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dataset.dense;

/**
 * Interface for dataset of booleans // PRIM_TYPE
 */
public interface BooleanDataset extends Dataset { // CLASS_TYPE

	/**
	 * This is a typed version of {@link #getBuffer()}
	 * @return data buffer as linear array
	 */
	boolean[] getData(); // PRIM_TYPE

	/**
	 * Get a value from an absolute index of the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @return value
	 */
	boolean getAbs(int index); // PRIM_TYPE

	/**
	 * Set a value at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @param val
	 *            new value
	 */
	void setAbs(int index, boolean val); // PRIM_TYPE

	/**
	 * @param i
	 * @return item in given position
	 */
	boolean get(int i); // PRIM_TYPE

	/**
	 * @param i
	 * @param j
	 * @return item in given position
	 */
	boolean get(int i, int j); // PRIM_TYPE

	/**
	 * @param pos
	 * @return item in given position
	 */
	boolean get(int... pos); // PRIM_TYPE

	/**
	 * Sets the value at a particular point to the passed value. The dataset must be 1D
	 *
	 * @param value
	 * @param i
	 */
	void setItem(boolean value, int i); // PRIM_TYPE

	/**
	 * Sets the value at a particular point to the passed value. The dataset must be 2D
	 *
	 * @param value
	 * @param i
	 * @param j
	 */
	void setItem(boolean value, int i, int j); // PRIM_TYPE

	/**
	 * Sets the value at a particular point to the passed value
	 *
	 * @param value
	 * @param pos
	 */
	void setItem(boolean value, int... pos); // PRIM_TYPE

	@Override
	BooleanDataset synchronizedCopy();

	@Override
	BooleanDataset getView();

	@Override
	BooleanDataset squeezeEnds();

	@Override
	BooleanDataset squeeze();

	@Override
	BooleanDataset squeeze(boolean onlyFromEnds);

	@Override
	BooleanDataset clone();

	@Override
	BooleanDataset reshape(int... shape);

	@Override
	BooleanDataset getTransposedView(int... axes);

	@Override
	BooleanDataset transpose(int... axes);

	@Override
	BooleanDataset swapAxes(int axis1, int axis2);

	@Override
	BooleanDataset flatten();

	@Override
	BooleanDataset fill(Object obj);

	@Override
	BooleanDataset sort(Integer axis);

	@Override
	BooleanDataset iadd(Object b);

	@Override
	BooleanDataset isubtract(Object b);

	@Override
	BooleanDataset imultiply(Object b);

	@Override
	BooleanDataset idivide(Object b);

	@Override
	BooleanDataset ifloor();

	@Override
	BooleanDataset iremainder(Object b);

	@Override
	BooleanDataset ipower(Object b);
}
