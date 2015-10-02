/*-
 *******************************************************************************
 * Copyright (c) 2015 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Peter Chang - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.dataset.dense;

import org.eclipse.dataset.IMonitor;
import org.eclipse.dataset.Slice;
import org.eclipse.dataset.SliceND;

public interface GenericDataset<T extends GenericDataset<?>> extends Dataset {


	/**
	 * This is a <b>synchronized</b> version of the clone method
	 * 
	 * @return a copy of dataset
	 */
	@Override
	public T synchronizedCopy();

	/**
	 * @return whole view of dataset (i.e. data buffer is shared)
	 */
	@Override
	public T getView();

	@Override
	public T squeezeEnds();

	@Override
	public T squeeze();

	@Override
	public T squeeze(boolean onlyFromEnds);

	@Override
	public T clone();

	/**
	 * Returns new dataset with new shape but old data if possible, otherwise a copy is made
	 * 
	 * @param shape
	 *            new shape
	 */
	@Override
	public T reshape(int... shape);

//	/**
//	 * @return real part of dataset as new dataset
//	 */
//	@Override
//	public T real();

//	/**
//	 * @return view of real part of dataset
//	 */
//	@Override
//	public T realView();

	@Override
	public T getTransposedView(int... axes);

	/**
	 * See {@link #getTransposedView}
	 * @return remapped copy of data
	 */
	@Override
	public T transpose(int... axes);

	/**
	 * Swap two axes in dataset
	 * 
	 * @param axis1
	 * @param axis2
	 * @return swapped view of dataset
	 */
	@Override
	public T swapAxes(int axis1, int axis2);

	/**
	 * Flatten shape
	 * 
	 * @return a flattened dataset which is a view if dataset is contiguous otherwise is a copy
	 */
	@Override
	public T flatten();

	/**
	 * Get unique items
	 * @return a sorted dataset of unique items
	 */
	@Override
	public T getUniqueItems();

	/**
	 * This is modelled after the NumPy get item with a condition specified by a boolean dataset
	 *
	 * @param selection
	 *            a boolean dataset of same shape to use for selecting items
	 * @return The new selected dataset
	 */
	@Override
	public T getByBoolean(Dataset selection);

	/**
	 * This is modelled after the NumPy set item with a condition specified by a boolean dataset
	 *
	 * @param obj
	 *            specifies the object used to set the selected items
	 * @param selection
	 *            a boolean dataset of same shape to use for selecting items
	 * 
	 * @return The dataset with modified content
	 */
	@Override
	public T setByBoolean(Object obj, Dataset selection);

	/**
	 * This is modelled after the NumPy get item with an index dataset
	 *
	 * @param index
	 *            an integer dataset
	 * @return The new selected dataset by indices
	 */
	@Override
	public T getBy1DIndex(IntegerDataset index);

	/**
	 * This is modelled after the NumPy get item with an array of indexing objects
	 *
	 * @param indexes
	 *            an array of integer dataset, boolean dataset, slices or null entries (same as
	 *            full slices)
	 * @return The new selected dataset by index
	 */
	@Override
	public T getByIndexes(Object... indexes);

	/**
	 * This is modelled after the NumPy set item with an index dataset
	 *
	 * @param obj
	 *            specifies the object used to set the selected items
	 * @param index
	 *            an integer dataset
	 * 
	 * @return The dataset with modified content
	 */
	@Override
	public T setBy1DIndex(Object obj, Dataset index);

	/**
	 * This is modelled after the NumPy set item with an array of indexing objects
	 *
	 * @param obj
	 *            specifies the object used to set the selected items
	 * @param indexes
	 *            an array of integer dataset, boolean dataset, slices or null entries (same as
	 *            full slices)
	 * 
	 * @return The dataset with modified content
	 */
	@Override
	public T setByIndexes(Object obj, Object... indexes);

	/**
	 * Fill dataset with given object
	 * 
	 * @param obj
	 * @return filled dataset with each item being equal to the given object
	 */
	@Override
	public T fill(Object obj);

	@Override
	public T sort(Integer axis);

	@Override
	public T getSlice(int[] start, int[] stop, int[] step);

	@Override
	public T getSlice(IMonitor mon, int[] start, int[] stop, int[] step);

	@Override
	public T getSlice(Slice... slice);

	@Override
	public T getSlice(IMonitor mon, Slice... slice);

	@Override
	public T getSlice(SliceND slice);

	@Override
	public T getSlice(IMonitor mon, SliceND slice);

	@Override
	public T getSliceView(int[] start, int[] stop, int[] step);

	@Override
	public T getSliceView(Slice... slice);

	@Override
	public T getSliceView(SliceND slice);

	/**
	 * This is modelled after the NumPy array slice
	 *
	 * @param obj
	 *            specifies the object used to set the specified slice
	 * @param start
	 *            specifies the starting indexes
	 * @param stop
	 *            specifies the stopping indexes (nb, these are <b>not</b> included in the slice)
	 * @param step
	 *            specifies the steps in the slice
	 * 
	 * @return The dataset with the sliced set to object
	 */
	@Override
	public T setSlice(Object obj, int[] start, int[] stop, int[] step);

	/**
	 * This is modelled after the NumPy array slice
	 * 
	 * @param obj
	 * @param slice
	 */
	@Override
	public T setSlice(Object obj, Slice... slice);

	/**
	 * This is modelled after the NumPy array slice
	 * 
	 * @param obj
	 * @param slice
	 */
	@Override
	public T setSlice(Object obj, SliceND slice);

	/**
	 * @param obj
	 *            specifies the object used to set the specified slice
	 * @param iterator
	 *            specifies the slice iterator
	 * 
	 * @return The dataset with the sliced set to object
	 */
	@Override
	public T setSlice(Object obj, IndexIterator iterator);

	/**
	 * In-place addition with object o
	 * 
	 * @param o
	 * @return sum dataset
	 */
	@Override
	public T iadd(Object o);

	/**
	 * In-place subtraction with object o
	 * 
	 * @param o
	 * @return difference dataset
	 */
	@Override
	public T isubtract(Object o);

	/**
	 * In-place multiplication with object o
	 * 
	 * @param o
	 * @return product dataset
	 */
	@Override
	public T imultiply(Object o);

	/**
	 * In-place division with object o
	 * 
	 * @param o
	 * @return dividend dataset
	 */
	@Override
	public T idivide(Object o);

	/**
	 * In-place floor division with object o
	 * 
	 * @param o
	 * @return dividend dataset
	 */
	@Override
	public T ifloorDivide(Object o);

	/**
	 * In-place remainder
	 * 
	 * @return remaindered dataset
	 */
	@Override
	public T iremainder(Object o);

	/**
	 * In-place floor
	 * 
	 * @return floored dataset
	 */
	@Override
	public T ifloor();

	/**
	 * In-place raise to power of object o
	 * 
	 * @param o
	 * @return raised dataset
	 */
	@Override
	public T ipower(Object o);

	/**
	 * See {@link #max(boolean ignoreNaNs, int axis)} with ignoreNaNs = false
	 * @param axis
	 * @return maxima along axis in dataset
	 */
	@Override
	public T max(int axis);

	/**
	 * @param ignoreNaNs if true, ignore NaNs
	 * @param axis
	 * @return maxima along axis in dataset
	 */
	@Override
	public T max(boolean ignoreNaNs, int axis);

	/**
	 * See {@link #min(boolean ignoreNaNs, int axis)} with ignoreNaNs = false
	 * @param axis
	 * @return minima along axis in dataset
	 */
	@Override
	public T min(int axis);

	/**
	 * @param ignoreNaNs if true, ignore NaNs
	 * @param axis
	 * @return minima along axis in dataset
	 */
	@Override
	public T min(boolean ignoreNaNs, int axis);

	/**
	 * @param axis
	 * @return peak-to-peak dataset, the difference of maxima and minima of dataset along axis
	 */
	@Override
	public T peakToPeak(int axis);
}
