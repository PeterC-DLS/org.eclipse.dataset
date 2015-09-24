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
 * Interface for dataset of bytes // PRIM_TYPE
 */
public interface ByteDataset extends Dataset { // CLASS_TYPE

	/**
	 * This is a typed version of {@link #getBuffer()}
	 * @return data buffer as linear array
	 */
	byte[] getData(); // PRIM_TYPE

	/**
	 * Get a value from an absolute index of the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @return value
	 */
	byte getAbs(int index); // PRIM_TYPE

	/**
	 * Set a value at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @param val
	 *            new value
	 */
	void setAbs(int index, byte val); // PRIM_TYPE

	/**
	 * @param i
	 * @return item in given position
	 */
	byte get(int i); // PRIM_TYPE

	/**
	 * @param i
	 * @param j
	 * @return item in given position
	 */
	byte get(int i, int j); // PRIM_TYPE

	/**
	 * @param pos
	 * @return item in given position
	 */
	byte get(int... pos); // PRIM_TYPE

	/**
	 * Sets the value at a particular point to the passed value. The dataset must be 1D
	 *
	 * @param value
	 * @param i
	 */
	void setItem(byte value, int i); // PRIM_TYPE

	/**
	 * Sets the value at a particular point to the passed value. The dataset must be 2D
	 *
	 * @param value
	 * @param i
	 * @param j
	 */
	void setItem(byte value, int i, int j); // PRIM_TYPE

	/**
	 * Sets the value at a particular point to the passed value
	 *
	 * @param value
	 * @param pos
	 */
	void setItem(byte value, int... pos); // PRIM_TYPE

	@Override
	ByteDataset synchronizedCopy();

	@Override
	ByteDataset getView();

	@Override
	ByteDataset squeezeEnds();

	@Override
	ByteDataset squeeze();

	@Override
	ByteDataset squeeze(boolean onlyFromEnds);

	@Override
	ByteDataset clone();

	@Override
	ByteDataset reshape(int... shape);

	@Override
	ByteDataset getTransposedView(int... axes);

	@Override
	ByteDataset transpose(int... axes);

	@Override
	ByteDataset swapAxes(int axis1, int axis2);

	@Override
	ByteDataset flatten();

	@Override
	ByteDataset fill(Object obj);

	@Override
	ByteDataset sort(Integer axis);

	@Override
	ByteDataset iadd(Object b);

	@Override
	ByteDataset isubtract(Object b);

	@Override
	ByteDataset imultiply(Object b);

	@Override
	ByteDataset idivide(Object b);

	@Override
	ByteDataset ifloor();

	@Override
	ByteDataset iremainder(Object b);

	@Override
	ByteDataset ipower(Object b);
}
