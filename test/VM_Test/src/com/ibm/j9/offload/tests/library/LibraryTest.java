package com.ibm.j9.offload.tests.library;

/*******************************************************************************
 * Copyright (c) 2008, 2012 IBM Corp. and others
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] http://openjdk.java.net/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *******************************************************************************/

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

public class LibraryTest extends TestCase {
	/**
	 * method that can be used to run the test by itself
	 * 
	 * @param args no valid arugments
	 */
	public static void main (String[] args) {
		junit.textui.TestRunner.run(suite());
	}
	
	public static Test suite(){
		return new TestSuite(LibraryTest.class);
	}
	
	/**
	 * tests ExceptionClear
	 */
	public void testOnLoad(){
		System.loadLibrary(LibraryNatives.NATIVE_LIBRARY_NAME);
		assertTrue("Does not appear OnLoad ran correctly",LibraryNatives.getOnLoadCalled() == 1);
	}
}
