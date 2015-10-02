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
 * Interface for dataset of floats // PRIM_TYPE
 */
public interface FloatDataset extends GenericDataset<FloatDataset> { // CLASS_TYPE

	/**
	 * This is a typed version of {@link #getBuffer()}
	 * @return data buffer as linear array
	 */
	float[] getData(); // PRIM_TYPE

	/**
	 * Get a value from an absolute index of the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @return value
	 */
	float getAbs(int index); // PRIM_TYPE

	/**
	 * Set a value at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @param val
	 *            new value
	 */
	void setAbs(int index, float val); // PRIM_TYPE

	/**
	 * @param i
	 * @return item in given position
	 */
	float get(int i); // PRIM_TYPE

	/**
	 * @param i
	 * @param j
	 * @return item in given position
	 */
	float get(int i, int j); // PRIM_TYPE

	/**
	 * @param pos
	 * @return item in given position
	 */
	float get(int... pos); // PRIM_TYPE

	/**
	 * Sets the value at a particular point to the passed value. The dataset must be 1D
	 *
	 * @param value
	 * @param i
	 */
	void setItem(float value, int i); // PRIM_TYPE

	/**
	 * Sets the value at a particular point to the passed value. The dataset must be 2D
	 *
	 * @param value
	 * @param i
	 * @param j
	 */
	void setItem(float value, int i, int j); // PRIM_TYPE

	/**
	 * Sets the value at a particular point to the passed value
	 *
	 * @param value
	 * @param pos
	 */
	void setItem(float value, int... pos); // PRIM_TYPE
}
