/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dataset;

import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;

import org.eclipse.dataset.dense.DTypeUtils;
import org.eclipse.dataset.dense.Dataset;
import org.eclipse.dataset.dense.IndexIterator;
import org.eclipse.dataset.dense.ObjectDataset;
import org.eclipse.dataset.dense.StringDataset;
import org.junit.Assert;

public class TestUtils {
	

	/**
	 * Utility function to skip a JUnit test if the specified condition is true.
	 * If called from a method annotated with @Test, and condition is true, the @Test method will halt and be ignored (skipped).
	 * If called from a method annotated with @Before or @BeforeClass, all @Test methods of the class are ignored (skipped).
	 * 
	 * Existing test runners (we're talking JUnit 4.5 and Ant 1.7.1, as bundled with Eclipse 3.5.1, don't have the concept of a
	 * skipped test (tests are classified as either a pass or fail). Tests that fail an assumption are reported as passed.
	 * 
	 * Internally, a failing assumption throws an AssumptionViolatedException (in JUnit 4,5; this may have changed in later releases).
	 * 
	 * @param condition - boolean specifying whether the test method or test class is to be skipped
	 * @param reason - explanation of why the test is skipped
	 */
	public static void skipTestIf(boolean condition, String reason) {
		if (condition) {
			System.out.println("JUnit test skipped: " + reason);
			assumeTrue(false);
		}
	}
	/**
	 * Utility function to skip a JUnit test.
	 * @param reason - explanation of why the test is skipped
	 */
	public static void skipTest(String reason) {
		System.out.println("JUnit test skipped: " + reason);
		assumeTrue(false);
	}

	/**
	 * Prefix to folder in which test files are to be generated
	 */
	public static final String OUTPUT_FOLDER_PREFIX = "test-scratch/";
	
	/**
	 * Generates a (relative) directory name based on a class name. Uses the appropriate separators for the platform.
	 * 
	 * @param classname
	 *            - the name of the class on which to base the directory name (a value something like
	 *            "gda.analysis.io.JPEGTest").
	 * @return - the derived directory name.
	 */
	public static String generateDirectorynameFromClassname(String classname) {
		// the generated directory name is usable on both Linux and Windows
		// (which uses \ as a separator).
		return OUTPUT_FOLDER_PREFIX + classname.replace('.', '/') + '/';
		// File tmp = new File(OUTPUT_FOLDER_PREFIX + classname.replace('.',
		// '/') + '/');
		// return tmp.getAbsolutePath();
	}

	/**
	 * Helper function to (recursively) delete a directory and all its contents
	 * 
	 * @param dir
	 *            - path to directory to delete
	 * @return - boolean True if directory no longer exists
	 */
	static boolean deleteDir(File dir) {
		if (!dir.exists()) {
			return true;
		}
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (String element : children) {
				boolean success = deleteDir(new File(dir, element));
				if (!success) {
					System.out.println("1. deleteDir could not delete: " + new File(dir, element));
					return false;
				}
			}
		}
		if (dir.delete()){
			return true;
		}
		System.out.println("2. deleteDir could not delete: " + dir +". Make sure each test creates a uniquely named folder. Otherwise stale NFS locks may prevent folder deletion.");
		return false;
	}


	/**
	 * Creates a scratch directory for test files created by the specified class.
	 * 
	 * @param clazz
	 *            the class
	 * @return the directory
	 */
	public static File createClassScratchDirectory(Class<?> clazz) {
		File dir = new File(generateDirectorynameFromClassname(clazz.getCanonicalName()));
		dir.mkdirs();
		return dir;
	}

	/**
	 * Creates an empty directory for use by test code. If the directory exists (from a previous test), the directory
	 * and all its contents are first deleted.
	 * 
	 * @param testScratchDirectoryname
	 *            - the name of the directory to create.
	 * @throws Exception
	 */
	public static void makeScratchDirectory(String testScratchDirectoryname) throws Exception {
		// delete any remains from a previous run of this test
		if (!deleteDir(new File(testScratchDirectoryname))) {
			throw new Exception("Unable to delete old test scratch directory " + testScratchDirectoryname);
		}
		// set up for a new run of this test
		if (!((new File(testScratchDirectoryname)).mkdirs())) {
			throw new Exception("Unable to create new test scratch directory " + testScratchDirectoryname);
		}
	}

	/**
	 * Returns a {@link File} for the specified resource, associated with the
	 * specified class.
	 * 
	 * @param clazz the class with which the resource is associated
	 * @param name the desired resource
	 * 
	 * @return a {@link File} for the resource, if it is found
	 * 
	 * @throws FileNotFoundException if the resource cannot be found
	 */
	public static File getResourceAsFile(Class<?> clazz, String name) throws FileNotFoundException {
		URL url = clazz.getResource(name);
		if (url == null) {
			throw new FileNotFoundException(name + " (resource not found)");
		}
		return new File(url.getFile());
	}

	/**
	 * Sets up of environment for the a test Set property so that output is to Nexus format file Uses
	 * MockJythonServerFacade and MockJythonServer to configure InterfaceProvider Configure logging so that DEBUG and
	 * above go to log.txt in output folder
	 * 
	 * @param testClass
	 *            e.g. gda.data.nexus.ScanToNexusTest
	 * @param nameOfTest
	 *            name of test method which the testClass e.g. testCreateScanFile
	 * @param makedir
	 *            if true the scratch dir is deleted and constructed
	 * @return The directory into which output will be sent
	 * @throws Exception
	 *             if setup fails
	 */
	public static String setUpTest(Class<?> testClass, String nameOfTest, boolean makedir) throws Exception {
		String name = testClass.getCanonicalName();
		if (name == null) {
			throw new IllegalArgumentException("getCanonicalName failed for class " + testClass.toString());
		}
		String testScratchDirectoryName = TestUtils.generateDirectorynameFromClassname(testClass.getCanonicalName())
				+ nameOfTest;

		if (makedir) {
			TestUtils.makeScratchDirectory(testScratchDirectoryName);
		}

		
		return testScratchDirectoryName;
	}	
	
	/**
	 * Assert equality if abs(e - a) <= max(1e-20, 1e-14*max(abs(e), abs(a)))
	 * @param s message for assert exception
	 * @param e expected value
	 * @param a actual value
	 */
	public static void assertEquals(String s, double e, double a) {
		assertEquals(s, e, a, 1e-14, 1e-20);
	}
	/**
	 * Assert equality if abs(e - a) <= max(absTol, relTol*max(abs(e), abs(a)))
	 * @param s message for assert exception
	 * @param e expected value
	 * @param a actual value
	 * @param relTol relative tolerance
	 * @param absTol absolute tolerance
	 */
	public static void assertEquals(String s, double e, double a, double relTol, double absTol) {
		double t = Math.max(absTol, relTol*Math.max(Math.abs(e), Math.abs(a)));
		Assert.assertEquals(s, e, a, t);
	}

	/**
	 * Assert equality of datasets where each element is true if abs(e - a) <= max(absTol, relTol*max(abs(e), abs(a)))
	 * @param expected
	 * @param calc
	 * @param testDType
	 * @param relTolerance
	 * @param absTolerance
	 */
	public static void assertDatasetEquals(Dataset expected, Dataset calc, boolean testDType, double relTol, double absTol) {
		int dtype = expected.getDType();
		if (testDType) {
			Assert.assertEquals("Type", dtype, calc.getDType());
			Assert.assertEquals("Items", expected.getElementsPerItem(), calc.getElementsPerItem());
		}
		Assert.assertEquals("Size", expected.getLongSize(), calc.getLongSize());
		try {
			Assert.assertArrayEquals("Shape", expected.getShape(), calc.getShape());
		} catch (AssertionError e) {
			if (calc.getLongSize() == 1) {
				Assert.assertArrayEquals("Shape", new int[0], calc.getShape());
			} else {
				throw e;
			}
		}
		IndexIterator at = expected.getIterator(true);
		IndexIterator bt = calc.getIterator();
		final int eis = expected.getElementsPerItem();
		final int cis = calc.getElementsPerItem();
		final int is = Math.max(eis, cis);
	
		if (dtype == Dataset.BOOL) {
			while (at.hasNext() && bt.hasNext()) {
				for (int j = 0; j < is; j++) {
					boolean e = j >= eis ? false : expected.getElementBooleanAbs(at.index + j);
					boolean c = j >= cis ? false : calc.getElementBooleanAbs(bt.index + j);
					Assert.assertEquals("Value does not match at " + Arrays.toString(at.getPos()) + "; " + j +
							": ", e, c);
				}
			}
		} else if (DTypeUtils.isDTypeFloating(dtype)) {
			while (at.hasNext() && bt.hasNext()) {
				for (int j = 0; j < is; j++) {
					double e = j >= eis ? 0 : expected.getElementDoubleAbs(at.index + j);
					double c = j >= cis ? 0 : calc.getElementDoubleAbs(bt.index + j);
					double t = Math.max(absTol, relTol*Math.max(Math.abs(e), Math.abs(c)));
					Assert.assertEquals("Value does not match at " + Arrays.toString(at.getPos()) + "; " + j +
							": ", e, c, t);
				}
			}
		} else if (dtype == Dataset.STRING) {
			StringDataset es = (StringDataset) expected;
			StringDataset cs = (StringDataset) calc;
	
			while (at.hasNext() && bt.hasNext()) {
				Assert.assertEquals("Value does not match at " + Arrays.toString(at.getPos()) + ": ",
						es.getAbs(at.index), cs.getAbs(bt.index));
			}
		} else if (dtype == Dataset.OBJECT) {
			ObjectDataset eo = (ObjectDataset) expected;
			ObjectDataset co = (ObjectDataset) calc;
	
			while (at.hasNext() && bt.hasNext()) {
				Assert.assertEquals("Value does not match at " + Arrays.toString(at.getPos()) + ": ",
						eo.getAbs(at.index), co.getAbs(bt.index));
			}
		} else {
			while (at.hasNext() && bt.hasNext()) {
				for (int j = 0; j < is; j++) {
					long e = j >= eis ? 0 : expected.getElementLongAbs(at.index + j);
					long c = j >= cis ? 0 : calc.getElementLongAbs(bt.index + j);
					Assert.assertEquals("Value does not match at " + Arrays.toString(at.getPos()) + "; " + j +
							": ", e, c);
				}
			}
		}
	}

	/**
	 * Assert equality of datasets where each element is true if abs(e - a) <= max(absTol, relTol*max(abs(e), abs(a)))
	 * @param expected
	 * @param calc
	 * @param relTolerance
	 * @param absTolerance
	 */
	public static void assertDatasetEquals(Dataset expected, Dataset calc, double relTol, double absTol) {
		assertDatasetEquals(expected, calc, true, relTol, absTol);
	}

	/**
	 * Assert equality of datasets where each element is true if abs(e - a) <= max(abs1e-5, 1e-5*max(abs(e), abs(a)))
	 * @param expected
	 * @param calc
	 * @param relTolerance
	 * @param absTolerance
	 */
	public static void assertDatasetEquals(Dataset calc, Dataset expected) {
		assertDatasetEquals(expected, calc, 1e-5, 1e-5);
	}
}
