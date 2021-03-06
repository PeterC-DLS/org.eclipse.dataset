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

package org.eclipse.dataset.dense;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.dataset.DatasetException;
import org.eclipse.dataset.IDataset;
import org.eclipse.dataset.internal.dense.DoubleDataset;

/**
 * Statistics of data set lists. Used for image processing.
 */
public class CollectionStats {

	private static interface StatFunction {
		double evaluate(Dataset set);
	}

	/**
	 * Used to get a mean image from a set of images for instance.
	 * 
	 * @param sets
	 * @return mean data set of the same shape as those passed in.
	 * @throws DatasetException
	 */
	public static Dataset mean(final List<IDataset> sets) throws DatasetException {
		
		return process(sets, new StatFunction() {
			@Override
			public double evaluate(Dataset set) {
				return (Double)set.mean();
			}
		});
	}
	
	/**
	 * Used to get a median image from a set of images for instance.
	 * 
	 * @param sets
	 * @return median data set of the same shape as those passed in.
	 * @throws DatasetException
	 */
	public static Dataset median(final List<IDataset> sets) throws DatasetException {
		
		return process(sets, new StatFunction() {
			@Override
			public double evaluate(Dataset set) {
				return (Double)Stats.median(set);
			}
		});
	}

	/**
	 * Used to get a median image from a set of images for instance.
	 * 
	 * @param sets
	 * @return median data set of the same shape as those passed in.
	 * @throws DatasetException
	 */
	private static Dataset process(final List<IDataset> sets,
			                               final StatFunction   function) throws DatasetException {
		
		int[] shape = assertShapes(sets);
		final DoubleDataset result = (DoubleDataset) DatasetFactory.zeros(shape, Dataset.FLOAT64);
        final double[] rData = result.getData();
        final IndexIterator iter = result.getIterator(true);
        final int[] pos = iter.getPos();

        final int len = sets.size();
		final DoubleDataset pixel = (DoubleDataset) DatasetFactory.zeros(new int[] {len}, Dataset.FLOAT64);
		final double[] pData = pixel.getData();
		while (iter.hasNext()) {
			for (int ipix = 0; ipix < len; ipix++) {
				pData[ipix] = sets.get(ipix).getDouble(pos);
			}
			pixel.setDirty();
			rData[iter.index] = function.evaluate(pixel);
		}
        
        return result;
	}

	private static int[] assertShapes(final Collection<IDataset> sets) throws DatasetException{
		
		if (sets.size()<2) throw new DatasetException("You must take the median of at least two sets!");
		
		final Iterator<IDataset> it = sets.iterator();
		final int[] shape = it.next().getShape();
		while (it.hasNext()) {
			IDataset d = it.next();
			final int[] nextShape = d.getShape();
			if (!Arrays.equals(shape, nextShape)) throw new DatasetException("All data sets should be the same shape!");
		}
		return shape;
	}
}
