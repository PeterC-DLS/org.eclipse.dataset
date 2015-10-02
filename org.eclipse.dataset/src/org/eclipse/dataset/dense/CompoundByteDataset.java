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
 * Interface for compound dataset of bytes // PRIM_TYPE
 */
public interface CompoundByteDataset extends GenericCompoundDataset<CompoundByteDataset> { // CLASS_TYPE

	/**
	 * This is a typed version of {@link #getBuffer()}
	 * @return data buffer as linear array
	 */
	byte[] getData(); // PRIM_TYPE

	/**
	 * Get values at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @return values
	 */
	byte[] getAbs(int index); // PRIM_TYPE

	/**
	 * Get values at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @param values
	 */
	void getAbs(int index, byte[] values); // PRIM_TYPE

	/**
	 * Set values at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @param val
	 *            new values
	 */
	void setAbs(int index, byte[] val); // PRIM_TYPE

	/**
	 * Set element value at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @param val
	 *            new value
	 */
	void setAbs(int index, byte val); // PRIM_TYPE

	/**
	 * Set values at given position. The dataset must be 1D
	 *
	 * @param d
	 * @param i
	 */
	void setItem(byte[] d, int i); // PRIM_TYPE

	/**
	 * Set values at given position. The dataset must be 1D
	 *
	 * @param d
	 * @param i
	 * @param j
	 */
	void setItem(byte[] d, int i, int j); // PRIM_TYPE

	/**
	 * Set values at given position
	 *
	 * @param d
	 * @param pos
	 */
	void setItem(byte[] d, int... pos); // PRIM_TYPE

	@Override
	public ByteDataset real(); // CLASS_TYPE

	@Override
	public ByteDataset realView(); // CLASS_TYPE

	@Override
	public ByteDataset getElements(int element); // CLASS_TYPE

	@Override
	public ByteDataset getElementsView(int element); // CLASS_TYPE

	@Override
	public ByteDataset asNonCompoundDataset(final boolean shareData); // CLASS_TYPE
}
