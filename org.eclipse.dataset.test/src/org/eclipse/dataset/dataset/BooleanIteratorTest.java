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

import org.eclipse.dataset.dense.BooleanIterator;
import org.eclipse.dataset.dense.Dataset;
import org.eclipse.dataset.dense.DatasetFactory;
import org.eclipse.dataset.dense.IndexIterator;
import org.eclipse.dataset.dense.BooleanDataset;
import org.eclipse.dataset.dense.IntegerDataset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BooleanIteratorTest {
	Dataset a, b;

	@Before
	public void setUpClass() {
		a = DatasetFactory.createFromObject(new double[] { 0, 1, 3, 5, -7, -9 });
		b = DatasetFactory.createFromObject(new double[] { 0.01, 1.2, 2.9, 5, -7.1, -9 });
	}

	@Test
	public void testEqualTo() {
		Dataset c = a.clone().reshape(2, 3);
		BooleanDataset s = (BooleanDataset) DatasetFactory.createFromObject(new boolean[] {false, true, false, true, false, true});

		List<Integer> inds = new ArrayList<Integer>();

		BooleanIterator iter = c.getBooleanIterator(s);
		while (iter.hasNext())
			inds.add((int) c.getElementLongAbs(iter.index));

		checkDatasets((IntegerDataset) DatasetFactory.createFromList(inds), (IntegerDataset) DatasetFactory.createFromObject(new int[] {1,5,-9}));

		iter = c.getBooleanIterator(s, false);
		inds.clear();
		while (iter.hasNext())
			inds.add((int) c.getElementLongAbs(iter.index));

		checkDatasets((IntegerDataset) DatasetFactory.createFromList(inds), (IntegerDataset) DatasetFactory.createFromObject(new int[] {0, 3,-7}));
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
