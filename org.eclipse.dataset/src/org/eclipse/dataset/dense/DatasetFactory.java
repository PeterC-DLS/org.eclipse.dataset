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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dataset.IDataset;
import org.eclipse.dataset.internal.dense.BooleanDatasetImpl;
import org.eclipse.dataset.internal.dense.ByteDatasetImpl;
import org.eclipse.dataset.internal.dense.ComplexDoubleDatasetImpl;
import org.eclipse.dataset.internal.dense.ComplexFloatDatasetImpl;
import org.eclipse.dataset.internal.dense.CompoundByteDatasetImpl;
import org.eclipse.dataset.internal.dense.CompoundDoubleDatasetImpl;
import org.eclipse.dataset.internal.dense.CompoundFloatDatasetImpl;
import org.eclipse.dataset.internal.dense.CompoundIntegerDatasetImpl;
import org.eclipse.dataset.internal.dense.CompoundLongDatasetImpl;
import org.eclipse.dataset.internal.dense.CompoundShortDatasetImpl;
import org.eclipse.dataset.internal.dense.DoubleDatasetImpl;
import org.eclipse.dataset.internal.dense.FloatDatasetImpl;
import org.eclipse.dataset.internal.dense.IntegerDatasetImpl;
import org.eclipse.dataset.internal.dense.LongDatasetImpl;
import org.eclipse.dataset.internal.dense.ObjectDatasetImpl;
import org.eclipse.dataset.internal.dense.ShortDatasetImpl;
import org.eclipse.dataset.internal.dense.StringDatasetImpl;

public class DatasetFactory {

	/**
	 * Create dataset with items ranging from 0 to given stop in steps of 1
	 * @param stop
	 * @param dtype
	 * @return a new dataset of given shape and type, filled with values determined by parameters
	 */
	public static Dataset createRange(final double stop, final int dtype) {
		return createRange(0, stop, 1, dtype);
	}

	/**
	 * Create dataset with items ranging from given start to given stop in given steps
	 * @param start
	 * @param stop
	 * @param step
	 * @param dtype
	 * @return a new 1D dataset of given type, filled with values determined by parameters
	 */
	public static Dataset createRange(final double start, final double stop, final double step, final int dtype) {
		if ((step > 0) != (start <= stop)) {
			throw new IllegalArgumentException("Invalid parameters: start and stop must be in correct order for step");
		}

		switch (dtype) {
		case Dataset.BOOL:
			break;
		case Dataset.INT8:
			return ByteDatasetImpl.createRange(start, stop, step);
		case Dataset.INT16:
			return ShortDatasetImpl.createRange(start, stop, step);
		case Dataset.INT32:
			return IntegerDatasetImpl.createRange(start, stop, step);
		case Dataset.INT64:
			return LongDatasetImpl.createRange(start, stop, step);
		case Dataset.FLOAT32:
			return FloatDatasetImpl.createRange(start, stop, step);
		case Dataset.FLOAT64:
			return DoubleDatasetImpl.createRange(start, stop, step);
		case Dataset.COMPLEX64:
			return ComplexFloatDatasetImpl.createRange(start, stop, step);
		case Dataset.COMPLEX128:
			return ComplexDoubleDatasetImpl.createRange(start, stop, step);
		}
		throw new IllegalArgumentException("dtype not known");
	}

	/**
	 * Create compound dataset with items of given size ranging from 0 to given stop in steps of 1
	 * @param itemSize
	 * @param stop
	 * @param dtype
	 * @return a new dataset of given shape and type, filled with values determined by parameters
	 */
	public static CompoundDataset createRange(final int itemSize, final double stop, final int dtype) {
		return createRange(itemSize, 0, stop, 1, dtype);
	}

	/**
	 * Create compound dataset with items of given size ranging from given start to given stop in given steps
	 * @param itemSize
	 * @param start
	 * @param stop
	 * @param step
	 * @param dtype
	 * @return a new 1D dataset of given type, filled with values determined by parameters
	 */
	public static CompoundDataset createRange(final int itemSize, final double start, final double stop, final double step, final int dtype) {
		if (itemSize < 1) {
			throw new IllegalArgumentException("Item size must be greater or equal to 1");
		}
		if ((step > 0) != (start <= stop)) {
			throw new IllegalArgumentException("Invalid parameters: start and stop must be in correct order for step");
		}

		switch (dtype) {
		case Dataset.BOOL:
			break;
		case Dataset.ARRAYINT8:
		case Dataset.INT8:
			return CompoundByteDatasetImpl.createRange(itemSize, start, stop, step);
		case Dataset.ARRAYINT16:
		case Dataset.INT16:
			return CompoundShortDatasetImpl.createRange(itemSize, start, stop, step);
		case Dataset.ARRAYINT32:
		case Dataset.INT32:
			return CompoundIntegerDatasetImpl.createRange(itemSize, start, stop, step);
		case Dataset.ARRAYINT64:
		case Dataset.INT64:
			return CompoundLongDatasetImpl.createRange(itemSize, start, stop, step);
		case Dataset.ARRAYFLOAT32:
		case Dataset.FLOAT32:
			return CompoundFloatDatasetImpl.createRange(itemSize, start, stop, step);
		case Dataset.ARRAYFLOAT64:
		case Dataset.FLOAT64:
			return CompoundDoubleDatasetImpl.createRange(itemSize, start, stop, step);
		case Dataset.COMPLEX64:
			if (itemSize != 2) {
				throw new IllegalArgumentException("Item size must be equal to 2");
			}
			return ComplexFloatDatasetImpl.createRange(start, stop, step);
		case Dataset.COMPLEX128:
			if (itemSize != 2) {
				throw new IllegalArgumentException("Item size must be equal to 2");
			}
			return ComplexFloatDatasetImpl.createRange(start, stop, step);
		}
		throw new IllegalArgumentException("dtype not known");
	}

	/**
	 * Create a dataset from object (automatically detect dataset type)
	 * 
	 * @param obj
	 *            can be Java list, array or Number
	 * @return dataset
	 */
	public static Dataset createFromObject(Object obj) {
		if (obj instanceof IDataset)
			return DatasetUtils.convertToDataset((IDataset) obj);
		if (obj instanceof BigInteger) {
			obj = ((BigInteger) obj).longValue();
		}

		final int dtype = DTypeUtils.getDTypeFromObject(obj);
		return createFromObject(obj, dtype);
	}

	/**
	 * Create a dataset from object (automatically detect dataset type)
	 * 
	 * @param obj
	 *            can be a Java list, array or Number
	 * @param isUnsigned
	 *            if true, interpret integer values as unsigned by increasing element bit width
	 * @return dataset
	 */
	public static Dataset createFromObject(final Object obj, boolean isUnsigned) {
		Dataset a = createFromObject(obj);
		if (isUnsigned) {
			a = DatasetUtils.makeUnsigned(a);
		}
		return a;
	}

	/**
	 * Create a dataset from object
	 * 
	 * @param obj
	 *            can be a Java list, array or Number
	 * @param dtype
	 * @return dataset
	 * @throws IllegalArgumentException if dataset type is not known
	 */
	public static Dataset createFromObject(final Object obj, final int dtype) {
		if (obj instanceof IDataset)
			return DatasetUtils.cast((IDataset) obj, dtype);

		Class<? extends Object> ca = obj.getClass().getComponentType();
		if (ca != null && (ca.isPrimitive() || ca.equals(String.class))) {
			return DatasetUtils.cast(createFromPrimitiveArray(obj, DTypeUtils.getDTypeFromClass(ca)), dtype);
		}

		switch (dtype) {
		case Dataset.BOOL:
			return BooleanDatasetImpl.createFromObject(obj);
		case Dataset.INT8:
			return ByteDatasetImpl.createFromObject(obj);
		case Dataset.INT16:
			return ShortDatasetImpl.createFromObject(obj);
		case Dataset.INT32:
			return IntegerDatasetImpl.createFromObject(obj);
		case Dataset.INT64:
			return LongDatasetImpl.createFromObject(obj);
		case Dataset.ARRAYINT8:
			return CompoundByteDatasetImpl.createFromObject(obj);
		case Dataset.ARRAYINT16:
			return CompoundShortDatasetImpl.createFromObject(obj);
		case Dataset.ARRAYINT32:
			return CompoundIntegerDatasetImpl.createFromObject(obj);
		case Dataset.ARRAYINT64:
			return CompoundLongDatasetImpl.createFromObject(obj);
		case Dataset.FLOAT32:
			return FloatDatasetImpl.createFromObject(obj);
		case Dataset.FLOAT64:
			return DoubleDatasetImpl.createFromObject(obj);
		case Dataset.ARRAYFLOAT32:
			return CompoundFloatDatasetImpl.createFromObject(obj);
		case Dataset.ARRAYFLOAT64:
			return CompoundDoubleDatasetImpl.createFromObject(obj);
		case Dataset.COMPLEX64:
			return ComplexFloatDatasetImpl.createFromObject(obj);
		case Dataset.COMPLEX128:
			return ComplexDoubleDatasetImpl.createFromObject(obj);
		case Dataset.STRING:
			return StringDatasetImpl.createFromObject(obj);
		case Dataset.OBJECT:
			return ObjectDatasetImpl.createFromObject(obj);
		default:
			throw new IllegalArgumentException("Dataset type is not known");
		}
	}

	private static Dataset createFromPrimitiveArray(final Object array, final int dtype) {
		switch (dtype) {
		case Dataset.BOOL:
			return new BooleanDatasetImpl((boolean []) array);
		case Dataset.INT8:
			return new ByteDatasetImpl((byte []) array);
		case Dataset.INT16:
			return new ShortDatasetImpl((short []) array);
		case Dataset.INT32:
			return new IntegerDatasetImpl((int []) array, null);
		case Dataset.INT64:
			return new LongDatasetImpl((long []) array);
		case Dataset.FLOAT32:
			return new FloatDatasetImpl((float []) array);
		case Dataset.FLOAT64:
			return new DoubleDatasetImpl((double []) array);
		case Dataset.STRING:
			return new StringDatasetImpl((String []) array);
		default:
			return null;
		}
	}

	/**
	 * Create a dataset from an object which could be a Java list, array (of arrays...) or Number. Ragged sequences or
	 * arrays are padded with zeros.
	 * 
	 * @param obj
	 * @return compound dataset with contents given by input
	 */
	public static CompoundDataset createCompoundDatasetFromObject(Object obj) {
		Dataset result = createFromObject(obj);
		return DatasetUtils.createCompoundDatasetFromLastAxis(result, true);
	}

	/**
	 * Create dataset of appropriate type from list
	 * 
	 * @param objectList
	 * @return dataset filled with values from list
	 */
	public static Dataset createFromList(List<?> objectList) {
		if (objectList == null || objectList.size() == 0) {
			throw new IllegalArgumentException("No list or zero-length list given");
		}
		Object obj = null;
		for (Object o : objectList) {
			if (o != null) {
				obj = o;
				break;
			}
		}
		if (obj == null) {
			return zeros(new int[objectList.size()], Dataset.OBJECT);
		}
		Class<? extends Object> clazz = obj.getClass();
		if (DTypeUtils.isComponentSupported(clazz)) {
			int dtype = DTypeUtils.getDTypeFromClass(clazz);
			int len = objectList.size();
			Dataset result = zeros(new int[] { len }, dtype);

			int i = 0;
			for (Object object : objectList) {
				result.setObjectAbs(i++, object);
			}
			return result;
		}
		throw new IllegalArgumentException("Class of list element not supported");
	}

	/**
	 * @param shape
	 * @param dtype
	 * @return a new dataset of given shape and type, filled with zeros
	 */
	public static Dataset zeros(final int[] shape, final int dtype) {
		switch (dtype) {
		case Dataset.BOOL:
			return new BooleanDatasetImpl(shape);
		case Dataset.INT8:
		case Dataset.ARRAYINT8:
			return new ByteDatasetImpl(shape);
		case Dataset.INT16:
		case Dataset.ARRAYINT16:
			return new ShortDatasetImpl(shape);
		case Dataset.RGB:
			return new RGBDataset(shape);
		case Dataset.INT32:
		case Dataset.ARRAYINT32:
			return new IntegerDatasetImpl(shape);
		case Dataset.INT64:
		case Dataset.ARRAYINT64:
			return new LongDatasetImpl(shape);
		case Dataset.FLOAT32:
		case Dataset.ARRAYFLOAT32:
			return new FloatDatasetImpl(shape);
		case Dataset.FLOAT64:
		case Dataset.ARRAYFLOAT64:
			return new DoubleDatasetImpl(shape);
		case Dataset.COMPLEX64:
			return new ComplexFloatDatasetImpl(shape);
		case Dataset.COMPLEX128:
			return new ComplexDoubleDatasetImpl(shape);
		case Dataset.STRING:
			return new StringDatasetImpl(shape);
		case Dataset.OBJECT:
			return new ObjectDatasetImpl(shape);
		}
		throw new IllegalArgumentException("dtype not known or unsupported");
	}

	/**
	 * @param itemSize
	 *            if equal to 1, then non-compound dataset is returned
	 * @param shape
	 * @param dtype
	 * @return a new dataset of given item size, shape and type, filled with zeros
	 */
	public static Dataset zeros(final int itemSize, final int[] shape, final int dtype) {
		if (itemSize == 1) {
			return zeros(shape, dtype);
		}
		switch (dtype) {
		case Dataset.INT8:
		case Dataset.ARRAYINT8:
			return new CompoundByteDatasetImpl(itemSize, shape);
		case Dataset.INT16:
		case Dataset.ARRAYINT16:
			return new CompoundShortDatasetImpl(itemSize, shape);
		case Dataset.RGB:
			if (itemSize != 3) {
				throw new IllegalArgumentException("Number of elements not compatible with RGB type");
			}
			return new RGBDataset(shape);
		case Dataset.INT32:
		case Dataset.ARRAYINT32:
			return new CompoundIntegerDatasetImpl(itemSize, shape);
		case Dataset.INT64:
		case Dataset.ARRAYINT64:
			return new CompoundLongDatasetImpl(itemSize, shape);
		case Dataset.FLOAT32:
		case Dataset.ARRAYFLOAT32:
			return new CompoundFloatDatasetImpl(itemSize, shape);
		case Dataset.FLOAT64:
		case Dataset.ARRAYFLOAT64:
			return new CompoundDoubleDatasetImpl(itemSize, shape);
		case Dataset.COMPLEX64:
			if (itemSize != 2) {
				throw new IllegalArgumentException("Number of elements not compatible with complex type");
			}
			return new ComplexFloatDatasetImpl(shape);
		case Dataset.COMPLEX128:
			if (itemSize != 2) {
				throw new IllegalArgumentException("Number of elements not compatible with complex type");
			}
			return new ComplexDoubleDatasetImpl(shape);
		}
		throw new IllegalArgumentException("dtype not a known compound type");
	}

	/**
	 * @param dataset
	 * @return a new dataset of same shape and type as input dataset, filled with zeros
	 */
	public static Dataset zeros(final Dataset dataset) {
		return zeros(dataset, dataset.getDType());
	}

	/**
	 * Create a new dataset of same shape as input dataset, filled with zeros. If dtype is not
	 * explicitly compound then an elemental dataset is created 
	 * @param dataset
	 * @param dtype
	 * @return a new dataset
	 */
	public static Dataset zeros(final Dataset dataset, final int dtype) {
		final int[] shape = dataset.getShapeRef();
		final int isize = DTypeUtils.isDTypeElemental(dtype) ? 1 :dataset.getElementsPerItem();

		return zeros(isize, shape, dtype);
	}

	/**
	 * @param dataset
	 * @return a new dataset of same shape and type as input dataset, filled with ones
	 */
	public static Dataset ones(final Dataset dataset) {
		return ones(dataset, dataset.getDType());
	}

	/**
	 * Create a new dataset of same shape as input dataset, filled with ones. If dtype is not
	 * explicitly compound then an elemental dataset is created
	 * @param dataset
	 * @param dtype
	 * @return a new dataset
	 */
	public static Dataset ones(final Dataset dataset, final int dtype) {
		final int[] shape = dataset.getShapeRef();
		final int isize = DTypeUtils.isDTypeElemental(dtype) ? 1 :dataset.getElementsPerItem();

		return ones(isize, shape, dtype);
	}

	/**
	 * @param shape
	 * @param dtype
	 * @return a new dataset of given shape and type, filled with ones
	 */
	public static Dataset ones(final int[] shape, final int dtype) {
		switch (dtype) {
		case Dataset.BOOL:
			return BooleanDatasetImpl.ones(shape);
		case Dataset.INT8:
			return ByteDatasetImpl.ones(shape);
		case Dataset.INT16:
			return ShortDatasetImpl.ones(shape);
		case Dataset.RGB:
			return new RGBDataset(shape).fill(1);
		case Dataset.INT32:
			return IntegerDatasetImpl.ones(shape);
		case Dataset.INT64:
			return LongDatasetImpl.ones(shape);
		case Dataset.FLOAT32:
			return FloatDatasetImpl.ones(shape);
		case Dataset.FLOAT64:
			return DoubleDatasetImpl.ones(shape);
		case Dataset.COMPLEX64:
			return ComplexFloatDatasetImpl.ones(shape);
		case Dataset.COMPLEX128:
			return ComplexDoubleDatasetImpl.ones(shape);
		}
		throw new IllegalArgumentException("dtype not known");
	}

	/**
	 * @param itemSize
	 *            if equal to 1, then non-compound dataset is returned
	 * @param shape
	 * @param dtype
	 * @return a new dataset of given item size, shape and type, filled with ones
	 */
	public static Dataset ones(final int itemSize, final int[] shape, final int dtype) {
		if (itemSize == 1) {
			return ones(shape, dtype);
		}
		switch (dtype) {
		case Dataset.INT8:
		case Dataset.ARRAYINT8:
			return CompoundByteDatasetImpl.ones(itemSize, shape);
		case Dataset.INT16:
		case Dataset.ARRAYINT16:
			return CompoundShortDatasetImpl.ones(itemSize, shape);
		case Dataset.RGB:
			if (itemSize != 3) {
				throw new IllegalArgumentException("Number of elements not compatible with RGB type");
			}
			return new RGBDataset(shape).fill(1);
		case Dataset.INT32:
		case Dataset.ARRAYINT32:
			return CompoundIntegerDatasetImpl.ones(itemSize, shape);
		case Dataset.INT64:
		case Dataset.ARRAYINT64:
			return CompoundLongDatasetImpl.ones(itemSize, shape);
		case Dataset.FLOAT32:
		case Dataset.ARRAYFLOAT32:
			return CompoundFloatDatasetImpl.ones(itemSize, shape);
		case Dataset.FLOAT64:
		case Dataset.ARRAYFLOAT64:
			return CompoundDoubleDatasetImpl.ones(itemSize, shape);
		case Dataset.COMPLEX64:
			if (itemSize != 2) {
				throw new IllegalArgumentException("Number of elements not compatible with complex type");
			}
			return ComplexFloatDatasetImpl.ones(shape);
		case Dataset.COMPLEX128:
			if (itemSize != 2) {
				throw new IllegalArgumentException("Number of elements not compatible with complex type");
			}
			return ComplexDoubleDatasetImpl.ones(shape);
		}
		throw new IllegalArgumentException("dtype not a known compound type");
	}
}
