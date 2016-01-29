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

// This is generated from DoubleDatasetImpl.java by fromdouble.py

package org.eclipse.dataset.internal.dense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.dataset.IDataset;
import org.eclipse.dataset.IDatasetIterator;
import org.eclipse.dataset.PositionIterator;
import org.eclipse.dataset.dense.BooleanIterator;
import org.eclipse.dataset.dense.BroadcastIterator;
import org.eclipse.dataset.dense.Dataset;
import org.eclipse.dataset.dense.DatasetFactory;
import org.eclipse.dataset.dense.DatasetUtils;
import org.eclipse.dataset.dense.BooleanDataset; // CLASS_TYPE
import org.eclipse.dataset.dense.DTypeUtils;
import org.eclipse.dataset.dense.IndexIterator;
import org.eclipse.dataset.dense.IntegerIterator;
import org.eclipse.dataset.dense.IntegersIterator;
import org.eclipse.dataset.dense.SliceIterator;


/**
 * Extend dataset for boolean values // PRIM_TYPE
 */
public class BooleanDatasetBaseImpl extends AbstractDataset<BooleanDatasetBaseImpl> implements BooleanDataset { // CLASS_TYPE
	// pin UID to base class
	private static final long serialVersionUID = Dataset.serialVersionUID;

	protected boolean[] data; // subclass alias // PRIM_TYPE

	@Override
	protected void setData() {
		data = (boolean[]) odata; // PRIM_TYPE
	}

	protected static boolean[] createArray(final long size) { // PRIM_TYPE
		boolean[] array = null; // PRIM_TYPE

		try {
			array = new boolean[(int) size]; // PRIM_TYPE
		} catch (OutOfMemoryError e) {
			logger.error("The size of the dataset ({}) that is being created is too large "
					+ "and there is not enough memory to hold it.", size);
			throw new OutOfMemoryError("The dimensions given are too large, and there is "
					+ "not enough memory available in the Java Virtual Machine");
		}
		return array;
	}

	@Override
	public int getDType() {
		return BOOL; // DATA_TYPE
	}

	public BooleanDatasetBaseImpl() {
	}

	/**
	 * Create a zero-filled dataset of given shape
	 * @param shape
	 */
	public BooleanDatasetBaseImpl(final int... shape) {
		if (shape.length == 1) {
			size = shape[0];
			if (size < 0) {
				throw new IllegalArgumentException("Negative component in shape is not allowed");
			}
		} else {
			size = DatasetUtils.calculateSize(shape);
		}
		this.shape = shape.clone();

		odata = data = createArray(size);
	}

	/**
	 * Create a dataset using given data
	 * @param data
	 * @param shape
	 *            (can be null to create 1D dataset)
	 */
	public BooleanDatasetBaseImpl(final boolean[] data, int... shape) { // PRIM_TYPE
		if (data == null) {
			throw new IllegalArgumentException("Data must not be null");
		}
		if (shape == null || shape.length == 0) {
			shape = new int[] { data.length };
		}
		size = DatasetUtils.calculateSize(shape);
		if (size != data.length) {
			throw new IllegalArgumentException(String.format("Shape %s is not compatible with size of data array, %d",
					Arrays.toString(shape), data.length));
		}
		this.shape = shape.clone();

		odata = this.data = data;
	}

	/**
	 * Copy a dataset
	 * @param dataset
	 */
	public BooleanDatasetBaseImpl(final BooleanDatasetBaseImpl dataset) {
		copyToView(dataset, this, true, true);
		if (dataset.stride == null) {
			odata = data = dataset.data.clone();
		} else {
			offset = 0;
			stride = null;
			base = null;
			odata = data = createArray(size);
			IndexIterator iter = dataset.getIterator();
			for (int i = 0; iter.hasNext(); i++) {
				data[i] = dataset.data[iter.index];
			}
		}
	}

	/**
	 * Cast a dataset to this class type
	 * @param dataset
	 */
	public BooleanDatasetBaseImpl(final Dataset dataset) {
		copyToView(dataset, this, true, false);
		offset = 0;
		stride = null;
		base = null;
		odata = data = createArray(size);
		IndexIterator iter = dataset.getIterator();
		for (int i = 0; iter.hasNext(); i++) {
			data[i] = dataset.getElementBooleanAbs(iter.index); // GET_ELEMENT_WITH_CAST
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}

		if (getRank() == 0) // already true for zero-rank dataset
			return true;

		BooleanDatasetBaseImpl other = (BooleanDatasetBaseImpl) obj;
		IndexIterator iter = getIterator();
		IndexIterator oiter = other.getIterator();
		while (iter.hasNext() && oiter.hasNext()) {
			if (data[iter.index] != other.data[oiter.index]) // OBJECT_UNEQUAL
				return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Create a dataset from an object which could be a Java list, array (of arrays...) or Number. Ragged
	 * sequences or arrays are padded with zeros.
	 *
	 * @param obj
	 * @return dataset with contents given by input
	 */
	public static BooleanDatasetBaseImpl createFromObject(final Object obj) {
		BooleanDatasetBaseImpl result = new BooleanDatasetBaseImpl();

		result.shape = DatasetUtils.getShapeFromObject(obj);
		result.size = DatasetUtils.calculateSize(result.shape);

		result.odata = result.data = createArray(result.size);

		int[] pos = new int[result.shape.length];
		result.fillData(obj, 0, pos);
		return result;
	}

	/**
	 * @param shape
	 * @return a dataset filled with ones
	 */
	public static BooleanDatasetBaseImpl ones(final int... shape) {
		return new BooleanDatasetBaseImpl(shape).fill(1);
	}

	/**
	 * This is a typed version of {@link #getBuffer()}
	 * @return data buffer as linear array
	 */
	@Override
	public boolean[] getData() { // PRIM_TYPE
		return data;
	}

	@Override
	protected int getBufferLength() {
		if (data == null)
			return 0;
		return data.length;
	}

	@Override
	public BooleanDatasetBaseImpl getView() {
		BooleanDatasetBaseImpl view = new BooleanDatasetBaseImpl();
		copyToView(this, view, true, true);
		view.setData();
		return view;
	}

	@Override
	public BooleanDatasetBaseImpl clone() {
		return new BooleanDatasetBaseImpl(this);
	}

	@Override
	public BooleanDatasetBaseImpl real() {
		return this;
	}

	@Override
	public BooleanDatasetBaseImpl realView() {
		return getView();
	}

	@Override
	public BooleanDatasetBaseImpl fill(final Object obj) {
		boolean dv = DTypeUtils.toBoolean(obj); // PRIM_TYPE // FROM_OBJECT
		IndexIterator iter = getIterator();
		while (iter.hasNext()) {
			data[iter.index] = dv;
		}

		setDirty();
		return this;
	}

	/**
	 * Get a value from an absolute index of the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @return value
	 */
	@Override
	public boolean getAbs(final int index) { // PRIM_TYPE
		return data[index];
	}

	@Override
	public boolean getElementBooleanAbs(final int index) {
		return false;
	}

	@Override
	public double getElementDoubleAbs(final int index) {
		return 0;
	}

	@Override
	public long getElementLongAbs(final int index) {
		return 0;
	}

	@Override
	public Object getObjectAbs(final int index) {
		return data[index];
	}

	@Override
	public String getStringAbs(final int index) {
		return stringFormat == null ? String.format("%b", data[index]) : // FORMAT_STRING
			stringFormat.format(data[index]);
	}

	/**
	 * Set a value at absolute index in the internal array. This is an internal method with no checks so can be
	 * dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index
	 *            absolute index
	 * @param val
	 *            new value
	 */
	@Override
	public void setAbs(final int index, final boolean val) { // PRIM_TYPE
		data[index] = val;
		setDirty();
	}

	@Override
	public void setItemDirect(final int dindex, final int sindex, final Object src) {
		boolean[] dsrc = (boolean[]) src; // PRIM_TYPE
		data[dindex] = dsrc[sindex];
	}

	@Override
	public void setObjectAbs(final int index, final Object obj) {
		if (index < 0 || index > data.length) {
			throw new IndexOutOfBoundsException("Index given is outside dataset");
		}

		setAbs(index, DTypeUtils.toBoolean(obj)); // FROM_OBJECT
	}

	/**
	 * @param i
	 * @return item in given position
	 */
	@Override
	public boolean get(final int i) { // PRIM_TYPE
		return data[get1DIndex(i)];
	}

	/**
	 * @param i
	 * @param j
	 * @return item in given position
	 */
	@Override
	public boolean get(final int i, final int j) { // PRIM_TYPE
		return data[get1DIndex(i, j)];
	}

	/**
	 * @param pos
	 * @return item in given position
	 */
	@Override
	public boolean get(final int... pos) { // PRIM_TYPE
		return data[get1DIndex(pos)];
	}

	@Override
	public Object getObject(final int i) {
		return Boolean.valueOf(get(i)); // CLASS_TYPE
	}

	@Override
	public Object getObject(final int i, final int j) {
		return Boolean.valueOf(get(i, j)); // CLASS_TYPE
	}

	@Override
	public Object getObject(final int... pos) {
		return Boolean.valueOf(get(pos)); // CLASS_TYPE
	}

	@Override
	public String getString(final int i) {
		return getStringAbs(get1DIndex(i));
	}

	@Override
	public String getString(final int i, final int j) {
		return getStringAbs(get1DIndex(i, j));
	}

	@Override
	public String getString(final int... pos) {
		return getStringAbs(get1DIndex(pos));
	}

	@Override
	public double getDouble(final int i) {
		return 0;
	}

	@Override
	public double getDouble(final int i, final int j) {
		return 0;
	}

	@Override
	public double getDouble(final int... pos) {
		return 0;
	}

	@Override
	public float getFloat(final int i) {
		return 0;
	}

	@Override
	public float getFloat(final int i, final int j) {
		return 0;
	}

	@Override
	public float getFloat(final int... pos) {
		return 0;
	}

	@Override
	public long getLong(final int i) {
		return 0;
	}

	@Override
	public long getLong(final int i, final int j) {
		return 0;
	}

	@Override
	public long getLong(final int... pos) {
		return 0;
	}

	@Override
	public int getInt(final int i) {
		return 0;
	}

	@Override
	public int getInt(final int i, final int j) {
		return 0;
	}

	@Override
	public int getInt(final int... pos) {
		return 0;
	}

	@Override
	public short getShort(final int i) {
		return 0;
	}

	@Override
	public short getShort(final int i, final int j) {
		return 0;
	}

	@Override
	public short getShort(final int... pos) {
		return 0;
	}

	@Override
	public byte getByte(final int i) {
		return 0;
	}

	@Override
	public byte getByte(final int i, final int j) {
		return 0;
	}

	@Override
	public byte getByte(final int... pos) {
		return 0;
	}

	@Override
	public boolean getBoolean(final int i) {
		return false;
	}

	@Override
	public boolean getBoolean(final int i, final int j) {
		return false;
	}

	@Override
	public boolean getBoolean(final int... pos) {
		return false;
	}

	/**
	 * Sets the value at a particular point to the passed value. The dataset must be 1D
	 *
	 * @param value
	 * @param i
	 */
	@Override
	public void setItem(final boolean value, final int i) { // PRIM_TYPE
		setAbs(get1DIndex(i), value);
	}

	/**
	 * Sets the value at a particular point to the passed value. The dataset must be 2D
	 *
	 * @param value
	 * @param i
	 * @param j
	 */
	@Override
	public void setItem(final boolean value, final int i, final int j) { // PRIM_TYPE
		setAbs(get1DIndex(i, j), value);
	}

	/**
	 * Sets the value at a particular point to the passed value
	 *
	 * @param value
	 * @param pos
	 */
	@Override
	public void setItem(final boolean value, final int... pos) { // PRIM_TYPE
		setAbs(get1DIndex(pos), value);
	}

	@Override
	public void set(final Object obj, final int i) {
		setItem(DTypeUtils.toBoolean(obj), i); // FROM_OBJECT
	}

	@Override
	public void set(final Object obj, final int i, final int j) {
		setItem(DTypeUtils.toBoolean(obj), i, j); // FROM_OBJECT
	}

	@Override
	public void set(final Object obj, int... pos) {
		if (pos == null || (pos.length == 0 && shape.length > 0)) {
			pos = new int[shape.length];
		}

		setItem(DTypeUtils.toBoolean(obj), pos); // FROM_OBJECT
	}


	@Override
	public void resize(int... newShape) {
		final long nsize = DatasetUtils.calculateSize(newShape);
		final boolean[] ndata = createArray(nsize); // PRIM_TYPE
		final IndexIterator iter = getIterator();
		for (int i = 0; iter.hasNext() && i < nsize; i++) {
			ndata[i] = data[iter.index];
		}

		odata = data = ndata;
		size = nsize;
		shape = newShape;
		stride = null;
		offset = 0;
		base = null;
	}

	@Override
	public BooleanDatasetBaseImpl sort(Integer axis) {
		throw new UnsupportedOperationException("Cannot sort dataset"); // BOOLEAN_USE
	}

	@Override
	public BooleanDatasetBaseImpl getUniqueItems() {
		Set<Boolean> set = new TreeSet<Boolean>(); // CLASS_TYPE
		IndexIterator it = getIterator();
		while (it.hasNext()) {
			set.add(data[it.index]);
		}

		BooleanDatasetBaseImpl u = new BooleanDatasetBaseImpl(set.size()); // CLASS_TYPE
		int i = 0;
		boolean[] udata = u.getData(); // PRIM_TYPE
		for (Boolean v : set) { // CLASS_TYPE
			udata[i++] = v;
		}
		return u;
	}

	@Override
	public BooleanDatasetBaseImpl getSlice(final SliceIterator siter) {
		BooleanDatasetBaseImpl result = new BooleanDatasetBaseImpl(siter.getShape());
		boolean[] rdata = result.data; // PRIM_TYPE

		for (int i = 0; siter.hasNext(); i++)
			rdata[i] = data[siter.index];

		result.setName(name + BLOCK_OPEN + siter.toString() + BLOCK_CLOSE);
		return result;
	}

	@Override
	public void fillDataset(Dataset result, IndexIterator iter) {
		IndexIterator riter = result.getIterator();

		boolean[] rdata = ((BooleanDatasetBaseImpl) result).data; // PRIM_TYPE

		while (riter.hasNext() && iter.hasNext())
			rdata[riter.index] = data[iter.index];
	}

	@Override
	public BooleanDatasetBaseImpl setByBoolean(final Object obj, Dataset selection) {
		if (obj instanceof Dataset) {
			final Dataset ds = (Dataset) obj;
			final long length = ((Number) selection.sum()).longValue();
			if (length != ds.getLongSize()) {
				throw new IllegalArgumentException(
						"Number of true items in selection does not match number of items in dataset");
			}

			final IndexIterator oiter = ds.getIterator();
			final BooleanIterator biter = getBooleanIterator(selection);

			while (biter.hasNext() && oiter.hasNext()) {
				data[biter.index] = ds.getElementBooleanAbs(oiter.index); // GET_ELEMENT_WITH_CAST
			}
		} else {
			final boolean dv = DTypeUtils.toBoolean(obj); // PRIM_TYPE // FROM_OBJECT
			final BooleanIterator biter = getBooleanIterator(selection);

			while (biter.hasNext()) {
				data[biter.index] = dv;
			}
		}
		setDirty();
		return this;
	}

	@Override
	public BooleanDatasetBaseImpl setBy1DIndex(final Object obj, final Dataset index) {
		if (obj instanceof Dataset) {
			final Dataset ds = (Dataset) obj;
			if (index.getLongSize() != ds.getLongSize()) {
				throw new IllegalArgumentException(
						"Number of items in index dataset does not match number of items in dataset");
			}

			final IndexIterator oiter = ds.getIterator();
			if (size > Integer.MAX_VALUE) {
				// TODO fix for large datasets
			}
			final IntegerIterator iter = new IntegerIterator(index, (int) size);

			while (iter.hasNext() && oiter.hasNext()) {
				data[iter.index] = ds.getElementBooleanAbs(oiter.index); // GET_ELEMENT_WITH_CAST
			}
		} else {
			final boolean dv = DTypeUtils.toBoolean(obj); // PRIM_TYPE // FROM_OBJECT
			if (size > Integer.MAX_VALUE) {
				// TODO fix for large datasets
			}
			IntegerIterator iter = new IntegerIterator(index, (int) size);

			while (iter.hasNext()) {
				data[iter.index] = dv;
			}
		}
		setDirty();
		return this;
	}

	@Override
	public BooleanDatasetBaseImpl setByIndexes(final Object obj, final Object... indexes) {
		final IntegersIterator iter = new IntegersIterator(shape, indexes);
		final int[] pos = iter.getPos();

		if (obj instanceof Dataset) {
			final Dataset ds = (Dataset) obj;
			if (DatasetUtils.calculateSize(iter.getShape()) != ds.getLongSize()) {
				throw new IllegalArgumentException(
						"Number of items in index datasets does not match number of items in dataset");
			}

			final IndexIterator oiter = ds.getIterator();

			while (iter.hasNext() && oiter.hasNext()) {
				setItem(ds.getElementBooleanAbs(oiter.index), pos); // GET_ELEMENT_WITH_CAST
			}
		} else {
			final boolean dv = DTypeUtils.toBoolean(obj); // PRIM_TYPE // FROM_OBJECT

			while (iter.hasNext()) {
				setItem(dv, pos);
			}
		}
		setDirty();
		return this;
	}

	@Override
	BooleanDatasetBaseImpl setSlicedView(Dataset view, Dataset d) {
		final BroadcastIterator it = BroadcastIterator.createIterator(view, d);

		while (it.hasNext()) {
			data[it.aIndex] = d.getElementBooleanAbs(it.bIndex); // GET_ELEMENT_WITH_CAST
		}
		return this;
	}

	@Override
	public BooleanDatasetBaseImpl setSlice(final Object obj, final IndexIterator siter) {

		if (obj instanceof IDataset) {
			final IDataset ds = (IDataset) obj;
			final int[] oshape = ds.getShape();

			if (!DatasetUtils.areShapesCompatible(siter.getShape(), oshape)) {
				throw new IllegalArgumentException(String.format(
						"Input dataset is not compatible with slice: %s cf %s", Arrays.toString(oshape),
						Arrays.toString(siter.getShape())));
			}

			if (ds instanceof Dataset) {
				final Dataset ads = (Dataset) ds;
				final IndexIterator oiter = ads.getIterator();

				while (siter.hasNext() && oiter.hasNext())
					data[siter.index] = ads.getElementBooleanAbs(oiter.index); // GET_ELEMENT_WITH_CAST
			} else {
				final IDatasetIterator oiter = new PositionIterator(oshape);
				final int[] pos = oiter.getPos();

				while (siter.hasNext() && oiter.hasNext())
					data[siter.index] = ds.getBoolean(pos); // PRIM_TYPE
			}
		} else {
			try {
				boolean v = DTypeUtils.toBoolean(obj); // PRIM_TYPE // FROM_OBJECT

				while (siter.hasNext())
					data[siter.index] = v;
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Object for setting slice is not a dataset or number");
			}
		}
		setDirty();
		return this;
	}

	@Override
	public void copyItemsFromAxes(final int[] pos, final boolean[] axes, final Dataset dest) {
		boolean[] ddata = (boolean[]) dest.getBuffer(); // PRIM_TYPE

		SliceIterator siter = getSliceIteratorFromAxes(pos, axes);
		int[] sshape = DatasetUtils.squeezeShape(siter.getShape(), false);

		IndexIterator diter = dest.getSliceIterator(null, sshape, null);

		if (ddata.length < DatasetUtils.calculateSize(sshape)) {
			throw new IllegalArgumentException("destination array is not large enough");
		}

		while (siter.hasNext() && diter.hasNext())
			ddata[diter.index] = data[siter.index];
	}

	@Override
	public void setItemsOnAxes(final int[] pos, final boolean[] axes, final Object src) {
		boolean[] sdata = (boolean[]) src; // PRIM_TYPE

		SliceIterator siter = getSliceIteratorFromAxes(pos, axes);

		if (sdata.length < DatasetUtils.calculateSize(siter.getShape())) {
			throw new IllegalArgumentException("destination array is not large enough");
		}

		for (int i = 0; siter.hasNext(); i++) {
			data[siter.index] = sdata[i];
		}
		setDirty();
	}

	@Override
	protected Number fromDoubleToNumber(double x) {
		return Integer.valueOf((int) (long) x); // BOOLEAN_USE
		// return null; // OBJECT_USE
	}

	private List<int[]> findPositions(final boolean value) { // PRIM_TYPE
		IndexIterator iter = getIterator(true);
		List<int[]> posns = new ArrayList<int[]>();
		int[] pos = iter.getPos();

		{
			while (iter.hasNext()) {
				if (data[iter.index] == value) {
					posns.add(pos.clone());
				}
			}
		}
		return posns;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public int[] maxPos(boolean ignoreInvalids) {
		if (storedValues == null || storedValues.isEmpty()) {
			calculateMaxMin(ignoreInvalids, ignoreInvalids);
		}
		String n = storeName(ignoreInvalids, ignoreInvalids, STORE_MAX_POS);
		Object o = storedValues.get(n);

		List<int[]> max = null;
		if (o == null) {
			max = findPositions(max(false).intValue() != 0); // BOOLEAN_USE
			// max = findPositions(null); // OBJECT_USE
			storedValues.put(n, max);
		} else if (o instanceof List<?>) {
			max = (List<int[]>) o;
		} else {
			throw new InternalError("Inconsistent internal state of stored values for statistics calculation");
		}

		return max.get(0); // first maximum
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public int[] minPos(boolean ignoreInvalids) {
		if (storedValues == null || storedValues.isEmpty()) {
			calculateMaxMin(ignoreInvalids, ignoreInvalids);
		}
		String n = storeName(ignoreInvalids, ignoreInvalids, STORE_MIN_POS);
		Object o = storedValues.get(n);
		List<int[]> min = null;
		if (o == null) {
			min = findPositions(min(false).intValue() != 0); // BOOLEAN_USE
			// min = findPositions(null); // OBJECT_USE
			storedValues.put(n, min);
		} else if (o instanceof List<?>) {
			min = (List<int[]>) o;
		} else {
			throw new InternalError("Inconsistent internal state of stored values for statistics calculation");
		}

		return min.get(0); // first minimum
	}

	@Override
	public boolean containsNans() {
		return false;
	}

	@Override
	public boolean containsInfs() {
		return false;
	}

	@Override
	public boolean containsInvalidNumbers() {
		return false;
	}

	@Override
	public BooleanDatasetBaseImpl iadd(final Object b) {
		return this;
	}

	@Override
	public BooleanDatasetBaseImpl isubtract(final Object b) {
		return this;
	}

	@Override
	public BooleanDatasetBaseImpl imultiply(final Object b) {
		return this;
	}

	@Override
	public BooleanDatasetBaseImpl idivide(final Object b) {
		return this;
	}

	@Override
	public BooleanDatasetBaseImpl ifloor() {
		return this;
	}

	@Override
	public BooleanDatasetBaseImpl iremainder(final Object b) {
		return this;
	}

	@Override
	public BooleanDatasetBaseImpl ipower(final Object b) {
		return this;
	}

	@Override
	public double residual(final Object b, final Dataset w, boolean ignoreNaNs) {
		double sum = 0;
		return sum;
	}
}
