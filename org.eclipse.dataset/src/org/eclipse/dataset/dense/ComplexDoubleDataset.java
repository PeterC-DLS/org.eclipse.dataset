/*-
 * Copyright 2015 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dataset.dense;

import org.apache.commons.math.complex.Complex;

/**
 * Interface for complex dataset to hold double values // PRIM_TYPE
 */
public interface ComplexDoubleDataset extends CompoundDoubleDataset { // CLASS_TYPE

	/**
	 * Get complex value at absolute index in the internal array.
	 * 
	 * This is an internal method with no checks so can be dangerous. Use with care or ideally with an iterator.
	 *
	 * @param index absolute index
	 * @return value
	 */
	Complex getComplexAbs(int index);

	/**
	 * Set values at absolute index in the internal array.
	 * 
	 * This is an internal method with no checks so can be dangerous. Use with care or ideally with an iterator.
	 * @param index absolute index
	 * @param val new values
	 */
	void setAbs(int index, Complex val);

	/**
	 * Set item at index to complex value given by real and imaginary parts 
	 * @param index absolute index
	 * @param real
	 * @param imag
	 */
	void setAbs(int index, double real, double imag); // PRIM_TYPE

	/**
	 * @param i
	 * @return item in given position
	 */
	Complex get(int i);

	/**
	 * @param i
	 * @param j
	 * @return item in given position
	 */
	Complex get(int i, int j);

	/**
	 * @param pos
	 * @return item in given position
	 */
	Complex get(int... pos);

	/**
	 * @param i
	 * @return item in given position
	 */
	double getReal(int i); // PRIM_TYPE

	/**
	 * @param i
	 * @param j
	 * @return item in given position
	 */
	double getReal(int i, int j); // PRIM_TYPE

	/**
	 * @param pos
	 * @return item in given position
	 */
	double getReal(int... pos); // PRIM_TYPE

	/**
	 * @param i
	 * @return item in given position
	 */
	double getImag(int i); // PRIM_TYPE

	/**
	 * @param i
	 * @param j
	 * @return item in given position
	 */
	double getImag(int i, int j); // PRIM_TYPE

	/**
	 * @param pos
	 * @return item in given position
	 */
	double getImag(int... pos); // PRIM_TYPE

	/**
	 * @param i
	 * @return item in given position
	 */
	Complex getComplex(int i);

	/**
	 * @param i
	 * @param j
	 * @return item in given position
	 */
	Complex getComplex(int i, int j);

	/**
	 * @param pos
	 * @return item in given position
	 */
	Complex getComplex(int... pos);

	/**
	 * Set real and imaginary values at given position
	 * @param dr
	 * @param di
	 * @param i
	 */
	void set(double dr, double di, int i); // PRIM_TYPE

	/**
	 * Set real and imaginary values at given position
	 * @param dr
	 * @param di
	 * @param i
	 * @param j
	 */
	void set(double dr, double di, int i, int j); // PRIM_TYPE

	/**
	 * Set real and imaginary values at given position
	 * @param dr
	 * @param di
	 * @param pos
	 */
	void set(double dr, double di, int... pos); // PRIM_TYPE

	/**
	 * @return imaginary part of dataset as new dataset
	 */
	DoubleDataset imaginary(); // CLASS_TYPE

	/**
	 * @return view of imaginary values
	 */
	DoubleDataset imaginaryView(); // CLASS_TYPE
}
