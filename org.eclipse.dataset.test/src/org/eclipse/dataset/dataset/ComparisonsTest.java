/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dataset.dataset;

import java.util.List;

import org.eclipse.dataset.TestUtils;
import org.eclipse.dataset.dense.BooleanDataset;
import org.eclipse.dataset.dense.Comparisons;
import org.eclipse.dataset.dense.ComplexDoubleDataset;
import org.eclipse.dataset.dense.Dataset;
import org.eclipse.dataset.dense.DatasetFactory;
import org.eclipse.dataset.dense.DoubleDataset;
import org.eclipse.dataset.dense.IntegerDataset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ComparisonsTest {
	Dataset a, b, z;

	@Before
	public void setUpClass() {
		a = DatasetFactory.createFromObject(DoubleDataset.class, new double[] { 0, 1, 3, 5, -7, -9 });
		b = DatasetFactory.createFromObject(DoubleDataset.class, new double[] { 0.01, 1.2, 2.9, 5, -7.1, -9 });
		z = DatasetFactory.createFromObject(ComplexDoubleDataset.class, new double[] { 0.01, 1.2, 2.5, 5, -7.1, -9, 2.5, 0 });
	}

	@Test
	public void testEqualTo() {
		BooleanDataset c = Comparisons.equalTo(a, b);
		BooleanDataset d = DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false, true, false, true});
		TestUtils.assertDatasetEquals(c, d);

		TestUtils.assertDatasetEquals(Comparisons.equalTo(3, a), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {false, false, true, false, false, false}));

		DoubleDataset ta = (DoubleDataset) DatasetFactory.zeros(new int [] {20, 10}, Dataset.FLOAT64);
		ta.fill(Double.NaN);
		DoubleDataset tb = (DoubleDataset) DatasetFactory.zeros(new int [] {20, 10}, Dataset.FLOAT64);
		tb.fill(Double.NaN);
		
		BooleanDataset bd = DatasetFactory.zeros(BooleanDataset.class, ta.getShape());
		bd.fill(Boolean.FALSE);
		TestUtils.assertDatasetEquals(Comparisons.equalTo(ta, tb), bd);
		ta.fill(Double.POSITIVE_INFINITY);
		tb.fill(Double.POSITIVE_INFINITY);
		bd.fill(Boolean.TRUE);
		TestUtils.assertDatasetEquals(Comparisons.equalTo(ta, tb), bd);

		c = Comparisons.equalTo(DatasetFactory.createFromObject(1.), 1);
		TestUtils.assertDatasetEquals(c, DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true}));

		c = Comparisons.equalTo(a, 3);
		TestUtils.assertDatasetEquals(c, DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, true, false, false, false}));
		c = Comparisons.equalTo(3, a);
		TestUtils.assertDatasetEquals(c, DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, true, false, false, false}));

		c = Comparisons.equalTo(z, 2.5);
		TestUtils.assertDatasetEquals(c, DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false, true}));
		c = Comparisons.equalTo(2.5, z);
		TestUtils.assertDatasetEquals(c, DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false, true}));
	}

	@Test
	public void testAlmostEqualTo() {
		BooleanDataset c = Comparisons.almostEqualTo(a, b, 0.1, 1e-3);
		BooleanDataset d = DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, true, true, true, true});
		TestUtils.assertDatasetEquals(c, d);

		TestUtils.assertDatasetEquals(Comparisons.almostEqualTo(a, 3, 0.1, 1e-3), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {false, false, true, false, false, false}));
		TestUtils.assertDatasetEquals(Comparisons.almostEqualTo(3, a, 0.1, 1e-3), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {false, false, true, false, false, false}));

		c = Comparisons.almostEqualTo(z, 2.5, 0.1, 1e-3);
		TestUtils.assertDatasetEquals(c, DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false, true}));
		c = Comparisons.almostEqualTo(2.5, z, 0.1, 1e-3);
	}

	@Test
	public void testAllCloseTo() {
		Assert.assertFalse(Comparisons.allCloseTo(a, b, 0.1, 1e-3));

		Assert.assertTrue(Comparisons.allCloseTo(a, b, 0.1, 2e-1));

		Assert.assertFalse(Comparisons.allCloseTo(z, 2.5, 0.1, 1e-3));
		Assert.assertFalse(Comparisons.allCloseTo(2.5, z, 0.1, 1e-3));
	}

	@Test
	public void testGreaterThan() {
		BooleanDataset c = Comparisons.greaterThan(a, b);
		BooleanDataset d = DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, true, false, true, false});
		TestUtils.assertDatasetEquals(c, d);

		TestUtils.assertDatasetEquals(Comparisons.greaterThan(3, a), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {true, true, false, false, true, true}));
		TestUtils.assertDatasetEquals(Comparisons.greaterThan(a, 3), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {false, false, false, true, false, false}));
	}

	@Test
	public void testGreaterThanOrEqualTo() {
		BooleanDataset c = Comparisons.greaterThanOrEqualTo(a, b);
		BooleanDataset d = DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, true, true, true, true});
		TestUtils.assertDatasetEquals(c, d);

		TestUtils.assertDatasetEquals(Comparisons.greaterThanOrEqualTo(3, a), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {true, true, true, false, true, true}));
		TestUtils.assertDatasetEquals(Comparisons.greaterThanOrEqualTo(a, 3), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {false, false, true, true, false, false}));
	}

	@Test
	public void testLessThan() {
		BooleanDataset c = Comparisons.lessThan(a, b);
		BooleanDataset d = DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true, true, false, false, false, false});
		TestUtils.assertDatasetEquals(c, d);

		TestUtils.assertDatasetEquals(Comparisons.lessThan(3, a), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {false, false, false, true, false, false}));
		TestUtils.assertDatasetEquals(Comparisons.lessThan(a, 3), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {true, true, false, false, true, true}));
	}

	@Test
	public void testLessThanOrEqualTo() {
		BooleanDataset c = Comparisons.lessThanOrEqualTo(a, b);
		BooleanDataset d = DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true, true, false, true, false, true});
		TestUtils.assertDatasetEquals(c, d);

		TestUtils.assertDatasetEquals(Comparisons.lessThanOrEqualTo(3, a), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {false, false, true, true, false, false}));
		TestUtils.assertDatasetEquals(Comparisons.lessThanOrEqualTo(a, 3), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {true, true, true, false, true, true}));
	}

	@Test
	public void testWithinRange() {
		BooleanDataset c = Comparisons.withinRange(b, -8, 2);
		BooleanDataset d = DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true, true, false, false, true, false});
		TestUtils.assertDatasetEquals(c, d);
	}

	@Test
	public void testAllTrue() {
		Assert.assertFalse(Comparisons.allTrue(a));
		Assert.assertTrue(Comparisons.allTrue(b));
		Dataset c = a.clone().reshape(2, 3);
		TestUtils.assertDatasetEquals(Comparisons.allTrue(c, 0), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, true, true}));
		Dataset d = b.clone().reshape(2, 3);
		TestUtils.assertDatasetEquals(Comparisons.allTrue(d, 1), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true, true}));
	}

	@Test
	public void testAnyTrue() {
		Assert.assertTrue(Comparisons.anyTrue(a));
		Assert.assertTrue(Comparisons.anyTrue(b));
		Assert.assertFalse(Comparisons.anyTrue(DatasetFactory.createFromObject(DoubleDataset.class, new double[] {0, 0})));
		Dataset c = a.clone().reshape(2, 3);
		TestUtils.assertDatasetEquals(Comparisons.anyTrue(c, 0), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true, true, true}));
		Dataset d = b.clone().reshape(2, 3);
		TestUtils.assertDatasetEquals(Comparisons.anyTrue(d, 1), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true, true}));
		TestUtils.assertDatasetEquals(Comparisons.anyTrue(DatasetFactory.createFromObject(DoubleDataset.class, new double[] {0, 0, 0, 1}).reshape(2,2), 1),
				DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, true}));
	}

	@Test
	public void testNot() {
		TestUtils.assertDatasetEquals(Comparisons.logicalNot(a), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {true, false, false, false, false, false}));
		TestUtils.assertDatasetEquals(Comparisons.logicalNot(b), DatasetFactory.createFromObject(BooleanDataset.class, 
				new boolean[] {false, false, false, false, false, false}));
	}

	@Test
	public void testAnd() {
		BooleanDataset c = Comparisons.logicalAnd(a, b);
		BooleanDataset d = DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, true, true, true, true, true});
		TestUtils.assertDatasetEquals(c, d);
	}

	@Test
	public void testOr() {
		BooleanDataset c = Comparisons.logicalOr(a, b);
		BooleanDataset d = DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true, true, true, true, true, true});
		TestUtils.assertDatasetEquals(c, d);
	}

	@Test
	public void testXor() {
		BooleanDataset c = Comparisons.logicalXor(a, b);
		BooleanDataset d = DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true, false, false, false, false, false});
		TestUtils.assertDatasetEquals(c, d);
	}

	@Test
	public void testNonZero() {
		Dataset c = a.clone().reshape(2, 3);
		List<Dataset> e = Comparisons.nonZero(c);
		TestUtils.assertDatasetEquals(e.get(0), DatasetFactory.createFromObject(IntegerDataset.class, new int[] {0, 0, 1, 1, 1}, null));
		TestUtils.assertDatasetEquals(e.get(1), DatasetFactory.createFromObject(IntegerDataset.class, new int[] {1, 2, 0, 1, 2}, null));
	}

	@Test
	public void testFlags() {
		Dataset c;

		c = DatasetFactory.createFromObject(new int[] {0, -1, 1});
		TestUtils.assertDatasetEquals(Comparisons.isFinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true, true, true}));
		TestUtils.assertDatasetEquals(Comparisons.isInfinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false}));
		TestUtils.assertDatasetEquals(Comparisons.isPositiveInfinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false}));
		TestUtils.assertDatasetEquals(Comparisons.isNegativeInfinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false}));
		TestUtils.assertDatasetEquals(Comparisons.isNaN(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false}));

		c = DatasetFactory.createFromObject(new double[] {0, -1, 1});
		TestUtils.assertDatasetEquals(Comparisons.isFinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true, true, true}));
		TestUtils.assertDatasetEquals(Comparisons.isInfinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false}));
		TestUtils.assertDatasetEquals(Comparisons.isPositiveInfinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false}));
		TestUtils.assertDatasetEquals(Comparisons.isNegativeInfinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false}));
		TestUtils.assertDatasetEquals(Comparisons.isNaN(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false}));

		c = DatasetFactory.createFromObject(new double[] {Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY});
		TestUtils.assertDatasetEquals(Comparisons.isFinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false}));
		TestUtils.assertDatasetEquals(Comparisons.isInfinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, true, true}));
		TestUtils.assertDatasetEquals(Comparisons.isPositiveInfinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, true}));
		TestUtils.assertDatasetEquals(Comparisons.isNegativeInfinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, true, false}));
		TestUtils.assertDatasetEquals(Comparisons.isNaN(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true, false, false}));

		c = DatasetFactory.createFromObject(new double[] {Double.NaN, -Double.POSITIVE_INFINITY, -Double.NEGATIVE_INFINITY});
		TestUtils.assertDatasetEquals(Comparisons.isFinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, false}));
		TestUtils.assertDatasetEquals(Comparisons.isInfinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, true, true}));
		TestUtils.assertDatasetEquals(Comparisons.isPositiveInfinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, false, true}));
		TestUtils.assertDatasetEquals(Comparisons.isNegativeInfinite(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {false, true, false}));
		TestUtils.assertDatasetEquals(Comparisons.isNaN(c), DatasetFactory.createFromObject(BooleanDataset.class, new boolean[] {true, false, false}));
	}

	@Test
	public void testNans() {
		double n = Double.NaN;
		double[] a = {-4.34, -1.34, 21.34};
		double l = -2.;
		double h = 15.4;
		for (double x : a) {
			System.err.println((x >= l && x <= h) + "\t" + (x <= h) + " = " + ((Double.isNaN(n) || x >= n) && x <= h) + "\t" +  (x >= l) + " = " + (x >= l && (Double.isNaN(n) || x <= n)) + "\t" + (x >= n && x <= n));
		}

		System.err.println(Double.isNaN(n));
		System.err.println(Float.isNaN((float) n));
		float f = Float.NaN;
		System.err.println(Double.isNaN(f));
	}
}
