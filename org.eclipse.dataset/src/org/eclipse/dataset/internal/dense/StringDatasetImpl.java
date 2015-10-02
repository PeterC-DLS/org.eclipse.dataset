/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Peter Chang - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.dataset.internal.dense;

import java.text.MessageFormat;

import org.eclipse.dataset.dense.Dataset;
import org.eclipse.dataset.dense.IndexIterator;
import org.eclipse.dataset.dense.SliceIterator;
import org.eclipse.dataset.dense.StringDataset;

/**
 * Extend dataset for objects
 */
public class StringDatasetImpl extends StringDatasetBaseImpl {
	// pin UID to base class
	private static final long serialVersionUID = Dataset.serialVersionUID;

	public StringDatasetImpl() {
		super();
	}

	/**
	 * Create a null-filled dataset of given shape
	 * @param shape
	 */
	public StringDatasetImpl(final int... shape) {
		super(shape);
	}

	/**
	 * Create a dataset using given data
	 * @param data
	 * @param shape
	 *            (can be null to create 1D dataset)
	 */
	public StringDatasetImpl(final String[] data, int... shape) {
		super(data, shape);
	}

	/**
	 * Copy a dataset
	 * @param dataset
	 */
	public StringDatasetImpl(final StringDataset dataset) {
		super(dataset);
	}

	/**
	 * Cast a dataset to this class type
	 * @param dataset
	 */
	public StringDatasetImpl(final Dataset dataset) {
		super(dataset);
	}

	@Override
	public StringDatasetImpl getView() {
		StringDatasetImpl view = new StringDatasetImpl();
		copyToView(this, view, true, true);
		view.data = data;
		return view;
	}

	@Override
	public StringDatasetImpl clone() {
		return new StringDatasetImpl(this);
	}

	@Override
	public StringDatasetImpl getSlice(SliceIterator siter) {
		StringDatasetImpl slice = new StringDatasetImpl();
		StringDatasetBaseImpl base = super.getSlice(siter);
		copyToView(base, slice, false, false);
		slice.setData();
		return slice;
	}

	/**
	 * Create a dataset from an object which could be a Java list, array (of arrays...)
	 * or Number. Ragged sequences or arrays are padded with zeros.
	 * 
	 * @param obj
	 * @return dataset with contents given by input
	 */
	public static StringDatasetImpl createFromObject(final Object obj) {
		StringDatasetBaseImpl result = StringDatasetBaseImpl.createFromObject(obj);
		StringDatasetImpl ds = new StringDatasetImpl(result.data, result.shape);
		if (result.shape.length == 0)
			ds.setShape(result.shape); // special case of single item 
		return ds;
	}

	/**
	 * @param shape
	 * @return a dataset filled with ones
	 */
	public static StringDatasetImpl ones(final int... shape) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	protected void calculateMaxMin(final boolean ignoreNaNs, final boolean ignoreInfs) {
		// override to skip max/min calculation for hash only
		IndexIterator iter = getIterator();
		double hash = 0;

		while (iter.hasNext()) {
			final int val = getStringAbs(iter.index).hashCode();
			hash = (hash * 19 + val) % Integer.MAX_VALUE;
		}

		int ihash = ((int) hash) * 19 + getDType() * 17 + getElementsPerItem();
		setStoredValue(storeName(ignoreNaNs, ignoreInfs, STORE_SHAPELESS_HASH), ihash);
	}

	@Override
	public boolean getElementBooleanAbs(int index) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public double getElementDoubleAbs(int index) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public long getElementLongAbs(int index) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public double getDouble(int i) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public double getDouble(int i, int j) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public double getDouble(int... pos) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public float getFloat(int i) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public float getFloat(int i, int j) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public float getFloat(int... pos) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public long getLong(int i) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public long getLong(int i, int j) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public long getLong(int... pos) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public int getInt(int i) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public int getInt(int i, int j) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public int getInt(int... pos) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public short getShort(int i) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public short getShort(int i, int j) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public short getShort(int... pos) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public byte getByte(int i) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public byte getByte(int i, int j) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public byte getByte(int... pos) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public boolean getBoolean(int i) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public boolean getBoolean(int i, int j) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public boolean getBoolean(int... pos) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public String getStringAbs(final int index) {
		return stringFormat instanceof MessageFormat ? stringFormat.format(data[index]) :
				String.format("%s", data[index]);
	}

	@Override
	public StringDatasetImpl getSlice(final int[] start, final int[] stop, final int[] step) {
		StringDatasetBaseImpl result = super.getSlice(start, stop, step);

		return new StringDatasetImpl(result.data, result.shape);
	}

	@Override
	public int[] minPos() {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public int[] maxPos() {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public boolean containsInfs() {
		return false;
	}

	@Override
	public boolean containsNans() {
		return false;
	}

	@Override
	public StringDatasetImpl iadd(Object o) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public StringDatasetImpl isubtract(Object o) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public StringDatasetImpl imultiply(Object o) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public StringDatasetImpl idivide(Object o) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public StringDatasetImpl iremainder(Object o) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public StringDatasetImpl ifloor() {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public StringDatasetImpl ipower(Object o) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}

	@Override
	public double residual(Object o) {
		throw new UnsupportedOperationException("Unsupported method for class");
	}
}
