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

package org.eclipse.dataset.roi;

import java.util.ArrayList;


/**
 * Wrapper for a list of polyline ROIs
 */
public class PolylineROIList extends ArrayList<PolylineROI> implements ROIList<PolylineROI> {

	@Override
	public boolean add(IROI roi) {
		if (roi instanceof PolylineROI)
			return super.add((PolylineROI) roi);
		return false;
	}

	@Override
	public boolean append(PolylineROI roi) {
		return super.add(roi);
	}
}