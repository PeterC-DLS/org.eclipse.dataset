/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dataset.dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.dataset.dense.Dataset;
import org.eclipse.dataset.dense.DatasetFactory;
import org.eclipse.dataset.dense.IndexIterator;
import org.eclipse.dataset.dense.IntegerIterator;
import org.eclipse.dataset.internal.dense.DoubleDataset;
import org.eclipse.dataset.internal.dense.IntegerDataset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntegerIteratorTest {
	Dataset a, b;

	@Before
	public void setUpClass() {
		a = new DoubleDataset(new double[] { 0, 1, 3, 5, -7, -9 });
		b = new DoubleDataset(new double[] { 0.01, 1.2, 2.9, 5, -7.1, -9 });
	}

	@Test
	public void testEqualTo() {
		Dataset c = a.clone().reshape(2, 3);
		IntegerDataset s = new IntegerDataset(new int[] {0, 3, 4}, null);

		List<Integer> inds = new ArrayList<Integer>();

		IntegerIterator iter = new IntegerIterator(s, c.getSize());
		while (iter.hasNext())
			inds.add((int) c.getElementLongAbs(iter.index));

		checkDatasets((IntegerDataset) DatasetFactory.createFromList(inds),
				new IntegerDataset(new int[] {0,5,-7}, null));
	}

	public void checkDatasets(IntegerDataset calc, IntegerDataset expected) {
		IndexIterator at = calc.getIterator(true);
		IndexIterator bt = expected.getIterator();
		final int is = calc.getElementsPerItem();

		while (at.hasNext() && bt.hasNext()) {
			for (int j = 0; j < is; j++) {
				Assert.assertEquals("Value does not match at " + Arrays.toString(at.getPos()) + "; " + j +
						": ", expected.getAbs(at.index + j), calc.getAbs(bt.index + j));
			}
		}
	}
}
