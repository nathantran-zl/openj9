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
package com.ibm.j9.recreateclass.testclasses;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

/**
 * This class generates an interface to be used by class generated by SharedInvokersTestGenerator.
 * 
 * @author ashutosh
 */
public class SharedInvokersTestIntfGenerator {
	public static byte[] generateClassData() {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		MethodVisitor mv;

		cw.visit(
				V1_7,
				ACC_ABSTRACT + ACC_INTERFACE + ACC_PUBLIC,
				"com/ibm/j9/recreateclasscompare/testclasses/SharedInvokersTestIntf",
				null, "java/lang/Object", null);
		{
			mv = cw.visitMethod(ACC_PUBLIC, "foo", "()V", null, null);
			mv.visitCode();
			mv.visitInsn(RETURN);
			mv.visitMaxs(0, 1);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}
}
