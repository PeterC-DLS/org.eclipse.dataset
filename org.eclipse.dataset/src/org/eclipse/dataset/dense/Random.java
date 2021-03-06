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


import org.apache.commons.math.random.MersenneTwister;
import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.random.RandomGenerator;
import org.eclipse.dataset.DatasetException;
import org.eclipse.dataset.IDataset;
import org.eclipse.dataset.ILazyDataset;
import org.eclipse.dataset.IMonitor;
import org.eclipse.dataset.SliceND;
import org.eclipse.dataset.internal.dense.DoubleDataset;
import org.eclipse.dataset.internal.dense.IntegerDataset;
import org.eclipse.dataset.internal.dense.LazyDataset;
import org.eclipse.dataset.io.ILazyLoader;

/**
 * Class to hold methods to create random datasets
 * 
 * Emulates numpy.random
 */
public class Random {
	private final static RandomGenerator generator = new MersenneTwister();
	private final static RandomData prng = new RandomDataImpl(generator);

	/**
	 * @param seed
	 */
	public static void seed(final int seed) {
		generator.setSeed(seed);
	}

	/**
	 * @param seed
	 */
	public static void seed(final int[] seed) {
		generator.setSeed(seed);
	}

	/**
	 * @param seed
	 */
	public static void seed(final long seed) {
		generator.setSeed(seed);
	}

	/**
	 * @param shape
	 * @return an array of values sampled from a uniform distribution between 0 (inclusive) and 1 (exclusive) 
	 */
	public static DoubleDataset rand(final int... shape) {
		double[] buf = new double[DatasetUtils.calculateSize(shape)];

		for (int i = 0; i < buf.length; i++) {
			buf[i] = generator.nextDouble();
		}

		DoubleDataset data = (DoubleDataset) DatasetFactory.createFromObject(buf);
		data.setShape(shape);
		return data;
	}

	/**
	 * @param low
	 * @param high
	 * @param shape
	 * @return an array of values sampled from a uniform distribution between low and high (both exclusive) 
	 */
	public static DoubleDataset rand(double low, double high, final int... shape) {
		double[] buf = new double[DatasetUtils.calculateSize(shape)];

		for (int i = 0; i < buf.length; i++) {
			buf[i] = prng.nextUniform(low, high);
		}

		DoubleDataset data = (DoubleDataset) DatasetFactory.createFromObject(buf);
		data.setShape(shape);
		return data;
	}

	/**
	 * @param shape
	 * @return an array of values sampled from a Gaussian distribution with mean 0 and variance 1 
	 * 
	 * (The term Gaussian here is a description of a shape of data taken from the mathematician of the
	 * same name Carl Friedrich Gauss  http://en.wikipedia.org/wiki/Carl_Friedrich_Gauss born in 1777.)
	 */
	public static DoubleDataset randn(final int... shape) {
		double[] buf = new double[DatasetUtils.calculateSize(shape)];

		for (int i = 0; i < buf.length; i++) {
			buf[i] = generator.nextGaussian();
		}

		DoubleDataset data = (DoubleDataset) DatasetFactory.createFromObject(buf);
		data.setShape(shape);
		return data;
	}

	/**
	 * @param mean
	 * @param std standard deviation
	 * @param shape
	 * @return an array of values sampled from a Gaussian distribution with given mean and standard deviation 
	 */
	public static DoubleDataset randn(double mean, double std, final int... shape) {
		double[] buf = new double[DatasetUtils.calculateSize(shape)];

		for (int i = 0; i < buf.length; i++) {
			buf[i] = prng.nextGaussian(mean, std);
		}

		DoubleDataset data = (DoubleDataset) DatasetFactory.createFromObject(buf);
		data.setShape(shape);
		return data;
	}

	/**
	 * @param low 
	 * @param high
	 * @param shape
	 * @return an array of values sampled from a discrete uniform distribution in range [low, high)
	 */
	public static IntegerDataset randint(final int low, final int high, final int[] shape) {
		return random_integers(low, high-1, shape);
	}

	/**
	 * @param low 
	 * @param high 
	 * @param shape
	 * @return an array of values sampled from a discrete uniform distribution in range [low, high]
	 */
	public static IntegerDataset random_integers(final int low, final int high, final int[] shape) {
		int[] buf = new int[DatasetUtils.calculateSize(shape)];

		if (low == high) {
			for (int i = 0; i < buf.length; i++) {
				buf[i] = low;
			}			
		} else {
			for (int i = 0; i < buf.length; i++) {
				buf[i] = prng.nextInt(low, high);
			}
		}

		IntegerDataset data = (IntegerDataset) DatasetFactory.createFromObject(buf);
		data.setShape(shape);
		return data;
	}

	/**
	 * @param beta 
	 * @param shape
	 * @return an array of values sampled from an exponential distribution with mean beta
	 */
	public static DoubleDataset exponential(final double beta, final int... shape) {
		double[] buf = new double[DatasetUtils.calculateSize(shape)];

		for (int i = 0; i < buf.length; i++) {
			buf[i] = prng.nextExponential(beta);
		}

		DoubleDataset data = (DoubleDataset) DatasetFactory.createFromObject(buf);
		data.setShape(shape);
		return data;
	}

	/**
	 * @param lam 
	 * @param shape
	 * @return an array of values sampled from an exponential distribution with mean lambda
	 */
	public static IntegerDataset poisson(final double lam, final int... shape) {
		int[] buf = new int[DatasetUtils.calculateSize(shape)];

		for (int i = 0; i < buf.length; i++) {
			buf[i] = (int) prng.nextPoisson(lam);
		}

		IntegerDataset data = (IntegerDataset) DatasetFactory.createFromObject(buf);
		data.setShape(shape);
		return data;
	}

	/**
	 * @param shape
	 * @return a lazy dataset with uniformly distributed random numbers
	 */
	public static ILazyDataset lazyRand(int... shape) {
		return lazyRand(Dataset.FLOAT64, "random", shape);
	}

	/**
	 * @param name
	 * @param shape
	 * @return a lazy dataset with uniformly distributed random numbers
	 */
	public static ILazyDataset lazyRand(String name, int... shape) {
		return lazyRand(Dataset.FLOAT64, name, shape);
	}

	/**
	 * @param dtype
	 * @param name
	 * @param shape
	 * @return a lazy dataset with uniformly distributed random numbers
	 */
	public static ILazyDataset lazyRand(int dtype, String name, int... shape) {
		
		return new LazyDataset(name, dtype, shape, new ILazyLoader() {

			@Override
			public boolean isFileReadable() {
				return true;
			}

			@Override
			public IDataset getDataset(IMonitor mon, SliceND slice) throws DatasetException {
                return rand(slice.getShape());
			}
		});
	}
}
