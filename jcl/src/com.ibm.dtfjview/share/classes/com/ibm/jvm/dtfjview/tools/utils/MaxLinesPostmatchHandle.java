/*[INCLUDE-IF Sidecar18-SE]*/
/*******************************************************************************
 * Copyright (c) 2012, 2017 IBM Corp. and others
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
package com.ibm.jvm.dtfjview.tools.utils;

/**
 * This class caches up to a specified number of lines before a matching is found.
 * It will release the lines whenever it is asked to do so.
 * <p>
 * @author Manqing Li, IBM.
 *
 */
public class MaxLinesPostmatchHandle implements IPostmatchHandle {

	public MaxLinesPostmatchHandle(int maxPostmatchLineNum) {
		this.maxPostmatchLineNum = maxPostmatchLineNum > 0 ? maxPostmatchLineNum : 0;
		this.remainingPostMatchLineNum = 0;
	}
	public void justMatched() {
		remainingPostMatchLineNum = maxPostmatchLineNum;
	}
	public String process(String s) {
		if (0 < remainingPostMatchLineNum) {
			remainingPostMatchLineNum--;
			return s;
		} else {
			return "";
		}
	}
	private final int maxPostmatchLineNum;
	private int remainingPostMatchLineNum;
}
