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
 * Interface for compound dataset of floats // PRIM_TYPE
 */
public interface CompoundFloatDataset extends GenericCompoundDataset<CompoundFloatDataset> { // CLASS_TYPE

	/**
	 * This is a typed version of {@link #getBuffer()}
	 * @return data buffer as linear array
	 */
	float[] getData(); // PRIM_TYPE

	/**
	 * Get values at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @return values
	 */
	float[] getAbs(int index); // PRIM_TYPE

	/**
	 * Get values at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @param values
	 */
	void getAbs(int index, float[] values); // PRIM_TYPE

	/**
	 * Set values at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @param val
	 *            new values
	 */
	void setAbs(int index, float[] val); // PRIM_TYPE

	/**
	 * Set element value at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @param val
	 *            new value
	 */
	void setAbs(int index, float val); // PRIM_TYPE

	/**
	 * Set values at given position. The dataset must be 1D
	 *
	 * @param d
	 * @param i
	 */
	void setItem(float[] d, int i); // PRIM_TYPE

	/**
	 * Set values at given position. The dataset must be 1D
	 *
	 * @param d
	 * @param i
	 * @param j
	 */
	void setItem(float[] d, int i, int j); // PRIM_TYPE

	/**
	 * Set values at given position
	 *
	 * @param d
	 * @param pos
	 */
	void setItem(float[] d, int... pos); // PRIM_TYPE

	@Override
	public FloatDataset real(); // CLASS_TYPE

	@Override
	public FloatDataset realView(); // CLASS_TYPE

	@Override
	public FloatDataset getElements(int element); // CLASS_TYPE

	@Override
	public FloatDataset getElementsView(int element); // CLASS_TYPE

	@Override
	public FloatDataset asNonCompoundDataset(final boolean shareData); // CLASS_TYPE
}
