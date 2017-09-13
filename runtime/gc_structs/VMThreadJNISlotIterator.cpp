
/*******************************************************************************
 * Copyright (c) 1991, 2014 IBM Corp. and others
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

/**
 * @file
 * @ingroup GC_Structs
 */

#include "j9.h"
#include "j9cfg.h"

#include "VMThreadJNISlotIterator.hpp"

/**
 * @return the next slot containing a JNI reference
 * @return NULL if there are no more such slots
 */
j9object_t *
GC_VMThreadJNISlotIterator::nextSlot()
{
	while(_jniFrame) {
		j9object_t *objectPtr;

		objectPtr = (j9object_t *)_poolIterator.nextSlot();
		if(objectPtr) {
			return objectPtr;
		}

		_jniFrame = _jniFrame->previous;
		_poolIterator.init(_jniFrame ? (J9Pool *)_jniFrame->references : (J9Pool *)NULL);
	}
	return NULL;
}

