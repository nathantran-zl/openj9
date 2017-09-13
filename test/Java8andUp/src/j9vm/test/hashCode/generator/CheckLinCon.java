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
package j9vm.test.hashCode.generator;

public class CheckLinCon {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long count = 0;
		int value = 1;
		LinCon gen = new LinCon();
		do {
			value = gen.runLinCon(value);
			++count;
			if ((count & 0xffffff) == 0) {
				System.out.println("count: " + Long.toHexString(count)
						+ " value: " + Integer.toHexString(value));
			}
		} while (1 != value);
		System.out.println(Long.toHexString(count));

	}

}
