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
package org.openj9.test.javaagenttest.util;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import org.openj9.test.javaagenttest.AgentMain;
import static jdk.internal.org.objectweb.asm.Opcodes.ASM4;

public class CustomClassVisitor extends ClassVisitor {

	String className = null;

	public CustomClassVisitor(ClassVisitor cv, String className) {
		super(ASM4);
		super.cv = cv;
		this.className = className;
	}

	/*
	* Used for instrumenting a method in java/lang/ClassLoader
	* (non-Javadoc)
	* @see jdk.internal.org.objectweb.asm.ClassVisitor#visitMethod(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
	*/
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		AgentMain.logger.debug("CustomClassVisitor.visitMethod get name is: " + name);
		if (name.equals("getResource")) {
			return new MethodInstrumentAdapter_AddPrintOut(access, name, desc,
					super.visitMethod(access, name, desc, signature, exceptions));
		} else {
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}
}
