/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corp. and others
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
package com.ibm.j9ddr.vm29.j9.walkers;

import java.io.PrintStream;
import java.util.NoSuchElementException;

import com.ibm.j9ddr.CorruptDataException;
import com.ibm.j9ddr.util.PatternString;
import com.ibm.j9ddr.vm29.pointer.generated.J9MemorySegmentListPointer;
import com.ibm.j9ddr.vm29.pointer.generated.J9ROMClassPointer;
import com.ibm.j9ddr.vm29.pointer.helper.J9UTF8Helper;

public class FilteredROMClassesIterator extends ROMClassesIterator {

	private final PatternString classPattern;
	public FilteredROMClassesIterator(PrintStream out,
			J9MemorySegmentListPointer segmentList, PatternString classPattern) {
		super(out, segmentList);
		this.classPattern =classPattern;
		nextClass = null;
	}

	J9ROMClassPointer nextClass;
	@Override
	public boolean hasNext() {
		if (null == nextClass) {
			nextClass = next();
		}
		return null != nextClass;
	}
	@Override
	public J9ROMClassPointer next() {
		J9ROMClassPointer candidateClass = nextClass; /* in the case where we called hasNext() previously */
		nextClass = null; /* destructive read */
		try {
			while ((null == candidateClass) && super.hasNext()) {
				candidateClass = super.next();
				String className;
				className = J9UTF8Helper.stringValue(candidateClass.className());
				if (!classPattern.isMatch(className)) {
					candidateClass = null;
				}
			}
		} catch (CorruptDataException e) {			
			throw new NoSuchElementException();
		}
		return candidateClass;
	}
}
