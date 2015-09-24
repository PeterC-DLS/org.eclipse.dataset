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
 * Interface for dataset of Objects // PRIM_TYPE
 */
public interface ObjectDataset extends Dataset { // CLASS_TYPE

	/**
	 * This is a typed version of {@link #getBuffer()}
	 * @return data buffer as linear array
	 */
	Object[] getData(); // PRIM_TYPE

	/**
	 * Get a value from an absolute index of the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @return value
	 */
	Object getAbs(int index); // PRIM_TYPE

	/**
	 * Set a value at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @param val
	 *            new value
	 */
	void setAbs(int index, Object val); // PRIM_TYPE

	/**
	 * @param i
	 * @return item in given position
	 */
	Object get(int i); // PRIM_TYPE

	/**
	 * @param i
	 * @param j
	 * @return item in given position
	 */
	Object get(int i, int j); // PRIM_TYPE

	/**
	 * @param pos
	 * @return item in given position
	 */
	Object get(int... pos); // PRIM_TYPE

	/**
	 * Sets the value at a particular point to the passed value. The dataset must be 1D
	 *
	 * @param value
	 * @param i
	 */
	void setItem(Object value, int i); // PRIM_TYPE

	/**
	 * Sets the value at a particular point to the passed value. The dataset must be 2D
	 *
	 * @param value
	 * @param i
	 * @param j
	 */
	void setItem(Object value, int i, int j); // PRIM_TYPE

	/**
	 * Sets the value at a particular point to the passed value
	 *
	 * @param value
	 * @param pos
	 */
	void setItem(Object value, int... pos); // PRIM_TYPE

	@Override
	ObjectDataset synchronizedCopy();

	@Override
	ObjectDataset getView();

	@Override
	ObjectDataset squeezeEnds();

	@Override
	ObjectDataset squeeze();

	@Override
	ObjectDataset squeeze(boolean onlyFromEnds);

	@Override
	ObjectDataset clone();

	@Override
	ObjectDataset reshape(int... shape);

	@Override
	ObjectDataset getTransposedView(int... axes);

	@Override
	ObjectDataset transpose(int... axes);

	@Override
	ObjectDataset swapAxes(int axis1, int axis2);

	@Override
	ObjectDataset flatten();

	@Override
	ObjectDataset fill(Object obj);

	@Override
	ObjectDataset sort(Integer axis);

	@Override
	ObjectDataset iadd(Object b);

	@Override
	ObjectDataset isubtract(Object b);

	@Override
	ObjectDataset imultiply(Object b);

	@Override
	ObjectDataset idivide(Object b);

	@Override
	ObjectDataset ifloor();

	@Override
	ObjectDataset iremainder(Object b);

	@Override
	ObjectDataset ipower(Object b);
}
