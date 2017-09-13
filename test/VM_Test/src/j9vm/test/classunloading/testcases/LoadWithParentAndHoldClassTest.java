/*******************************************************************************
 * Copyright (c) 2001, 2012 IBM Corp. and others
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
package j9vm.test.classunloading.testcases;

import j9vm.test.classunloading.*;

/**
 *		Create a parent classloader and one child.  Ask the child to load a class and let the parent
 *	find it.  Retain a reference to the class.  The child is unloaded.
 *
 */
public class LoadWithParentAndHoldClassTest extends ClassUnloadingTestParent {
public static void main(String[] args) throws Exception {
	new LoadWithParentAndHoldClassTest().runTest();
}

protected String[] unloadableItems() { 
	return new String[] {"parent", "child", "j9vm.test.classunloading.classestoload.ClassToLoad1"};
}
protected String[] itemsToBeUnloaded() { 
	return new String[] {"child"};
}

Class class1;

protected void createScenario() throws Exception {
	ClassLoader parent = new SelectiveJarClassLoader("parent", jarFileName, "j9vm.test.classunloading.classestoload.ClassToLoad1");
	ClassLoader child = new SelectiveJarClassLoader("child", jarFileName, parent, "j9vm.test.classunloading.classestoload.ClassToLoad1");
	class1 = child.loadClass("j9vm.test.classunloading.classestoload.ClassToLoad1");
	//newInstance() forces clinit:
	class1.newInstance();
}

protected void afterFinalization() throws Exception {
	class1.getClassLoader().loadClass("j9vm.test.classunloading.classestoload.ClassToLoad1").newInstance();
}
}
