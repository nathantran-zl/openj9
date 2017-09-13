/*******************************************************************************
 * Copyright (c) 2001, 2017 IBM Corp. and others
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
package com.ibm.jvmti.tests.addToSystemClassLoaderSearch;

import com.ibm.jvmti.tests.util.Util;

public class ascl001 {

	public String helpAddToClasspathDuringOnload()
	{
		return "Check that AddToSystemClassLoaderSearch during OnLoad correctly adds classes and resources. " +
		       "Added as a unit test for J9 VM design ID 450";		
	}
	
	/**
	 * Check that the jar containing TestClass has already been added to the class path by the agent.
	 * 
	 * @return true on pass
	 */
	public boolean testAddToClasspathDuringOnload()
	{
		return Util.loadClassAndResource("TestClass", ascl001.class.getClassLoader());
	}

}
