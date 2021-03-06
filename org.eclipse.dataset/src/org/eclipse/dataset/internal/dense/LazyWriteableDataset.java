/*-
 * Copyright 2015 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dataset.internal.dense;

import java.io.IOException;

import org.eclipse.dataset.DataEvent;
import org.eclipse.dataset.DatasetException;
import org.eclipse.dataset.IDataset;
import org.eclipse.dataset.ILazyWriteableDataset;
import org.eclipse.dataset.IMonitor;
import org.eclipse.dataset.Slice;
import org.eclipse.dataset.SliceND;
import org.eclipse.dataset.dense.Dataset;
import org.eclipse.dataset.dense.DatasetFactory;
import org.eclipse.dataset.dense.DatasetUtils;
import org.eclipse.dataset.io.ILazySaver;

/**
 * Subclass of lazy dataset that allows setting slices
 */
public class LazyWriteableDataset extends LazyDynamicDataset implements ILazyWriteableDataset {
	private int[] chunks;
	private ILazySaver saver;

	/**
	 * Create a lazy dataset
	 * @param name
	 * @param dtype dataset type
	 * @param elements
	 * @param shape
	 * @param maxShape
	 * @param chunks
	 * @param saver
	 */
	public LazyWriteableDataset(String name, int dtype, int elements, int[] shape, int[] maxShape, int[] chunks, ILazySaver saver) {
		super(name, dtype, elements, shape, maxShape, saver);
		this.chunks = chunks == null ? null : chunks.clone();
		this.saver = saver;

		// check shape for expandable dimensions
		for (int i = 0; i < shape.length; i++) {
			if (this.shape[i] == ILazyWriteableDataset.UNLIMITED) {
				this.shape[i] = 0;
			}
		}
		size = DatasetUtils.calculateLongSize(this.shape);
	}

	/**
	 * Create a lazy dataset
	 * @param name
	 * @param dtype dataset type
	 * @param shape
	 * @param maxShape
	 * @param chunks
	 * @param saver
	 */
	public LazyWriteableDataset(String name, int dtype, int[] shape, int[] maxShape, int[] chunks, ILazySaver saver) {
		this(name, dtype, 1, shape, maxShape, chunks, saver);
	}

	/**
	 * Create a lazy writeable dataset based on in-memory data (handy for testing)
	 * @param dataset
	 */
	public static LazyWriteableDataset createLazyDataset(final Dataset dataset) {
		return createLazyDataset(dataset, null);
	}

	/**
	 * Create a lazy writeable dataset based on in-memory data (handy for testing)
	 * @param dataset
	 */
	public static LazyWriteableDataset createLazyDataset(final Dataset dataset, final int[] maxShape) {
		return new LazyWriteableDataset(dataset.getName(), dataset.getDType(), dataset.getElementsPerItem(), dataset.getShape(),
				maxShape, null,
		new ILazySaver() {
			Dataset d = dataset;
			@Override
			public boolean isFileReadable() {
				return true;
			}

			@Override
			public boolean isFileWriteable() {
				return true;
			}

			@Override
			public void initialize() throws DatasetException {
			}

			@Override
			public Dataset getDataset(IMonitor mon, SliceND slice)
					throws DatasetException {
				return d.getSlice(mon, slice);
			}

			@Override
			public void setSlice(IMonitor mon, IDataset data, SliceND slice) throws DatasetException {
				if (slice.isExpanded()) {
					Dataset od = d;
					d = DatasetFactory.zeros(slice.getSourceShape(), od.getDType());
					d.setSlice(od, SliceND.createSlice(od, null, null));
				}
				d.setSlice(data, slice);
			}
		});
	}

	@Override
	public int[] getChunking() {
		return chunks;
	}

	@Override
	public void setChunking(int[] chunks) {
		this.chunks = chunks == null ? null : chunks.clone();
	}

	@Override
	public LazyWriteableDataset clone() {
		LazyWriteableDataset ret = new LazyWriteableDataset(new String(name), getDType(), getElementsPerItem(), 
				oShape, maxShape, chunks, saver);
		ret.shape = shape;
		ret.size = size;
		ret.prepShape = prepShape;
		ret.postShape = postShape;
		ret.begSlice = begSlice;
		ret.delSlice = delSlice;
		ret.map = map;
		ret.base = base;
		ret.metadata = copyMetadata();
		ret.oMetadata = oMetadata;
		ret.eventDelegate = eventDelegate;
		return ret;
	}

	@Override
	public LazyWriteableDataset getSliceView(int[] start, int[] stop, int[] step) {
		return (LazyWriteableDataset) super.getSliceView(start, stop, step);
	}

	@Override
	public LazyWriteableDataset getSliceView(Slice... slice) {
		return (LazyWriteableDataset) super.getSliceView(slice);
	}

	@Override
	public LazyWriteableDataset getSliceView(SliceND slice) {
		return (LazyWriteableDataset) super.getSliceView(slice);
	}

	@Override
	public LazyWriteableDataset getTransposedView(int... axes) {
		return (LazyWriteableDataset) super.getTransposedView(axes);
	}

	/**
	 * Set a slice of the dataset
	 * 
	 * @param data
	 * @param slice an n-D slice
	 * @throws DatasetException 
	 */
	public void setSlice(IDataset data, SliceND slice) throws DatasetException {
		setSlice(null, data, slice);
	}

	@Override
	public void setSlice(IMonitor monitor, IDataset data, SliceND slice) throws DatasetException {
		if (saver == null || !saver.isFileWriteable()) {
			throw new DatasetException(new IOException("Cannot write to file!"));
		}

		SliceND nslice = calcTrueSlice(slice);

		if (base != null) {
			((ILazyWriteableDataset) base).setSlice(monitor, data, nslice);
		} else {
			saver.setSlice(monitor, data, nslice);
			oShape = nslice.getSourceShape();
			shape = slice.getSourceShape();
			eventDelegate.fire(new DataEvent(name, shape));
		}
	}

	@Override
	public void setSlice(IMonitor monitor, IDataset data, int[] start, int[] stop, int[] step) throws DatasetException {
		setSlice(monitor, data, new SliceND(shape, maxShape, start, stop, step));
	}

	/**
	 * Set saver (and also loader)
	 * @param saver
	 */
	@Override
	public void setSaver(ILazySaver saver) {
		this.saver = saver;
		this.loader = saver;
	}

	@Override
	protected SliceND createSlice(int[] nstart, int[] nstop, int[] nstep) {
		if (base == null) {
			return new SliceND(oShape, maxShape, nstart, nstop, nstep);
		}
		return base.createSlice(nstart, nstop, nstep);
	}
}
