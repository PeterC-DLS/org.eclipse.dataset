/*
 * Copyright (c) 2014 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dataset.metadata;

import java.util.List;
import java.util.Map;

import org.eclipse.dataset.ILazyDataset;
import org.eclipse.dataset.dense.Dataset;
import org.eclipse.dataset.dense.DatasetFactory;
import org.eclipse.dataset.dense.BooleanDataset;
import org.eclipse.dataset.dense.DoubleDataset;
import org.eclipse.dataset.dense.ShortDataset;
import org.eclipse.dataset.metadata.MetadataType;
import org.eclipse.dataset.metadata.Sliceable;

public class SubMetadata extends SliceableTestMetadata {
	private static final long serialVersionUID = -1507164756072041135L;

	@Sliceable
	ILazyDataset ldb;

	public SubMetadata(ILazyDataset ld, DoubleDataset[] array, List<ShortDataset> list, Map<String, BooleanDataset> map, List<DoubleDataset[]> l2) {
		super(ld, array, list, map, l2);
		ldb = DatasetFactory.zeros(ld.getShape(), Dataset.FLOAT64);
	}

	public ILazyDataset getLazyDataset2() {
		return ldb;
	}

	@Override
	public MetadataType clone() {
		SubMetadata c = new SubMetadata(ldb, getArray(), getList(), getMap(), getListOfArrays());
		c.ldb = ldb;
		return c;
	}
}
